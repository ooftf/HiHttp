package com.ooftf.hihttp.engine

import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.Request
import okio.Buffer

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/10/14
 */
class RequestWrapper(val request: Request)  {
    fun getHeader(): HashMap<String, String> {
        val header = HashMap<String, String>()
        request.headers.forEach {
            header[it.first] = it.second
        }
        return header
    }

    fun getUrlParams(): MutableMap<String, String> {
        val result = LinkedHashMap<String, String>()
        request.url.queryParameterNames.forEach {
            val value = request.url.queryParameter(it)
            if (value != null) {
                result[it] = value
            }
        }
        return result
    }

    fun getFormBodyParam(): MutableMap<String, String> {
        val formBody = request.body as FormBody
        val result = LinkedHashMap<String, String>()
        for (i in 0 until formBody.size) {
            result[formBody.name(i)] = formBody.value(i)
        }
        return result
    }

    fun isGet(): Boolean {
        return request.method.equals("GET", true)
    }

    fun isFormBody(): Boolean {
        return request.body is FormBody
    }

    fun isJsonBody(): Boolean {
        return request.body?.contentType()?.subtype == "json"
    }

    fun isMultipartBody(): Boolean {
        return request.body is MultipartBody
    }

    fun getJsonBodyString(): String {
        val buffer = Buffer()
        request.body?.writeTo(buffer)
        return buffer.readByteString().utf8()
    }

}