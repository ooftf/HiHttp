package com.ooftf.hi.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ooftf.hi.R
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_error.view.*
import kotlinx.android.synthetic.main.layout_start.view.*

/**
 * 适合只加载一次的页面，比如进入activity就要加载数据，并且每次数据都是独立的。不适合多次加载的列表页面
 *
 * Created by master on 2017/10/11 0011.
 */
open class ResponseLayout<T> : FrameLayout, ResponseViewInterface<T> {
    var counter = 0
    var state = ResponseViewInterface.STATE_START
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    lateinit var inflater: LayoutInflater
    var success: View? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        success = getChildAt(0)
        inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_start, this)
        inflater.inflate(R.layout.layout_error, this)
        invisibleAll()
    }

    override fun onRequest(d: Disposable) {
        counter++
        state = ResponseViewInterface.STATE_START
        invisibleAll()
        start_container.visibility = View.VISIBLE
    }

    override fun onError() {
        state = ResponseViewInterface.STATE_ERROR
        onComplete()
    }

    override fun onResponse(t: T) {
        state = ResponseViewInterface.STATE_RESPONSE
    }

    private fun invisibleAll() {
        (0 until childCount).forEach {
            getChildAt(it).visibility = View.INVISIBLE
        }
    }

    override fun onComplete() {
        counter--
        if (counter > 0) return
        when (state) {
            ResponseViewInterface.STATE_RESPONSE -> {
                invisibleAll()
                success?.visibility = View.VISIBLE
            }
            ResponseViewInterface.STATE_ERROR -> {
                invisibleAll()
                error_container.visibility = View.VISIBLE
            }
        }
    }

    fun setOnRetryListener(listener: () -> Unit) {
        retry.setOnClickListener {
            listener()
        }
    }
}