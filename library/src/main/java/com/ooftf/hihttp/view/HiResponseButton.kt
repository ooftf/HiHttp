package com.ooftf.hihttp.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import com.ooftf.support.MaterialProgressDrawable
import io.reactivex.disposables.Disposable

/**
 * Created by 99474 on 2018/1/25 0025.
 */
class HiResponseButton : Button, HiResponseView<Any> {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val loadingDrawable: Drawable by lazy {
        var result = MaterialProgressDrawable(context, this)
        result.setColorSchemeColors(Color.parseColor("#FF0000"))
        result.start()
        result
    }
    var initialLeft: Drawable?=null
    lateinit var initialText:String
    override fun onRequest(d: Disposable) {
        initialLeft = compoundDrawables[0]
        initialText = text.toString()
        setDrawableLeft(loadingDrawable)
        text = "加载中..."
        isEnabled = false
    }

    override fun onError(e: Throwable) {
        isEnabled = true
        setDrawableLeft(initialLeft)
        text = initialText
    }

    override fun onResponse(t: Any) {
        isEnabled = true
        setDrawableLeft(initialLeft)
        text = initialText
    }

    override fun onComplete() {

    }
    fun setDrawableLeft(left:Drawable?){
        setCompoundDrawablesWithIntrinsicBounds(left, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }

}