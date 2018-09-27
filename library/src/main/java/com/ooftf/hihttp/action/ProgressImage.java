package com.ooftf.hihttp.action;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.ooftf.hihttp.R;
import com.ooftf.support.MaterialProgressDrawable;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/9/28 0028
 */
public class ProgressImage extends AppCompatImageView {
    public ProgressImage(Context context) {
        super(context);
    }

    public ProgressImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * val result = MaterialProgressDrawable(context, this)
     * result.setColorSchemeColors(Color.parseColor("#FF0000"))
     * result.start()
     */
    Drawable initial;

    public <T> ObservableTransformer<T, T> getAction() {
        return upstream ->
                upstream.doOnSubscribe(disposable -> {
                    initial = getDrawable();
                    setImageDrawable(getProgressDrawable());
                }).doOnTerminate((Action) () -> {
                    setImageDrawable(initial);
                });
    }

    MaterialProgressDrawable progress;

    Drawable getProgressDrawable() {
        if (progress == null) {
            progress = new MaterialProgressDrawable(getContext(), this);
            progress.setColorSchemeColors(getColorPrimary());
            progress.start();
        }
        return progress;
    }

    /**
     * 获取主题颜色
     *
     * @return
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}
