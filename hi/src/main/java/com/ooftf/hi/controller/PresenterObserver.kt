package com.ooftf.hi.controller
import com.ooftf.hi.view.ResponseViewInterface
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
/**
 * Created by master on 2017/8/18 0018.
 */
abstract class PresenterObserver<T>(vararg view: ResponseViewInterface<T>) : Observer<T> {
    private var views = view
    override fun onSubscribe(d: Disposable) {
        views.forEach { it.onRequest(d) }
    }
    override fun onError(e: Throwable) {
        views.forEach { it.onError(e) }
    }

    override fun onNext(value: T) {
        views.forEach { it.onResponse(value) }
    }
    override fun onComplete(){
        views.forEach { it.onComplete() }
    }
}