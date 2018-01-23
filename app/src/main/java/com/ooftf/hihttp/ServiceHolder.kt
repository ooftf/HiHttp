package com.ooftf.hihttp

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ooftf.hi.engine.ServiceGenerator


/**
 * Created by master on 2017/8/15 0015.
 */
object ServiceHolder {
    val service: IEService by lazy {
        val generator = ServiceGenerator()
        generator.baseUrl = "https://api.etongdai.com/"
        generator.ignoreSSL = true
        generator.loggable = true
        generator.buildOkhttp = {
            it.addNetworkInterceptor(StethoInterceptor())
        }
        generator.createService(IEService::class.java)
    }
}