package com.ooftf.hihttp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ooftf.hi.controller.DispatchObserver
import com.ooftf.hi.view.ResponseDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.setOnClickListener {
            ServiceHolder
                    .service
                    .picCaptcha()
                    //.signIn("4","3","2","1")
                    //.bindToLifecycle(window.decorView)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : DispatchObserver<BaseBean>(ResponseDialog(this)) {

                    })
        }
    }
}
