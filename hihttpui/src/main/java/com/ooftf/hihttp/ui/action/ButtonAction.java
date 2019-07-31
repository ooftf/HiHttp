package com.ooftf.hihttp.ui.action;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.ooftf.hihttp.ui.action.weak.LifeAction;
import com.ooftf.hihttp.ui.action.weak.LifeConsumer;
import com.ooftf.progress.GradualHorizontalProgressDrawable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/10/8 0008
 */
public class ButtonAction<T> implements ObservableTransformer<T, T> {
    TextView view;
    String progressText = "正在加载...";
    GradualHorizontalProgressDrawable progressDrawable;

    public ButtonAction(TextView view) {
        this.view = view;
        progressDrawable = new GradualHorizontalProgressDrawable(view.getContext(), view);
        progressDrawable.setIntrinsicWidth(view.getWidth() - view.getPaddingLeft() - view.getPaddingRight());
    }

    public ButtonAction(TextView view, String progressText) {
        this(view);
        this.progressText = progressText;

    }

    Drawable[] tempDrawables;
    CharSequence tempText;

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new LifeConsumer<>(disposable -> {
                    Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        tempDrawables = view.getCompoundDrawables();
                        tempText = view.getText();
                        progressDrawable.start();
                        view.setCompoundDrawablesWithIntrinsicBounds(tempDrawables[0], tempDrawables[1], tempDrawables[2], progressDrawable);
                        view.setText(progressText);
                        view.setEnabled(false);
                    });

                },view))
                .doOnTerminate(new LifeAction(() -> {
                    Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        view.setText(tempText);
                        view.setCompoundDrawablesWithIntrinsicBounds(tempDrawables[0], tempDrawables[1], tempDrawables[2], tempDrawables[3]);
                        progressDrawable.stop();
                        view.setEnabled(true);
                    });
                },view));
    }
}
