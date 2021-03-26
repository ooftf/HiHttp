package com.ooftf.hihttp.download;

import java.io.File;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 9/4/2019
 * 回调是在子进程中
 */
public class DownLoadEngine {
    public static Call download(String url, File file, ProgressCallback.DownLoadProgressListener listener) {
        Request request = new Request.Builder().url(url).get().build();
        Call call = new OkHttpClient.Builder().build().newCall(request);
        call.enqueue(new ProgressCallback(file, listener));
        return call;
    }

    public static Call download(OkHttpClient okHttpClient,String url, File file, ProgressCallback.DownLoadProgressListener listener) {
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new ProgressCallback(file, listener));
        return call;
    }
}
