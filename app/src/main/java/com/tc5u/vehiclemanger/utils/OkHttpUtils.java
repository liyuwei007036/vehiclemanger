package com.tc5u.vehiclemanger.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import core.BaseApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
//    public static final String BASE_URL = "http://192.168.88.39:8080/wxapi/vehiclemanager";
//    public static final String IMG_URL = "http://192.168.88.39:8080";
    private static final String BASE_URL = "https://ei.tc5u.cn/wxapi/vehiclemanager";
    public static final String IMG_URL = "https://ei.tc5u.cn";

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");

    private Context context = BaseApplication.getInstance().getContext();
    /**
     * 网络访问要求singleton
     */
    private static OkHttpUtils instance;

    // 必须要用的okhttpclient实例,在构造器中实例化保证单一实例
    private OkHttpClient mOkHttpClient;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Handler mHandler;

    private BaseApplication baseApplication = BaseApplication.getInstance();

    private OkHttpUtils() {
        /**
         * okHttp3中超时方法移植到Builder中
         */
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new ResponseInterceptor())
                .addInterceptor(new RequestInterceptor())
                .build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 对外提供的Post方法访问
     *
     * @param url
     * @param body:    提交内容为表单数据
     * @param listener
     */
    public void PostWithFormData(String url, FormBody.Builder body, ResultCallBackListener listener) {
        /**
         * 通过url和POST方式构建Request
         */
        Request request = bulidRequestForPostByForm(BASE_URL + url, body);
        /**
         * 请求网络的逻辑
         */
        requestNetWork(url, request, listener);

    }

    /**
     * 对外提供的Post方法访问
     *
     * @param url
     * @param json:    提交内容为json数据
     * @param listener
     */
    public void PostWithJson(String url, String json, ResultCallBackListener listener) {
        /**
         * 通过url和POST方式构建Request
         */
        Request request = bulidRequestForPostByJson(BASE_URL + url, json);
        /**
         * 请求网络的逻辑
         */
        requestNetWork(url, request, listener);

    }

    /**
     * 对外提供的Post方法访问
     *
     * @param url
     * @param filePath 提交内容为文件数据
     * @param listener
     */
    public void PostWithFile(final String url, List<String> filePath, int quality, ResultCallBackListener listener) throws Exception {
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        MediaType type;
        File ysfile = new File(context.getCacheDir().getPath() + "/TC5U-YS-");
        if (!ysfile.exists()) {
            ysfile.mkdir();
        }
        for (String file_path : filePath) {
            if (file_path.endsWith("png")) {
                type = MEDIA_TYPE_PNG;
            } else {
                type = MEDIA_TYPE_JPG;
            }
            File old = new File(file_path);
            if (!old.exists()) {
                throw new Exception("文件不存在");
            }
            File file = ThumbUtils.scalFile(old, context.getCacheDir().getPath() + "/TC5U-YS-" + System.currentTimeMillis() + ".jpg", 100 - quality);
            body.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + file_path + "\""),
                    RequestBody.create(type, file));
        }
        body.build();
        /**
         * 通过url和POST方式构建Request
         */

        Request request = bulidRequestForPostByFile(BASE_URL + url, body);
        /**
         * 请求网络的逻辑
         */
        requestNetWork(url, request, listener);
    }

    /**
     * POST方式构建Request {File}
     *
     * @param url
     * @param body
     * @return
     */
    private Request bulidRequestForPostByFile(String url, MultipartBody.Builder body) {
        RequestBody requestBody = body.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    /**
     * POST方式构建Request {json}
     *
     * @param url
     * @param json
     * @return
     */
    private Request bulidRequestForPostByJson(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    /**
     * POST方式构建Request {Form}
     *
     * @param url
     * @param body
     * @return
     */
    private Request bulidRequestForPostByForm(String url, FormBody.Builder body) {
        RequestBody requestBody = body.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


    private void requestNetWork(final String url, final Request request, final ResultCallBackListener listener) {
        /**
         * 判断网络是否连接
         */
        if (!NetworkUtils.isConnected(baseApplication.getContext())) {
            Toast.makeText(baseApplication.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            listener.noNetWork();
            return;
        }
        /**
         * 加载前
         */
        listener.onLoadingShow();

        /**
         * 处理连网逻辑，此处只处理异步操作enqueue
         */
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //使用handler返回UI线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(url, e);
                        /**
                         * 数据加载后
                         */
                        listener.onLoadingDismiss();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result_str = response.body().string();


                //使用handler返回UI线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            listener.onSuccess(url, result_str);
                        } else {
                            listener.onError(url, result_str);
                        }
                        /**
                         * 数据加载后
                         */
                        listener.onLoadingDismiss();

                    }
                });
            }
        });


    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    private String isExistDir(String saveDir) throws IOException {

        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    public interface OnDownloadListener {
        void onDownloadFailed();

        void onDownloadSuccess();

        void onDownloading(int progress);
    }

    public interface ResultCallBackListener {
        void noNetWork();

        void onLoadingShow();

        void onLoadingDismiss();

        void onSuccess(String url, String result_str);

        void onFailure(String url, Exception e);

        void onError(String url, String result_str);
    }
}

