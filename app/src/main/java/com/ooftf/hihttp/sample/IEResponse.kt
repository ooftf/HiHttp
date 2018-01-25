package com.ooftf.hihttp.sample

import com.ooftf.hihttp.view.HiResponseView

/**
 * Created by master on 2017/10/12 0012.
 */
interface IEResponse<in T : BaseBean> : HiResponseView<T> {
    fun onResponseSuccess(bean:T)
    fun onResponseFailOffSiteLogin(bean:T)
    fun onResponseFailSessionOverdue(bean:T)
    fun onResponseFailMessage(bean:T)
}