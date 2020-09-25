package com.ooftf.hihttp.engine

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.net.ssl.HostnameVerifier


/**
 *
 * 配合retrofit生成service
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
 */
open class ServiceGenerator
internal constructor(private var baseUrl: String?, private var ignoreSSL: Boolean, private var keepCookie: Boolean, var buildOkhttp: ((OkHttpClient.Builder) -> Unit)?, private var buildRetrofit: ((Retrofit.Builder) -> Unit)?) {

    private fun createIgnoreHostnameVerifier() = HostnameVerifier { p0, p1 -> true }
    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (keepCookie) {
            builder.cookieJar(KeepCookieJar())
        }
        if (ignoreSSL) {
            OkHttpHelper.createX509TrustManager().let {
                builder.hostnameVerifier(createIgnoreHostnameVerifier())
                        .sslSocketFactory(OkHttpHelper.createIgnoreSSLSocketFactory(it), it)
            }
        }
        buildOkhttp?.invoke(builder)
        return builder.build()
    }

    private fun createRetrofit(): Retrofit {
        val builder = Retrofit.Builder()
        baseUrl?.let {
            builder.baseUrl(it)
        }
        builder
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(io.reactivex.rxjava3.schedulers.Schedulers.io()))
                .client(createOkHttpClient())
        buildRetrofit?.invoke(builder)
        return builder.build()
    }

    fun <T> createService(cla: Class<T>) = createRetrofit().create(cla)

}