package com.ooftf.hihttp.engine

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.schedulers.Schedulers
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
open class ServiceGenerator() {
    var baseUrl: String = ""
    var ignoreSSL: Boolean = false
    var loggable:Boolean = false
    var buildOkhttp:((OkHttpClient.Builder)->Unit)?=null
    private val headers: MutableMap<String, String> = HashMap()
    private fun createLogInterceptor(): LoggingInterceptor {
        val response = LoggingInterceptor.Builder()
                .loggable(loggable)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
        headers.forEach {
            response.addHeader(it.key, it.value)
        }
        return response.build()
    }

    private fun createIgnoreSSLSocketFactory(): SSLSocketFactory {
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
        return ssl.socketFactory
    }

    private fun createIgnoreHostnameVerifier() = object : HostnameVerifier {
        override fun verify(p0: String?, p1: SSLSession?): Boolean {
            return true
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .cookieJar(KeepCookieJar())
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
        if (ignoreSSL) {
            builder.hostnameVerifier(createIgnoreHostnameVerifier())
                    .sslSocketFactory(createIgnoreSSLSocketFactory())
        }
        buildOkhttp?.invoke(builder)
        builder.addInterceptor(createLogInterceptor())
        return builder.build()
    }

    private fun createRetrofit() =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(createOkHttpClient())
                    .build()

    fun addHeader(key: String, value: String) {
        headers.put(key, value)
    }
    fun <T> createService(cla: Class<T>) = createRetrofit().create(cla)
}