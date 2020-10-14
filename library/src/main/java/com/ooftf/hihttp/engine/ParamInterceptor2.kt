package com.ooftf.hihttp.engine

import okhttp3.*
import okio.Buffer
import okio.BufferedSink

/**
 *
 * 参数拦截器，可以修改请求参数
 * 以请求类型和请求body格式来区分
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
 */
abstract class ParamInterceptor2 : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // GET请求
        if (request.method.equals("GET", true)) {
            val newBuilder = request.newBuilder()
            //params = paramTransform(getUrlParams(request.url))
            val get = get(getUrlParams(request.url), getHeader(request))
            val newUrl = buildWithNewParams(request.url, get.param)
            newBuilder.url(newUrl)
            get.header.forEach {
                newBuilder.addHeader(it.key, it.value)
            }
            return chain.proceed(newBuilder.build())
        } else {
            when (val requestBody = request.body) {
                is FormBody -> {
                    val newBuilder = request.newBuilder()
                    val postFormBody = postFormBody(getFormBodyParam(requestBody), getHeader(request))
                    val builder = FormBody.Builder()
                    postFormBody.param.forEach {
                        builder.add(it.key, it.value)
                    }
                    val newRequestBody = builder.build()
                    newBuilder.method(request.method, newRequestBody)
                    postFormBody.header.forEach {
                        newBuilder.addHeader(it.key, it.value)
                    }
                    return chain.proceed(newBuilder.build())
                }
                is MultipartBody -> {
                    return chain.proceed(postMutableBody(request))
                }
                else -> {
                    if (requestBody?.contentType()?.subtype == "json") {
                        val newBuilder = request.newBuilder()
                        val buffer = Buffer()
                        requestBody.writeTo(buffer)
                        val postJsonBody = postJsonBody(buffer.readByteString().utf8(), getHeader(request))
                        val newRequestBody = object : RequestBody() {
                            override fun contentType(): MediaType? {
                                return requestBody.contentType()
                            }

                            override fun writeTo(sink: BufferedSink) {
                                sink.buffer.writeUtf8(postJsonBody.json)
                            }
                        }
                        newBuilder.method(request.method, newRequestBody)
                        postJsonBody.header.forEach {
                            newBuilder.addHeader(it.key, it.value)
                        }
                        return chain.proceed(newBuilder.build())
                    } else {
                        return chain.proceed(postOtherBody(request))
                    }

                }
            }
        }
    }

    private fun buildWithNewParams(url: HttpUrl, params: MutableMap<String, String>): HttpUrl {
        val newBuilder = url.newBuilder()
        url.queryParameterNames.forEach {
            newBuilder.removeAllQueryParameters(it)
        }
        params.forEach {
            newBuilder.addQueryParameter(it.key, it.value)
        }
        return newBuilder.build()
    }

    fun getFormBodyParam(body: FormBody): MutableMap<String, String> {
        val result = LinkedHashMap<String, String>()
        for (i in 0 until body.size) {
            result[body.name(i)] = body.value(i)
        }
        return result
    }

    private fun getUrlParams(url: HttpUrl): MutableMap<String, String> {
        var result = LinkedHashMap<String, String>()
        url.queryParameterNames.forEach {
            var value = url.queryParameter(it)
            if (value != null) {
                result[it] = value
            }
        }
        return result
    }

    private fun getHeader(request: Request): HashMap<String, String> {
        val header = HashMap<String, String>()
        request.headers.forEach {
            header[it.first] = it.second
        }
        return header
    }

    abstract fun get(oldParams: MutableMap<String, String>, oldHeader: MutableMap<String, String>): ParamAndHeader
    abstract fun postFormBody(oldParams: MutableMap<String, String>, oldHeader: MutableMap<String, String>): ParamAndHeader
    abstract fun postJsonBody(jsonString: String, oldHeader: MutableMap<String, String>): JsonParamAndHeader
    abstract fun postMutableBody(request: Request): Request
    abstract fun postOtherBody(request: Request): Request


}

class ParamAndHeader(var param: MutableMap<String, String> = HashMap(),
                     var header: MutableMap<String, String> = HashMap())

class JsonParamAndHeader(var json: String,
                         var header: MutableMap<String, String> = HashMap())
