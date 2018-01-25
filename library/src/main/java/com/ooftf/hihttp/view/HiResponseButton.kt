package com.ooftf.hihttp.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import io.reactivex.disposables.Disposable

/**
 * Created by 99474 on 2018/1/25 0025.
 */
class HiResponseButton: Button,HiResponseView<Any> {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onRequest(d: Disposable) {
       // setCompoundDrawables()
    }
    override fun onError(e: Throwable) {

    }
    override fun onResponse(t: Any) {

    }
    override fun onComplete() {

    }


}