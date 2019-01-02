package com.ooftf.hihttp.action;

import android.app.ProgressDialog;
import android.support.v4.app.SupportActivity;

import com.ooftf.hihttp.action.weak.LifeAction;
import com.ooftf.hihttp.action.weak.LifeConsumer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * DialogAction
 *
 * @author 99474
 * @date 2018/9/27 0027 0:59
 */
public class DialogAction<T> implements ObservableTransformer<T, T> {
    private SupportActivity activity;
    private ProgressDialog progressDialog;
    private CharSequence message;

    public DialogAction(SupportActivity activity) {
        this.activity = activity;
        message = "正在加载...";
    }

    public DialogAction(SupportActivity activity, CharSequence message) {
        this.activity = activity;
        this.message = message;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new LifeConsumer<>(disposable -> {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage(message);

                    }
                    progressDialog.show();
                }, activity))
                .doOnTerminate(new LifeAction(() -> {
                    if (progressDialog != null) {
                        progressDialog.dismiss();

                    }
                }, activity));
    }
}
