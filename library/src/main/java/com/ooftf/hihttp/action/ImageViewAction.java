package com.ooftf.hihttp.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.ImageView;

import com.ooftf.hihttp.R;
import com.ooftf.support.MaterialProgressDrawable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

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
        return upstream.doOnSubscribe(disposable -> {
            temp = view.getDrawable();
            progressDrawable.start();
            view.setImageDrawable(progressDrawable);
        }).doOnTerminate(() -> {
            progressDrawable.stop();
            view.setImageDrawable(temp);
        });
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
