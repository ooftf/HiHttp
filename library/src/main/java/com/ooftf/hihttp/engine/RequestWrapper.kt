package com.ooftf.hihttp.engine

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import okio.BufferedSink

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/10/14
 */
class RequestWrapper(val request: Request) {
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
        return getFormBodyParam(formBody)
    }

    fun getHttpUrl(): HttpUrl {
        return request.url
    }

    fun getUrl(): String {
        return request.url.toString()
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
        request.body?.let {
            return getJsonBodyString(it)
        }
        return ""
    }


    fun newRequestBuild(): Request.Builder {
        return request.newBuilder()
    }

    fun Request.Builder.setUrlParam(param: Map<String, String>): Request.Builder {
        val newBuilder = request.url.newBuilder()
        request.url.queryParameterNames.forEach {
            newBuilder.removeAllQueryParameters(it)
        }
        param.forEach {
            newBuilder.addQueryParameter(it.key, it.value)
        }
        this.url(newBuilder.build())
        return this
    }

    fun Request.Builder.setFormBody(param: Map<String, String>): Request.Builder {
        this.method(request.method, buildFormBody(param))
        return this
    }


    fun Request.Builder.setHeader(header: Map<String, String>): Request.Builder {
        header.forEach {
            this.header(it.key, it.value)
        }
        return this
    }


    fun Request.Builder.setJsonBody(jsonBody: String): Request.Builder {
        this.method(request.method, buildJsonBody(jsonBody))
        return this
    }

    companion object {
        fun buildJsonBody(jsonBody: String): RequestBody {
            return object : RequestBody() {
                override fun contentType(): MediaType? {
                    return "application/json;charset=utf-8".toMediaTypeOrNull()
                }

                override fun writeTo(sink: BufferedSink) {
                    sink.buffer.writeUtf8(jsonBody)
                }
            }
        }

        fun buildFormBody(param: Map<String, String>): FormBody {
            val builder = FormBody.Builder()
            param.forEach {
                builder.add(it.key, it.value)
            }
            val newRequestBody = builder.build()
            return newRequestBody
        }

        fun getFormBodyParam(formBody: FormBody): MutableMap<String, String> {
            val result = LinkedHashMap<String, String>()
            for (i in 0 until formBody.size) {
                result[formBody.name(i)] = formBody.value(i)
            }
            return result
        }

        fun getJsonBodyString(body: RequestBody): String {
            val buffer = Buffer()
            body.writeTo(buffer)
            return buffer.readByteString().utf8()
        }
    }
}