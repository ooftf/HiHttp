package com.ooftf.hihttp.action.weak;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/1 0001
 */
public class WeakConsumer<T> implements Consumer<T> {
    WeakReference<Consumer<T>> reference;

    public WeakConsumer(Consumer<T> real) {
        reference = new WeakReference<>(real);
    }

    @Override
    public void accept(T t) throws Exception {
        Consumer<T> consumer = reference.get();
        if (consumer == null) {
            return;
        }
        consumer.accept(t);
    }
}
