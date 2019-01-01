package com.ooftf.hihttp.action.weak;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Action;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/1 0001
 */
public class WeakAction implements Action {
    WeakReference<Action> reference;

    public WeakAction(Action real) {
        reference = new WeakReference<>(real);
    }

    @Override
    public void run() throws Exception {
        Action action = reference.get();
        if (action == null) {
            return;
        }
        action.run();
    }
}
