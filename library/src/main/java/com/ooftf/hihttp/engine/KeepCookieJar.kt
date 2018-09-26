package com.ooftf.hihttp.engine

import java.util.ArrayList

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 * 用来做Cookie内存层级的持久化
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
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
