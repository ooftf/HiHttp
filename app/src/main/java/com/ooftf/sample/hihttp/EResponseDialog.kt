package com.ooftf.sample.hihttp
import android.app.Activity
import android.support.v7.app.AlertDialog
import com.ooftf.hihttp.view.HiResponseDialog
/**
 * Created by master on 2017/10/12 0012.
 */
class EResponseDialog(activity: Activity,text:String="加载中"): HiResponseDialog<BaseBean>(activity,text),IEResponse<BaseBean> {

    override fun onResponseSuccess(bean: BaseBean) {

    }

    override fun onResponseFailOffSiteLogin(bean: BaseBean) {
        showToMainDialog(bean)
    }

    override fun onResponseFailSessionOverdue(bean: BaseBean) {
        showToMainDialog(bean)
    }

    override fun onResponseFailMessage(bean: BaseBean) {
        AlertDialog
                .Builder(activity)
                .setMessage(bean.info)
                .setPositiveButton("确定", { dialog, _ -> dialog.dismiss() })
                .show()
    }

    private fun showToMainDialog(bean: BaseBean) {
       /* AlertDialog
                .Builder(activity)
                .setMessage(bean.info)
                .setNeutralButton("返回首页", { _, _ -> activity.startActivity(MainActivity.getStartIntent(activity)) })
                .setCancelable(false)
                .show()*/
    }

}