package com.ooftf.hihttp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ooftf.hi.controller.PresenterObserver
import com.ooftf.hi.view.ResponseDialog
import com.ooftf.hi.view.ResponseViewInterface
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        responseLayout.setOnRetryListener {
            ServiceHolder
                    .service
                    .picCaptcha()
                    .bindToLifecycle(responseLayout)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : PresenterObserver<PicCaptchaBean>(responseLayout as ResponseViewInterface<PicCaptchaBean>){

                    })
        }
        ServiceHolder
                .service
                .picCaptcha()
                .bindToLifecycle(responseLayout)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PresenterObserver<PicCaptchaBean>(responseLayout as ResponseViewInterface<PicCaptchaBean>){

                })
        textView.setOnClickListener {
            ServiceHolder
                    .service
                    .picCaptcha()
                    //.signIn("4","3","2","1")
                    .bindToLifecycle(window.decorView)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : PresenterObserver<PicCaptchaBean>(ResponseDialog(this)) {

                    })
        }
    }
    class MyDispatchObserver(vararg responseView: ResponseViewInterface<*>) : com.ooftf.hi.controller.PresenterObserver<Nothing>(*responseView)

}
