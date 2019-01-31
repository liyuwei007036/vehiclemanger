package com.tc5u.vehiclemanger.utils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.UpdateInfo;

import java.io.File;

import core.BaseActivity;

/**
 * 检查更新工具类
 */
public class CheckUpdateUtil {

    private static UpdateInfo updateInfo;
    private static CheckUpdateUtil instance;

    private ProgressDialog progressDialog;

    public static CheckUpdateUtil getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new CheckUpdateUtil();
                }
            }
        }
        return instance;
    }

    public CheckUpdateUtil init(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
        return this;
    }


    /**
     * 检查更新
     *
     * @return
     */
    public Boolean checkUpdate(Context context) {
        if (updateInfo == null) return false;
        // 获取服务器上app版本号
        int server_ver_code = updateInfo.getVersionCode();
        if (server_ver_code == 0) {
            server_ver_code = 1;
        }
        // 获取本地app版本号
        int local_ver_code = SystemUtil.getVersionCode(context);
        if (server_ver_code > local_ver_code) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示更新对话框
     *
     * @param
     */
    public void showUpdateDialog(final Context context) {

        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions((BaseActivity) context, mPermissionList, 100);

        // 获取更新日志
        String update_log = updateInfo.getUpdateContent();
        // 最新版本
        String new_version = updateInfo.getVersionName();
        // 获取下载地址
        final String download_path = updateInfo.getUpdateUrl();

        if (TextUtils.isEmpty(update_log) || TextUtils.isEmpty(download_path) || TextUtils.isEmpty(new_version)) {
            return;
        }

        final AlertDialog mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.show();
        mAlertDialog.setCancelable(false);
        Window window = mAlertDialog.getWindow();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CLIP_VERTICAL);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(context, R.layout.dialog_update, null);
        window.setContentView(view);

        TextView msg_tv = view.findViewById(R.id.msg_tv);

        TextView title = view.findViewById(R.id.title);

        title.setText("发现新版本: V" + new_version);

        msg_tv.setText(update_log);
        Button update = view.findViewById(R.id.yes_btn);
        Button notNow = view.findViewById(R.id.no_btn);

        if (updateInfo.isForced()) {
            notNow.setVisibility(View.GONE);
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p1 = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int p2 = context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (p1 == 0 && p2 == 0) {
                    download(context);
                    mAlertDialog.dismiss();
                } else {
                    ((BaseActivity) context).showToast("权限不足无法下载");
                }
            }
        });
        notNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }

    private void download(final Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载新版本");
        progressDialog.setCancelable(false);//不能手动取消下载进度对话框
        progressDialog.show();
        OkHttpUtils.getInstance().download(updateInfo.getUpdateUrl(), "tc5u_update", new OkHttpUtils.OnDownloadListener() {
            @Override
            public void onDownloadFailed() {
                progressDialog.dismiss();
            }

            @Override
            public void onDownloadSuccess() {
                progressDialog.dismiss();
                installApk(context, Environment.getExternalStorageDirectory() + File.separator + "tc5u_update" + File.separator);
            }

            @Override
            public void onDownloading(int progress) {

            }
        });
    }

    public void installApk(Context context, String apkPath) {
        apkPath += getNameFromUrl(updateInfo.getUpdateUrl());
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }

        File file = new File(apkPath);
        if (!file.exists()) {
            Log.e("UPDATE", "文件不存在");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.tc5u.vehicleManger.fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }


    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


}
