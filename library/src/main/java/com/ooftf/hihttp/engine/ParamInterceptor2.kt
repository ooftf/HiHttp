package com.ooftf.hihttp.engine

import okhttp3.*
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
        val rw = RequestWrapper(request)
        // GET请求
        if (request.method.equals("GET", true)) {
            val newBuilder = request.newBuilder()
            //params = paramTransform(getUrlParams(request.url))
            val get = get(rw)
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
                    val postFormBody = postFormBody(rw)
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
                    return chain.proceed(postMutableBody(rw))
                }
                else -> {
                    if (rw.isJsonBody()) {
                        val newBuilder = request.newBuilder()
                        val postJsonBody = postJsonBody(rw)
                        val newRequestBody = object : RequestBody() {
                            override fun contentType(): MediaType? {
                                return requestBody?.contentType()
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
                        return chain.proceed(postOtherBody(rw))
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

    abstract fun get(rw: RequestWrapper): ParamAndHeader
    abstract fun postFormBody(rw: RequestWrapper): ParamAndHeader
    abstract fun postJsonBody(rw: RequestWrapper): JsonParamAndHeader
    abstract fun postMutableBody(rw: RequestWrapper): Request
    abstract fun postOtherBody(rw: RequestWrapper): Request
}

class ParamAndHeader(var param: MutableMap<String, String> = HashMap(),
                     var header: MutableMap<String, String> = HashMap())

class JsonParamAndHeader(var json: String,
                         var header: MutableMap<String, String> = HashMap())
