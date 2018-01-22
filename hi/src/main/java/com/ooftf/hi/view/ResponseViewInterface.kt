package com.ooftf.hi.view

import io.reactivex.disposables.Disposable

/**
 * Created by master on 2017/10/11 0011.
 */
interface ResponseViewInterface<T> {
    fun onRequest(d: Disposable)
    fun onError()
    fun onResponse(t:T)
    fun onComplete()
    companion object {
        var STATE_START = 0
        var STATE_ERROR = 1
        var STATE_RESPONSE = 2
    }
}