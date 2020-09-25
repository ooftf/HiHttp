package com.ooftf.hihttp.engine

import okhttp3.OkHttpClient
import retrofit2.Retrofit


/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/7/31 0031
 */
class ServiceGeneratorBuilder {
    private var baseUrl: String? = null
    private var ignoreSSL: Boolean = false
    private var keepCookie: Boolean = false
    private var buildOkHttp: ((OkHttpClient.Builder) -> Unit)? = null
    private var buildRetrofit: ((Retrofit.Builder) -> Unit)? = null
    fun setBaseUrl(baseUrl: String): ServiceGeneratorBuilder {
        this.baseUrl = baseUrl
        return this
    }

    fun setIgnoreSSL(ignoreSSL: Boolean): ServiceGeneratorBuilder {
        this.ignoreSSL = ignoreSSL
        return this
    }

    fun setKeepCookie(keepCookie: Boolean): ServiceGeneratorBuilder {
        this.keepCookie = keepCookie
        return this
    }

    fun setBuildOkHttp(buildOkHttp: ((OkHttpClient.Builder) -> Unit)): ServiceGeneratorBuilder {
        this.buildOkHttp = buildOkHttp
        return this
    }

    fun setBuildRetrofit(buildRetrofit: ((Retrofit.Builder) -> Unit)): ServiceGeneratorBuilder {
        this.buildRetrofit = buildRetrofit
        return this
    }

    fun build(): ServiceGenerator {
        return ServiceGenerator(baseUrl, ignoreSSL, keepCookie, buildOkHttp, buildRetrofit)
    }
}
