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
        if (rw.isGet()) {
            return chain.proceed(get(rw))
        } else {
            return when (request.body) {
                is FormBody -> {
                    chain.proceed(postFormBody(rw))
                }
                is MultipartBody -> {
                    chain.proceed(postMutableBody(rw))
                }
                else -> {
                    if (rw.isJsonBody()) {
                        chain.proceed(postJsonBody(rw))
                    } else {
                        chain.proceed(postOtherBody(rw))
                    }
                }
            }
        }
    }

    abstract fun get(rw: RequestWrapper): Request
    abstract fun postFormBody(rw: RequestWrapper): Request
    abstract fun postJsonBody(rw: RequestWrapper): Request
    abstract fun postMutableBody(rw: RequestWrapper): Request
    abstract fun postOtherBody(rw: RequestWrapper): Request
}
