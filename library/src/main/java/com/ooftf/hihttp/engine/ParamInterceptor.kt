package com.ooftf.hihttp.engine

import okhttp3.*
import kotlin.collections.LinkedHashMap

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
        val body = request.body()
        val newBuilder = request.newBuilder()
        var params:MutableMap<String,String>? = null
        if (request.method().equals("GET", true)) {
            request.url().queryParameterNames()
            params = getUrlParams(request.url())
            params = paramTransform(params)
            val newUrl = buildWithNewParams(request.url(), params)
            newBuilder.url(newUrl)
        } else {
            var newRequestBody: RequestBody?
            if (body is FormBody) {
                params = LinkedHashMap()
                val formBody = body as FormBody?
                for (i in 0 until formBody!!.size()) {
                    params[formBody.name(i)] = formBody.value(i)
                }
                val newFormBody = buildNewFormBody(params)
                newRequestBody = newFormBody
            } else {
                newRequestBody = body
            }
            newBuilder.method(request.method(), newRequestBody)
        }
        getAddHeaders(params).forEach {
            newBuilder.addHeader(it.key, it.value)
        }
        return chain.proceed(newBuilder.build())
    }

    private fun buildWithNewParams(url: HttpUrl, params: MutableMap<String, String>): HttpUrl {
        val newBuilder = url.newBuilder()
        url.queryParameterNames().forEach {
            newBuilder.removeAllQueryParameters(it)
        }
        params.forEach {
            newBuilder.addQueryParameter(it.key, it.value)
        }
        return newBuilder.build()
    }

    private fun getUrlParams(url: HttpUrl): MutableMap<String, String> {
        var result = LinkedHashMap<String, String>()
        url.queryParameterNames().forEach {
            var value = url.queryParameter(it)
            if (value != null) {
                result[it] = value
            }
        }
        return result
    }

    private fun buildNewFormBody(oldParams: MutableMap<String, String>): FormBody {
        var newParams = paramTransform(oldParams)
        val builder = FormBody.Builder()
        for ((key, value) in newParams) {
            builder.add(key, value)
        }
        return builder.build()
    }

    abstract fun paramTransform(oldParams: MutableMap<String, String>): MutableMap<String, String>


    abstract fun getAddHeaders(params: MutableMap<String, String>?): Map<String, String>
}
