package com.ooftf.hihttp.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ooftf.hihttp.controller.HiPresenterObserver
import com.ooftf.hihttp.view.HiResponseDialog
import com.ooftf.hihttp.view.HiResponseView
import com.ooftf.sample.sample.R
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        responseLayout.setOnRetryListener {
            ServiceHolder.service
                    .picCaptcha()
                    .bindToLifecycle(responseLayout)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : HiPresenterObserver<PicCaptchaBean>(responseLayout as HiResponseView<PicCaptchaBean>){

                    })
        }
        ServiceHolder.service
                .picCaptcha()
                .bindToLifecycle(responseLayout)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : HiPresenterObserver<PicCaptchaBean>(responseLayout as HiResponseView<PicCaptchaBean>){

                })
        textView.setOnClickListener {
            Toast.makeText(this,javaClass.genericInterfaces.toString(),Toast.LENGTH_SHORT)
            ServiceHolder.service
                    .picCaptcha()
                    //.signIn("4","3","2","1")
                    .bindToLifecycle(window.decorView)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : HiPresenterObserver<PicCaptchaBean>(HiResponseDialog(this)) {

                    })
        }
        commit.setOnClickListener {
            Toast.makeText(this,javaClass.interfaces.toString(),Toast.LENGTH_SHORT)
            ServiceHolder.service
                    .picCaptcha()
                    .bindToLifecycle(commit)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : HiPresenterObserver<PicCaptchaBean>(commit){

                    })
        }
    }
    class MyDispatchObserver(vararg responseView: HiResponseView<*>) : com.ooftf.hihttp.controller.HiPresenterObserver<Nothing>(*responseView)

}
