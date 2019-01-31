package com.tc5u.vehiclemanger.utils;

import android.content.Context;
import android.content.Intent;

import com.tc5u.vehiclemanger.activity.LoginActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import core.ActivityController;
import core.BaseActivity;
import core.BaseApplication;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ResponseInterceptor implements Interceptor {

    public ResponseInterceptor() {
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        BaseApplication application = BaseApplication.getInstance();

        Response response = chain.proceed(chain.request());
        List<String> cookie = response.headers("Set-Cookie");
        if (cookie != null && cookie.size() > 0) {
            BaseApplication.getInstance().setCookies(cookie.get(0));
            save(application.getContext(), "cookies", cookie.get(0));
        }

        if (response.code() == 401) {
            Intent intent = new Intent(application.getContext(), LoginActivity.class);
            intent.putExtra("msg", "登陆超时，请重新登陆");
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            application.getContext().startActivity(intent);
        }
        return response;
    }


    public void save(Context context, String file_name, String data) {
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = context.openFileOutput(file_name, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}