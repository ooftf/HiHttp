package com.ooftf.hihttp.action;

import android.app.Activity;
import android.app.ProgressDialog;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
/**
 *
 * DialogAction
 * @author 99474
 * @date 2018/9/27 0027 0:59
 */
public class DialogAction<T> implements ObservableTransformer<T, T> {
    private Activity activity;
    private ProgressDialog progressDialog;
    private CharSequence message;
    public DialogAction(Activity activity) {
        this.activity = activity;
        message = "正在加载...";
    }
    public DialogAction(Activity activity, CharSequence message) {
        this.activity = activity;
        this.message = message;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(disposable -> {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage(message);

                    }
                    progressDialog.show();
                })
                .doOnTerminate(() -> {
                    if (progressDialog != null) {
                        progressDialog.dismiss();

                    }
                });
    }
}
