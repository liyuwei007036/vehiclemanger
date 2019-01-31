package com.tc5u.vehiclemanger.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import core.BaseApplication;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {


    public RequestInterceptor() {
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        BaseApplication application = BaseApplication.getInstance();
        final Request.Builder builder = chain.request().newBuilder();
        builder.removeHeader("User-Agent").addHeader("User-Agent", SystemUtil.getUserAgent(application.getContext()));
        String cookie = read(application.getContext(), "cookies");
        if (StringUtils.isNotEmpty(cookie)) builder.addHeader("Cookie", cookie);
        return chain.proceed(builder.build());
    }

    public String read(Context context, String file_name) {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
        try {
            inputStream = context.openFileInput(file_name);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (FileNotFoundException e) {
            Log.i("COOKIE", "COOKIE  文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }
    }
}