package com.tc5u.vehiclemanger.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tc5u.vehiclemanger.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class OptionPickUtils<T> {

    private static OptionPickUtils instance;

    public static OptionPickUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OptionPickUtils();
                }
            }
        }
        return instance;
    }

    public OptionsPickerView initOptionPicker(Context context, OptionsPickerView pvOptions, List<T> list, T cur_obj, String title, final OptionPickListener listener) {
        int cur_index = 0;
        int i = list.indexOf(cur_obj);
        if (i > 0) cur_index = i;
        pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.click(options1, options2, options3, v);
            }
        }).setTitleText(title)
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(cur_index)//默认选中项
                .setSubmitText("确定")//确认按钮文字
                .setCancelText("取消")//取消按钮文字
                .setBgColor(Color.WHITE)
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.BLACK)
                .setCancelColor(context.getResources().getColor(R.color.text_color))//取消按钮文字颜色
                .setSubmitColor(context.getResources().getColor(R.color.wxLv))
                .setTextColorCenter(Color.BLACK)
                .setTitleSize(16)
                .setContentTextSize(16)
                .setSubCalSize(16)
                .setContentTextSize(14)
                .setLineSpacingMultiplier(2f)
                .build();
        pvOptions.setPicker(list);
        return pvOptions;
    }


    public void initTimePicker(Context context, TimePickerView pvTime, @NonNull final TextView textView, Boolean endToday, @NonNull int start_year, @NonNull int start_month, @NonNull int start_day) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(start_year, start_month, start_day);
        Calendar endDate = Calendar.getInstance();
        if (!endToday) {
            endDate.add(Calendar.YEAR, 5);
            endDate.set(Calendar.MONTH, 0);
            endDate.set(Calendar.DAY_OF_MONTH, -1);
        }
        Calendar selectedDate = Calendar.getInstance();
        String sel_date = ObjectUtil.getString(textView.getText());
        if (StringUtils.isNotEmpty(sel_date)) {
            selectedDate.setTime(DateUtils.parseDateString(sel_date));
        }

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                textView.setText(DateUtils.dateToStr(date, "yyyy-MM-dd"));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})//默认全部显示
                .setSubmitText("确定")//确认按钮文字
                .setCancelText("取消")//取消按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(context.getResources().getColor(R.color.wxLv))//确定按钮文字颜色
                .setCancelColor(context.getResources().getColor(R.color.text_color))//取消按钮文字颜色
                .setTitleBgColor(context.getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(context.getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .isDialog(false)
                .build();
        pvTime.show();
    }


    public void initOptionPick2(Context context, OptionsPickerView pvOptions, List<T> options1Items, List<List<T>> options2Items, T cur_obj, T cur_obj2, String title, final OptionPickListener listener) {
        int index1 = options1Items.indexOf(cur_obj);
        int index2 = 0;
        if (index1 > 0) {
            List<T> list = options2Items.get(index1);
            index2 = list.indexOf(cur_obj2);
            if (index2 < 0) index2 = 0;
        } else {
            index1 = 0;
        }
        pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.click(options1, options2, options3, v);
            }
        }).setTitleText(title)
                .setContentTextSize(18)//滚轮文字大小
                .setSelectOptions(index1, index2)//默认选中项
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitText("确定")//确认按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubmitColor(context.getResources().getColor(R.color.wxLv))//确定按钮文字颜色
                .setCancelColor(context.getResources().getColor(R.color.text_color))//取消按钮文字颜色
                .setTitleBgColor(context.getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(context.getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                    }
                })
                .build();
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.show();
    }

    public interface OptionPickListener {
        void click(int options1, int options2, int options3, View v);
    }
}
