package core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.utils.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //全局设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d(TAG, getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    public void startActivity(JSONObject data, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        if (data != null) {
            Set<String> keys = data.keySet();
            for (String key : keys) {
                intent.putExtra(key, data.getString(key));
            }
        }
        this.startActivity(intent);
    }

    public void save(String file_name, String data) {
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = this.openFileOutput(file_name, Context.MODE_PRIVATE);
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

    public String read(String file_name) {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
        try {
            inputStream = openFileInput(file_name);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
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


    public ProgressDialog loadAlertDialog(String title, String msg) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        return dialog;
    }

    public void showToast(String msg) {
        if (StringUtils.isNotEmpty(msg))
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
