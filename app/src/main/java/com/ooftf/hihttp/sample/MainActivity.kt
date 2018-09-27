package com.ooftf.hihttp.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ooftf.hihttp.action.DialogAction
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
                    .compose(responseLayout.getAction())
                    .subscribe(object : BaseObserver<PicCaptchaBean>() {
                        override fun onSuccess(value: PicCaptchaBean?) {

                        }
                    })
        }
        ServiceHolder.service
                .picCaptcha()
                .bindToLifecycle(responseLayout)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(responseLayout.getAction())
                .subscribe(object : BaseObserver<PicCaptchaBean>() {
                    override fun onSuccess(value: PicCaptchaBean?) {

                    }
                })
        progressImage.setOnClickListener {
            ServiceHolder.service
                    .picCaptcha()
                    //.signIn("4","3","2","1")
                    .bindToLifecycle(window.decorView)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(progressImage.getAction())
                    .subscribe(object : BaseObserver<PicCaptchaBean>() {
                        override fun onSuccess(value: PicCaptchaBean?) {

                        }
                    })
        }
        textView.setOnClickListener {
            Toast.makeText(this,javaClass.genericInterfaces.toString(),Toast.LENGTH_SHORT).show()
            ServiceHolder.service
                    .picCaptcha()
                    //.signIn("4","3","2","1")
                    .bindToLifecycle(window.decorView)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(DialogAction(this))
                    .subscribe(object : BaseObserver<PicCaptchaBean>() {
                        override fun onSuccess(value: PicCaptchaBean?) {

                        }
                    })
        }
        commit.setOnClickListener {
            Toast.makeText(this,javaClass.interfaces.toString(),Toast.LENGTH_SHORT)
            ServiceHolder.service
                    .picCaptcha()
                    .bindToLifecycle(commit)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(commit.getAction())
                    .subscribe(object : BaseObserver<PicCaptchaBean>() {
                        override fun onSuccess(value: PicCaptchaBean?) {

                        }
                    })
        }
    }
}
