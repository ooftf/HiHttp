package com.ooftf.hihttp.engine

import java.util.ArrayList

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 * Created by master on 2017/4/24 0024.
 */

class KeepCookieJar : okhttp3.CookieJar {
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        KeepCookieJar.cookies = cookies
    }
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
    }
    companion object {
        var cookies: List<Cookie> = ArrayList()
    }
}
