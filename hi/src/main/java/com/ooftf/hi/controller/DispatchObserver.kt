package com.ooftf.hi.controller
import com.ooftf.hi.view.ResponseViewInterface
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
/**
 * Created by master on 2017/8/18 0018.
 */
abstract class DispatchObserver<T>(private var responseView: ResponseViewInterface?) : Observer<T> {
    override fun onSubscribe(d: Disposable) {
        responseView?.onStart()
    }
    override fun onError(e: Throwable) {
        responseView?.onError()
    }

    override fun onNext(value: T) {
        responseView?.onResponse()
    }
    override fun onComplete(){
        responseView?.onComplete()
    }
}