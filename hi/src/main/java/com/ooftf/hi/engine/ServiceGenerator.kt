package com.ooftf.hi.engine

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.ooftf.hi.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


/**
 * Created by master on 2017/8/15 0015.
 */
open class ServiceGenerator(private var baseUrl: String, var ignoreSSL: Boolean = false, headers: MutableMap<String, String>, vararg interceptor: Interceptor) {
    private val logInterceptor: LoggingInterceptor by lazy {
        val response = LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
        headers.forEach {
            response.addHeader(it.key, it.value)
        }
        response.build()
    }
    private val okHttpClient: OkHttpClient by lazy {
        var builder = OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .cookieJar(KeepCookieJar())
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
        if (ignoreSSL) {
            builder.hostnameVerifier(ignoreHostnameVerifier)
                    .sslSocketFactory(ignoreSSLSocketFactory)
        }
        interceptor.forEach {
            builder.addInterceptor(it)
        }
        builder.build()
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(okHttpClient)
                .build()
    }
    private val ignoreSSLSocketFactory: SSLSocketFactory by lazy {
        val ssl = SSLContext.getInstance("SSL")
        val xtm = object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf<X509Certificate>()
            }
        }
        ssl.init(null, arrayOf<TrustManager>(xtm), SecureRandom())
        ssl.socketFactory
    }
    private val ignoreHostnameVerifier: HostnameVerifier by lazy {
        object : HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                return true
            }
        }
    }

    fun <T> createService(cla: Class<T>) = retrofit.create(cla)
}