package com.ooftf.hihttp.ui.action

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import com.ooftf.hihttp.ui.action.weak.LifeAction
import com.ooftf.hihttp.ui.action.weak.LifeConsumer
import com.ooftf.support.MaterialProgressDrawable
import io.reactivex.Completable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *
 * 一个左侧具有loading进度的button，主要结合RxJava的网络请求一起使用
 * @author ooftf
 * @Email 994749769@qq.com
 * @date 2018/9/27 0027
 */
class ActionButton : Button {
    fun <T> getAction(message: CharSequence = "正在加载..."): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                    .doOnSubscribe(LifeConsumer({
                        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe {
                            initialLeft = compoundDrawables[0]
                            initialText = text.toString()
                            setDrawableLeft(loadingDrawable)
                            text = message
                            isEnabled = false
                        }

                    }, this))
                    .doOnTerminate(LifeAction({
                        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe {
                            isEnabled = true
                            setDrawableLeft(initialLeft)
                            text = initialText
                        }

                    }, this))
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