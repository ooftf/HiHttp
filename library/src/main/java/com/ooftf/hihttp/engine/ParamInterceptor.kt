package com.ooftf.hihttp.engine

import okhttp3.*
import retrofit2.http.Body

/**
 *
 * 参数拦截器，可以修改请求参数
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
 */
abstract class ParamInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        val newBuilder = request.newBuilder()
        var params: MutableMap<String, String>? = null
        if (request.method.equals("GET", true)) {
            params = paramTransform(getUrlParams(request.url))
            val newUrl = buildWithNewParams(request.url, params)
            newBuilder.url(newUrl)
        } else {
            val newRequestBody: RequestBody? = when (requestBody) {
                is FormBody -> {
                    params = paramTransform(getFormBodyParam(requestBody))
                    buildNewFormBody(params)
                }
                is MultipartBody -> {
                    requestBody
                }
                else -> {
                    requestBody
                }
            }
            newBuilder.method(request.method, newRequestBody)
        }
        getAddHeaders(params).forEach {
            newBuilder.addHeader(it.key, it.value)
        }
        return chain.proceed(newBuilder.build())
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

    private fun buildNewFormBody(params: MutableMap<String, String>): FormBody {
        val builder = FormBody.Builder()
        for ((key, value) in params) {
            builder.add(key, value)
        }
        return builder.build()
    }

    abstract fun paramTransform(oldParams: MutableMap<String, String>): MutableMap<String, String>


    abstract fun getAddHeaders(params: MutableMap<String, String>?): Map<String, String>
}
