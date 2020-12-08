package com.ooftf.hihttp.upload

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.buffer
import java.io.File
import java.io.IOException
import java.net.URLConnection

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/1/2
 *
 * 对RequestBody进行一次进度包装（推荐）
 */
class ProgressRequestBody(
        val requestBody: RequestBody,
        val listener: (total: Long, size: Long, increment: Long) -> Unit
) :
        RequestBody() {

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    var currentLength = 0

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        // 如果拦截器打印请求消息会导致多次调用writeTo方法，只对真正的网络RealBufferedSink做进度封装
        if (sink.javaClass.simpleName.contains("RealBufferedSink")) {
            currentLength = 0
            val forwardingSink: ForwardingSink = object : ForwardingSink(sink) {
                @Throws(IOException::class)
                override fun write(source: Buffer, byteCount: Long) {
                    currentLength += byteCount.toInt()
                    super.write(source, byteCount)
                    listener(contentLength(), currentLength.toLong(), byteCount)
                }
            }
            val bufferedSink = forwardingSink.buffer()
            requestBody.writeTo(bufferedSink)
            bufferedSink.flush()
        } else {
            requestBody.writeTo(sink)
        }
    }
}


fun File.asProgressRequestBody(
        mediaType: MediaType? = URLConnection.guessContentTypeFromName(absolutePath)
                .toMediaTypeOrNull(), listener: (total: Long, size: Long, increment: Long) -> Unit
): ProgressRequestBody {
    return ProgressRequestBody(
            asRequestBody(mediaType),
            listener
    )
}