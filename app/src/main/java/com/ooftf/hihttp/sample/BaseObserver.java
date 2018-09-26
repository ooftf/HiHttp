package com.ooftf.hihttp.sample;

import com.ooftf.hihttp.EmptyObserver;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/9/27 0027
 */
public  class BaseObserver<T extends BaseBean> extends EmptyObserver<T> {
    @Override
    public void onNext(T value) {
        if (value.getSuccess()) {
            onSuccess(value);
        }else{
            onFail(value);
        }
    }

    public void onFail(T value){

    }

    /**
     * @param value
     */
    public void onSuccess(T value){

    }
}
