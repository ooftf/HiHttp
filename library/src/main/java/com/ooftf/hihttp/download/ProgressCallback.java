package com.ooftf.hihttp.download;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/8/23 0023
 */
public class ProgressCallback implements Callback {
    File target;
    DownLoadProgressListener listener;

    public ProgressCallback(File target, DownLoadProgressListener listener) {
        this.target = target;
        this.listener = listener;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        listener.fail(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ProgressResponseBody prb = new ProgressResponseBody(response.body(), (bytesRead, contentLength, done) -> {
            if (done) {
                listener.success(call, target);
            } else {
                listener.progress(call, bytesRead, contentLength);
            }
        });
        listener.start(call, prb.contentLength());

        // 保存文件到本地
        BufferedInputStream bis = null;
        OutputStream outputStream = null;
        byte[] buff = new byte[2048];
        int len;
        try {
            bis = new BufferedInputStream(prb.byteStream());
            outputStream = new FileOutputStream(target);
            // 随机访问文件，可以指定断点续传的起始位置
            while ((len = bis.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
        } catch (Exception e) {
            listener.fail(call, e);
            e.printStackTrace();
        } finally {
            closeSilently(outputStream);
            closeSilently(bis);
        }

    }

    public interface DownLoadProgressListener {
        void start(Call call, long totalByte);

        void progress(Call call, long bytesRead, long contentLength);

        void success(Call call, File file);

        void fail(Call call, Exception e);
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
