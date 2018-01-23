package com.ooftf.hi.view

import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.ooftf.hi.view.ResponseViewInterface.Companion.STATE_ERROR
import com.ooftf.hi.view.ResponseViewInterface.Companion.STATE_RESPONSE
import com.ooftf.hi.view.ResponseViewInterface.Companion.STATE_START
import com.ooftf.hi.R
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.dialog_response.*
import java.util.*

/**
 * 适合独立的请求防止多次点击，比如点击按钮的请求
 * Created by master on 2017/10/11 0011.
 */
open class ResponseDialog<in T>(var activity: Activity, var text:String = "加载中") : Dialog(activity, R.style.DialogTheme_Empty), ResponseViewInterface<T> {



    /**
     * 为了可是使一个ResponseDialog可以同时处理多个网络请求
     * 添加counter计数器，但只对最后的网络做相应处理
     */
    var counter = 0
    var state = STATE_START

    var progressBar: ProgressBar
    var imageError: ImageView
    init {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_response, null)
        setContentView(view)
        progressBar = view.findViewById(R.id.progressBar)
        imageError = view.findViewById(R.id.imageError)
        setCanceledOnTouchOutside(false)
    }
    override fun onRequest(d: Disposable) {
        counter++
        state = STATE_START
        imageError.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        textView.setText(text)
        setOnDismissListener {
            d.dispose()
        }
        show()
    }

    private val handler: Handler by lazy {
        Handler()
    }

    override fun onError() {
        state = STATE_ERROR
        onComplete()
    }

    override fun onResponse(t: T) {
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
        textView.setText("网络异常")
        handler.postDelayed({
            if (activity.isFinishing) return@postDelayed
            dismiss()
        }, 1000)
    }


}