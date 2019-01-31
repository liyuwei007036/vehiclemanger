package com.tc5u.vehiclemanger.utils;

import android.content.Context;
import android.util.TypedValue;

import core.BaseApplication;


public class UnitUtils {

    public static Context context = BaseApplication.getInstance().getContext();

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static float px2sp(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size, context.getResources().getDisplayMetrics());
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int dip2px(float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static float sp2px(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, context.getResources().getDisplayMetrics());
    }
}
