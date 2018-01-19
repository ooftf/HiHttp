package com.dks.master.masterretrofit.View

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ooftf.hi.R
import kotlinx.android.synthetic.main.layout_error.view.*
import kotlinx.android.synthetic.main.layout_start.view.*

/**
 * 适合只加载一次的页面，比如进入activity就要加载数据，并且每次数据都是独立的。不适合多次加载的列表页面
 *
 * Created by master on 2017/10/11 0011.
 */
open class ResponseLayout : FrameLayout, ResponseViewInterface {

    var counter = 0
    var state = ResponseViewInterface.STATE_START
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
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

    override fun onStart() {
        counter++
        state = ResponseViewInterface.STATE_START
        invisibleAll()
        start_container.visibility = View.VISIBLE
    }

    override fun onError() {
        state = ResponseViewInterface.STATE_ERROR
    }

    override fun onResponse() {
        state = ResponseViewInterface.STATE_RESPONSE
    }

    private fun invisibleAll() {
        (0..childCount - 1).forEach {
            getChildAt(it).visibility = View.INVISIBLE
        }
    }

    override fun onComplete() {
        counter--
        if (counter > 0) return
        when (state) {
            ResponseViewInterface.STATE_ERROR -> {
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