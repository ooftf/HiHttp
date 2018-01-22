package com.ooftf.hi.view

/**
 * Created by master on 2017/10/11 0011.
 */
interface ResponseViewInterface {
    fun onRequest()
    fun onError()
    fun onResponse()
    fun onComplete()
    companion object {
        var STATE_START = 0
        var STATE_ERROR = 1
        var STATE_RESPONSE = 2
    }
}