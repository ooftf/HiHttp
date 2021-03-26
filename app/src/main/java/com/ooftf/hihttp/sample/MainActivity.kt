package com.ooftf.hihttp.sample

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.ooftf.hihttp.download.DownLoadEngine
import com.ooftf.hihttp.download.ProgressCallback
import com.ooftf.sample.sample.R
import com.tbruyelle.rxpermissions3.RxPermissions
import okhttp3.Call
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) {
                        sss()
                    }
                }
        //https://cpcti.oss-cn-beijing.aliyuncs.com/apk/IOrderAB.apk
    }

    private fun sss() {
        var start = AtomicInteger(0)
        var success = AtomicInteger(0)
        var error = AtomicInteger(0)
        val build = OkHttpClient.Builder().dispatcher(Dispatcher(ThreadPoolExecutor(1, 1, 100, TimeUnit.SECONDS, LinkedBlockingQueue()))).build()
        for (i in 0 until 100) {
            var file = File(Environment.getExternalStorageDirectory(), "${System.currentTimeMillis() + Random.nextInt()}.apk")
            DownLoadEngine.download(build, "https://cpcti.oss-cn-beijing.aliyuncs.com/apk/IOrderAB.apk", file, object : ProgressCallback.DownLoadProgressListener {
                override fun start(call: Call?, totalByte: Long) {
                    Log.e("start", "${start.incrementAndGet()}")
                }

                override fun progress(call: Call?, bytesRead: Long, contentLength: Long) {
                    //Log.e("task::" + i, "${bytesRead.toDouble() / contentLength}")
                }

                override fun success(call: Call?, file: File?) {
                    Log.e("success", "${success.incrementAndGet()}")

                }

                override fun fail(call: Call?, e: Exception?) {
                    Log.e("error", "${error.incrementAndGet()}")
                    e?.printStackTrace()
                }

            })
        }
    }
}
