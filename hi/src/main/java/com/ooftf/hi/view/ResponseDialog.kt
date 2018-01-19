package com.ooftf.hi.view

import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.ooftf.hi.view.ResponseViewInterface.Companion.STATE_ERROR
import com.ooftf.hi.view.ResponseViewInterface.Companion.STATE_RESPONSE
import com.ooftf.hi.view.ResponseViewInterface.Companion.STATE_START
import com.ooftf.hi.R

/**
 * 适合独立的请求防止多次点击，比如点击按钮的请求
 * Created by master on 2017/10/11 0011.
 */
open class ResponseDialog : Dialog, ResponseViewInterface {


    var activity: Activity
    /**
     * 为了可是使一个ResponseDialog可以同时处理多个网络请求
     * 添加counter计数器，但只对最后的网络做相应处理
     */
    var counter = 0
    var state = STATE_START

    constructor(activity: Activity) : super(activity, R.style.DialogTheme_Empty) {
        this.activity = activity
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_response, null)
        setContentView(view)
        progressBar = view.findViewById(R.id.progressBar)
        imageError = view.findViewById(R.id.imageError)
    }

    var progressBar: ProgressBar
    var imageError: ImageView
    override fun onStart() {
        counter++
        state = STATE_START
        imageError.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        show()
    }

    private val handler: Handler by lazy {
        Handler()
    }

    override fun onError() {
        state = STATE_ERROR

    }

    override fun onResponse() {
        state = STATE_RESPONSE
        dismiss()
    }

    override fun onComplete() {
        counter--
        if (counter > 0) return
        when (state) {
            STATE_RESPONSE -> dismiss()
            STATE_ERROR -> {
                errorEnd()
            }
        }
    }

    private fun errorEnd() {
        imageError.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        handler.postDelayed({
            if (activity.isFinishing) return@postDelayed
            dismiss()
        }, 1000)
    }
}