package com.ooftf.hihttp.action

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import com.ooftf.support.MaterialProgressDrawable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable

/**
 * Created by 99474 on 2018/1/25 0025.
 */
class ActionButton : Button {

    fun <T> getAction(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                    .doOnSubscribe {
                        initialLeft = compoundDrawables[0]
                        initialText = text.toString()
                        setDrawableLeft(loadingDrawable)
                        text = "加载中..."
                        isEnabled = false
                    }
                    .doOnTerminate {
                        isEnabled = true
                        setDrawableLeft(initialLeft)
                        text = initialText
                    }
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val loadingDrawable: Drawable by lazy {
        val result = MaterialProgressDrawable(context, this)
        result.setColorSchemeColors(Color.parseColor("#FF0000"))
        result.start()
        result
    }
    private var initialLeft: Drawable? = null
    private lateinit var initialText: String

    private fun setDrawableLeft(left: Drawable?) {
        setCompoundDrawablesWithIntrinsicBounds(left, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }

}