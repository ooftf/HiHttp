package com.ooftf.hihttp.engine

import android.text.TextUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


/**
 *
 * 配合retrofit生成service
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
 */
open class ServiceGenerator {
    var baseUrl: String = ""
    var ignoreSSL: Boolean = false
    var loggable: Boolean = false
    var buildOkhttp: ((OkHttpClient.Builder) -> Unit)? = null
    private val headers: MutableMap<String, String> = HashMap()
    private fun createLogInterceptor(): Interceptor {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("OkHttp")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            if (TextUtils.isEmpty(it)) {
                return@Logger
            }
            try {
                JSONObject(it)
                Logger.json(it)
            } catch (e: Exception) {
                Logger.d(it)
            }
        })
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

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

    private fun createIgnoreHostnameVerifier() = object : HostnameVerifier {
        override fun verify(p0: String?, p1: SSLSession?): Boolean {
            return true
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .cookieJar(KeepCookieJar())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
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
        headers[key] = value
    }

    fun <T> createService(cla: Class<T>) = createRetrofit().create(cla)
}