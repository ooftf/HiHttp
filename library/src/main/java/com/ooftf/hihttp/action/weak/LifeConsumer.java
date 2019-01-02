package com.ooftf.hihttp.action.weak;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/1 0001
 */
public class LifeConsumer<T> implements Consumer<T> {
    Consumer<T> reference;

    public LifeConsumer(Consumer<T> real,LifecycleOwner owner) {
        reference = real;
        owner.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            void onDestroy() {
                reference = null;
                owner.getLifecycle().removeObserver(this);
            }
        });
    }

    public LifeConsumer(Consumer<T> real,View owner) {
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
    public void accept(T t) throws Exception {
        if (reference == null) {
            return;
        }
        reference.accept(t);
    }
}
