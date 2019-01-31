package com.tc5u.vehiclemanger.utils;

import android.content.Context;
import android.view.View;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.customerstyle.AlertDialog;

public class DiaLogUtils {
    public static void alertError(Context context, String title, String msg, final DiaLogResultListener result) {
        new AlertDialog(context).builder().setMsg(msg).setTitle(title)
                .setPositiveButton("确定", context.getResources().getColor(R.color.red), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.success();
                    }
                })
                .show();
    }

    public static void alert(Context context, String title, String msg, final DiaLogResultListener result) {
        new AlertDialog(context).builder().setMsg(msg).setTitle(title)
                .setPositiveButton("确定", context.getResources().getColor(R.color.wxLv), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.success();
                    }
                })
                .setNegativeButton("取消", context.getResources().getColor(R.color.text_color), new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        result.fail();
                    }
                }).show();
    }


    public interface DiaLogResultListener {
        void success();

        void fail();
    }
}
