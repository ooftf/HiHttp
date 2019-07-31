package com.ooftf.hihttp.ui;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/9/27 0027
 */
public class EmptyObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
