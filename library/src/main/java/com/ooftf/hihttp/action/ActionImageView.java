package com.ooftf.hihttp.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.ooftf.hihttp.R;
import com.ooftf.hihttp.action.weak.WeakAction;
import com.ooftf.hihttp.action.weak.WeakConsumer;
import com.ooftf.support.MaterialProgressDrawable;

import io.reactivex.ObservableTransformer;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/9/28 0028
 */
public class ActionImageView extends AppCompatImageView {
    public ActionImageView(Context context) {
        super(context);
    }

    public ActionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                upstream
                        .doOnSubscribe(new WeakConsumer<>(disposable -> {
                            initial = getDrawable();
                            getProgressDrawable().start();
                            setImageDrawable(getProgressDrawable());
                            setEnabled(false);
                        }))
                        .doOnTerminate(new WeakAction(() -> {
                            setImageDrawable(initial);
                            setEnabled(true);
                            getProgressDrawable().stop();
                        }));
    }

    MaterialProgressDrawable progress;

    MaterialProgressDrawable getProgressDrawable() {
        if (progress == null) {
            progress = new MaterialProgressDrawable(getContext(), this);
            progress.setColorSchemeColors(getColorPrimary());
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
