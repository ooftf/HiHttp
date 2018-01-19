package com.ooftf.hi.engine

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * Created by master on 2017/3/7.
 */

abstract class FormParamInterceptor : Interceptor {

    @Throws(IOException::class)
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
            val newRequest = request.newBuilder().method(request.method(), newFormBody).build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
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

    companion object {
        private val TAG = FormParamInterceptor::class.java.simpleName
    }
}
