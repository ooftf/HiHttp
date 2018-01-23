package com.ooftf.hihttp

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by 99474 on 2018/1/23 0023.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupStetho()
    }
    private fun setupStetho(){
        Stetho.initializeWithDefaults(this);
    }
}