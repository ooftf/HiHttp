package com.ooftf.hihttp.action;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.ooftf.progress.GradualHorizontalProgressDrawable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/10/8 0008
 */
public class ButtonAction<T> implements ObservableTransformer<T,T> {
    TextView view;
    String progressText = "正在加载...";
    GradualHorizontalProgressDrawable progressDrawable;
    public ButtonAction(TextView view){
        this.view = view;
        progressDrawable = new GradualHorizontalProgressDrawable(view.getContext(),view);
        view.post(() -> progressDrawable.setIntrinsicWidth(view.getMeasuredWidth()));
    }
    public ButtonAction(TextView view,String progressText){
        this(view);
        this.progressText = progressText;

    }
    Drawable[] tempDrawables;
    CharSequence tempText;
    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.doOnSubscribe(disposable -> {
            tempDrawables = view.getCompoundDrawables();
            tempText = view.getText();
            progressDrawable.start();
            view.setCompoundDrawablesWithIntrinsicBounds(tempDrawables[0], tempDrawables[1], tempDrawables[2],progressDrawable);
            view.setText(progressText);
        }).doOnTerminate(() -> {
            view.setText(tempText);
            view.setCompoundDrawablesWithIntrinsicBounds(tempDrawables[0], tempDrawables[1], tempDrawables[2],tempDrawables[3]);
            progressDrawable.stop();
        });
    }
}