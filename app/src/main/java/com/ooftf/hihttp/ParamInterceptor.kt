package com.ooftf.hihttp

import com.ooftf.hi.engine.FormParamInterceptor

/**
 * Created by master on 2017/3/7.
 */

class ParamInterceptor : FormParamInterceptor() {
    override fun paramTransform(oldParams: MutableMap<String, String>): MutableMap<String, String> {
        oldParams.put("terminalType", "3")
        oldParams.put("appVersion", "2.5.1")
        return oldParams
    }
}