package com.ooftf.hihttp.upload

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.File
import java.io.IOException
import java.net.URLConnection

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/1/2
 *
 * 每次进度上传固定大小  SEGMENT_SIZE
 */
class FileProgressRequestBody(
        val file: File,
        val contentType: MediaType? = URLConnection.guessContentTypeFromName(file.absolutePath)
                .toMediaTypeOrNull(),
        val listener: (total: Long, size: Long, increment: Long) -> Unit
) : RequestBody() {
    override fun contentLength(): Long {
        return file.length()
    }

    override fun contentType(): MediaType? {
        return contentType
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        // 如果拦截器打印请求消息会导致多次调用writeTo方法，只对真正的网络RealBufferedSink做进度封装
        if (sink.javaClass.simpleName.contains("RealBufferedSink")) {
            var source: Source? = null
            try {
                source = file.source()
                var total: Long = 0
                var read: Long
                while (source.read(sink.buffer, SEGMENT_SIZE.toLong()).also { read = it } != -1L) {
                    total += read
                    sink.flush()
                    listener(contentLength(), total, read)
                }
            } finally {
                source?.closeQuietly()
            }
        } else {
            file.source().use { source -> sink.writeAll(source) }
        }
    }

    companion object {
        const val SEGMENT_SIZE = 512
    }
}

fun File.asFileProgressRequestBody(
        contentType: MediaType? = URLConnection.guessContentTypeFromName(absolutePath)
                .toMediaTypeOrNull(),
        listener: (total: Long, size: Long, increment: Long) -> Unit
): FileProgressRequestBody {
    return FileProgressRequestBody(this, contentType, listener)
}