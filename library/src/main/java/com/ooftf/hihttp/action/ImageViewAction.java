package com.ooftf.hihttp.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.ImageView;

import com.ooftf.hihttp.R;
import com.ooftf.hihttp.action.weak.LifeAction;
import com.ooftf.hihttp.action.weak.LifeConsumer;
import com.ooftf.support.MaterialProgressDrawable;

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
public class ImageViewAction<T> implements ObservableTransformer<T, T> {
    private ImageView view;
    private MaterialProgressDrawable progressDrawable;

    public ImageViewAction(ImageView imageView) {
        view = imageView;
        progressDrawable = new MaterialProgressDrawable(view.getContext(), view);
        progressDrawable.setColorSchemeColors(getColorPrimary(view.getContext()));
    }

    Drawable temp;

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new LifeConsumer<>(disposable -> {
                    Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        temp = view.getDrawable();
                        progressDrawable.start();
                        view.setImageDrawable(progressDrawable);
                    });

                }, view))
                .doOnTerminate(new LifeAction(() -> {
                    Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        progressDrawable.stop();
                        view.setImageDrawable(temp);
                    });

                }, view));
    }

    /**
     * 获取主题颜色
     *
     * @return
     */
    public int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }
}
