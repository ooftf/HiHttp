package com.ooftf.hihttp.ui.action.weak;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/1 0001
 */
public class LifeAction implements Action {
    Action reference;

    @SuppressLint("CheckResult")
    public LifeAction(Action real, LifecycleOwner owner) {
        reference = real;
        Completable
                .complete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> owner.getLifecycle().addObserver(new LifecycleObserver() {
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    void onDestroy() {
                        reference = null;
                        owner.getLifecycle().removeObserver(this);
                    }
                }), Throwable::printStackTrace);

    }

    public LifeAction(Action real, View owner) {
        reference = real;
        owner.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                reference = null;
                owner.removeOnAttachStateChangeListener(this);
            }
        });
    }

    @Override
    public void run() throws Exception {
        if (reference == null) {
            return;
        }
        reference.run();
    }
}
