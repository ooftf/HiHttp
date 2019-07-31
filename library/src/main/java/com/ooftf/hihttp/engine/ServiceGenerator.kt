package com.ooftf.hihttp.engine

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


/**
 *
 * 配合retrofit生成service
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
 */
open class ServiceGenerator internal constructor(private var baseUrl: String?,private var ignoreSSL: Boolean,private var keepCookie: Boolean, var buildOkhttp: ((OkHttpClient.Builder) -> Unit)?,private var buildRetrofit: ((Retrofit.Builder) -> Unit)?) {

    private fun createIgnoreSSLSocketFactory(): SSLSocketFactory {
        val ssl = SSLContext.getInstance("SSL")
        val xtm = object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        ssl.init(null, arrayOf<TrustManager>(xtm), SecureRandom())
        return ssl.socketFactory
    }

    private fun createIgnoreHostnameVerifier() = HostnameVerifier { p0, p1 -> true }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (keepCookie) {
            builder.cookieJar(KeepCookieJar())
        }
        if (ignoreSSL) {
            builder.hostnameVerifier(createIgnoreHostnameVerifier())
                    .sslSocketFactory(createIgnoreSSLSocketFactory())
        }
        buildOkhttp?.invoke(builder)
        return builder.build()
    }

    private fun createRetrofit(): Retrofit {
        var builder = Retrofit.Builder()
        baseUrl?.let {
            builder.baseUrl(it)
        }
        builder
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(createOkHttpClient())
        buildRetrofit?.invoke(builder)
        return builder.build()
    }


    fun <T> createService(cla: Class<T>) = createRetrofit().create(cla)
}