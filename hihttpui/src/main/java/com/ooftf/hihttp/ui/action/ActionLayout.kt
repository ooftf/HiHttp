package com.ooftf.hihttp.ui.action

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ooftf.hihttp.ui.R
import com.ooftf.hihttp.ui.action.weak.LifeAction
import com.ooftf.hihttp.ui.action.weak.LifeConsumer
import io.reactivex.Completable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.layout_error.view.*
import kotlinx.android.synthetic.main.layout_start.view.*

/**
 * 适合只加载一次的页面，比如进入activity就要加载数据，并且每次数据都是独立的。不适合多次加载的列表页面
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
 */
open class ActionLayout : FrameLayout {

    fun <T> getAction(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                    .doOnSubscribe(LifeConsumer({
                        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe {
                            toLoadingView()
                        }

                    }, this))
                    .doOnError(LifeConsumer({
                        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe {
                            toErrorView()
                        }

                    }, this))
                    .doOnComplete(LifeAction({
                        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe {
                            toSuccessView()
                        }
                    }, this))
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    lateinit var inflater: LayoutInflater
    var success: View? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            success = getChildAt(0)
        }
        inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_start, this)
        inflater.inflate(R.layout.layout_error, this)
        toSuccessView()
    }

    private fun invisibleAll() {
        (0 until childCount).forEach {
            getChildAt(it).visibility = View.INVISIBLE
        }
    }

    fun setOnRetryListener(listener: () -> Unit) {
        retry.setOnClickListener {
            listener()
        }
    }

    fun toErrorView() {
        invisibleAll()
        error_container.visibility = View.VISIBLE
    }

    fun toLoadingView() {
        invisibleAll()
        start_container.visibility = View.VISIBLE
    }

    fun toSuccessView() {
        invisibleAll()
        success?.visibility = View.VISIBLE
    }
}