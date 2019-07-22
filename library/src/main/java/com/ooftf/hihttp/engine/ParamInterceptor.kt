package com.ooftf.hihttp.engine

import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

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
        if (request.method().equals("POST", ignoreCase = true)) {
            val oldParams = HashMap<String, String>()
            if (body is FormBody) {
                val formBody = body as FormBody?
                for (i in 0 until formBody!!.size()) {
                    oldParams.put(formBody.name(i), formBody.value(i))
                }
            }
            val newFormBody = buildNewFormBody(oldParams)
            val newBuilder = request.newBuilder().method(request.method(), newFormBody)
            getAddHeaders().forEach {
                newBuilder.addHeader(it.key, it.value)
            }
            return chain.proceed(newBuilder.build())
        } else if (request.method().equals("GET", true)) {
            request.url().queryParameterNames()
            var params = getUrlParams(request.url())
            params = paramTransform(params)
            val newUrl = buildWithNewParams(request.url(), params)
            val newBuilder = request.newBuilder().url(newUrl)
            getAddHeaders().forEach {
                newBuilder.addHeader(it.key, it.value)
            }
            return chain.proceed(newBuilder.build())
        }
        return chain.proceed(request)
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
        var result = HashMap<String, String>()
        url.queryParameterNames().forEach {
            var value = url.queryParameter(it)
            if (value != null) {
                result.put(it, value)
            }
        }
        return result
    }

    internal fun buildNewFormBody(oldParams: MutableMap<String, String>): FormBody {
        var newParams = paramTransform(oldParams)
        val builder = FormBody.Builder()
        for ((key, value) in newParams) {
            builder.add(key, value)
        }
        return builder.build()
    }

    abstract fun paramTransform(oldParams: MutableMap<String, String>): MutableMap<String, String>


    abstract fun getAddHeaders(): Map<String, String>
}
