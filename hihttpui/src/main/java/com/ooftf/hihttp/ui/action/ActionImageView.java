package com.ooftf.hihttp.ui.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.ooftf.hihttp.ui.R;
import com.ooftf.hihttp.ui.action.weak.LifeAction;
import com.ooftf.hihttp.ui.action.weak.LifeConsumer;
import com.ooftf.support.MaterialProgressDrawable;

import io.reactivex.Completable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

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
                        .doOnSubscribe(new LifeConsumer<>(disposable -> {
                            Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                initial = getDrawable();
                                getProgressDrawable().start();
                                setImageDrawable(getProgressDrawable());
                                setEnabled(false);
                            });
                        },this))
                        .doOnTerminate(new LifeAction(() -> {
                            Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                setImageDrawable(initial);
                                setEnabled(true);
                                getProgressDrawable().stop();
                            });
                        },this));
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
