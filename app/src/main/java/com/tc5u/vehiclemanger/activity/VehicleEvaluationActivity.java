package com.tc5u.vehiclemanger.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.customerstyle.AlertDialog;
import com.tc5u.vehiclemanger.model.SpinnerModel;
import com.tc5u.vehiclemanger.utils.DateUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import core.BaseActivity;

public class VehicleEvaluationActivity extends BaseActivity {

    private Long vehicle_id;

    private EditText vin, color, distance, owner_name, owner_phone;

    private TextView owner_type, register_date, check_date, department, tci_date;

    private List<Object> models;

    private TimePickerView pvTime;

    private OptionsPickerView pvOptions;

    private SpinnerModel cur_dep;

    private List<Object> customer_type = Arrays.asList(new Object[]{"个人", "商户", "自营", "公司", "经纪人（非商户）", "未知"});

    private Long sheet_id, owner_id;

    private Boolean disabled;

    private Button save, draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_evaluation);
        getDepartments();
        initIntent();
        initToolBar();
        initView();

    }

    private void initIntent() {
        vehicle_id = ObjectUtil.getLong(getIntent().getStringExtra("vehicle_id"));
    }

    public void initToolBar() {
        Toolbar mToolbarTb = findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        vin = findViewById(R.id.vin);
        color = findViewById(R.id.color);
        distance = findViewById(R.id.distance);
        owner_name = findViewById(R.id.owner_name);
        owner_phone = findViewById(R.id.owner_phone);
        owner_type = findViewById(R.id.owner_type);
        register_date = findViewById(R.id.register_date);
        check_date = findViewById(R.id.check_date);
        tci_date = findViewById(R.id.tci_date);
        department = findViewById(R.id.dep);
        save = findViewById(R.id.save);
        draw = findViewById(R.id.draw);
    }

    public void choosePicker(View view) {
        if (disabled) return;
        switch (view.getId()) {
            case R.id.dep: {
                pvOptions = initOptionPicker(models, "DEP");
                pvOptions.show();
                break;
            }
            case R.id.owner_type: {
                pvOptions = initOptionPicker(customer_type, "OWNER");
                pvOptions.show();
                break;
            }
            case R.id.register_date: {
                initTimePicker(register_date, true);
                break;
            }
            case R.id.check_date: {
                initTimePicker(check_date, false);
                break;
            }
            case R.id.tci_date: {
                initTimePicker(tci_date, false);
                break;
            }
        }
    }

    private void getDepartments() {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        okHttpUtils.PostWithJson("/getDepartments", "", new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {

            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONArray object = JSONArray.parseArray(result_str);
                models = new ArrayList<>();
                for (int i = 0; i < object.size(); i++) {
                    models.add(new SpinnerModel(object.getJSONObject(i)));
                }
                getEvaluationSheet();
            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }

    private void getEvaluationSheet() {
        JSONObject data = new JSONObject();
        data.put("vehicle_id", vehicle_id);
        OkHttpUtils.getInstance().PostWithJson("/getEvaluationSheet", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {

            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {

            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (res.getBoolean("valid")) {
                    initTextValue(res);
                } else {
                    showToast("系统错误");
                }

            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }

    private void initTextValue(JSONObject res) {
        res = res.getJSONObject("sheet");

        sheet_id = res.getLong("id");

        owner_type.setText(res.getString("owner_type"));

        color.setText(res.getString("color"));

        distance.setText(res.getString("distance"));

        owner_id = ObjectUtil.getLong(res.getLong("owner_id"));

        owner_phone.setText(res.getString("owner_phone"));

        vin.setText(res.getString("vin"));

        owner_name.setText(res.getString("owner_name"));

        String dep_id = ObjectUtil.getString(res.getString("department_id"));

        if (models != null) {
            for (Object o : models) {
                SpinnerModel model = (SpinnerModel) o;
                if (ObjectUtil.getString(model.getValue()).equals(dep_id)) {
                    cur_dep = model;
                    department.setText(model.getName());
                }
            }
        } else {

        }

        if (ObjectUtil.getString(res.getString("register_date")).length() > 10) {
            String date = ObjectUtil.getString(res.getString("register_date")).substring(0, 10);
            register_date.setText(date);
        }

        if (ObjectUtil.getString(res.getString("check_date")).length() > 10) {
            String date = ObjectUtil.getString(res.getString("check_date")).substring(0, 10);
            check_date.setText(date);
        }

        if (ObjectUtil.getString(res.getString("tci_date")).length() > 10) {
            String date = ObjectUtil.getString(res.getString("tci_date")).substring(0, 10);
            tci_date.setText(date);
        }

        disabled = "处理中".equals(res.getString("status"));
        editTextDisable();
    }

    private void editTextDisable() {
        if (disabled) {
            vin.setEnabled(false);
            color.setEnabled(false);
            distance.setEnabled(false);
            owner_name.setEnabled(false);
            owner_phone.setEnabled(false);
            draw.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            register_date.setEnabled(false);
            register_date.setTextColor(getResources().getColor(R.color.alertdialog_line));
            owner_type.setEnabled(false);
            owner_type.setTextColor(getResources().getColor(R.color.alertdialog_line));
            check_date.setEnabled(false);
            check_date.setTextColor(getResources().getColor(R.color.alertdialog_line));
            tci_date.setEnabled(false);
            tci_date.setTextColor(getResources().getColor(R.color.alertdialog_line));
            department.setEnabled(false);
            department.setTextColor(getResources().getColor(R.color.alertdialog_line));
        } else {
            vin.setEnabled(true);
            color.setEnabled(true);
            distance.setEnabled(true);
            owner_name.setEnabled(true);
            owner_phone.setEnabled(true);
            draw.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            register_date.setEnabled(true);
            register_date.setTextColor(getResources().getColor(R.color.black));
            owner_type.setEnabled(true);
            owner_type.setTextColor(getResources().getColor(R.color.black));
            check_date.setEnabled(true);
            check_date.setTextColor(getResources().getColor(R.color.black));
            tci_date.setEnabled(true);
            tci_date.setTextColor(getResources().getColor(R.color.black));
            department.setEnabled(true);
            department.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void initTimePicker(final TextView textView, Boolean endToday) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2008, 0, 1);
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

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
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
                .setSubmitColor(getResources().getColor(R.color.wxLv))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.text_color))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .isDialog(false)
                .build();
        pvTime.show();
    }

    private OptionsPickerView initOptionPicker(final List<Object> list, final String type) {
        int cur_index = 0;
        String title = "";
        if (type.equals("OWNER")) {
            String owner = ObjectUtil.getString(owner_type.getText());
            if (owner != null) cur_index = list.indexOf(owner);
            title = "车主类型";
        } else if (type.equals("DEP")) {
            if (cur_dep != null) cur_index = list.indexOf(cur_dep);
            title = "部门选择";
        }

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (type.equals("DEP")) {
                    SpinnerModel model = (SpinnerModel) list.get(options1);
                    department.setText(model.getName());
                    cur_dep = model;
                } else if (type.equals("OWNER")) {
                    owner_type.setText(ObjectUtil.getString(list.get(options1)));
                }
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
                .setCancelColor(getResources().getColor(R.color.text_color))//取消按钮文字颜色
                .setSubmitColor(getResources().getColor(R.color.wxLv))
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

    private JSONObject getFormData() {
        JSONObject data = new JSONObject();
        data.put("sheet_id", sheet_id);
        data.put("vin", ObjectUtil.getString(vin.getText()));
        data.put("owner_id", owner_id);
        data.put("owner_name", ObjectUtil.getString(owner_name.getText()));
        data.put("owner_phone", ObjectUtil.getString(owner_phone.getText()));
        data.put("color", ObjectUtil.getString(color.getText()));
        data.put("distance", ObjectUtil.getDouble(distance.getText()));
        data.put("register_date", ObjectUtil.getString(register_date.getText()));
        data.put("check_date", ObjectUtil.getString(check_date.getText()));
        data.put("tci_date", ObjectUtil.getString(tci_date.getText()));
        data.put("vehicle_id", vehicle_id);
        data.put("department_id", ObjectUtil.getLong(cur_dep.getValue()));
        data.put("owner_type", ObjectUtil.getString(owner_type.getText()));
        return data;
    }

    public void saveApplySheet(View view) {
        OkHttpUtils.getInstance().PostWithJson("/saveApplySheet", getFormData().toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {

            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {

            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject object = JSONObject.parseObject(result_str);
                final Boolean valid = object.getBoolean("valid");
                if (valid) {
                    sheet_id = object.getLong("sheet_id");
                    new AlertDialog(VehicleEvaluationActivity.this).builder().setMsg("保存成功是否提交")
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.wxLv), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    doSubmitApply();
                                }
                            })
                            .setNegativeButton("取消", getResources().getColor(R.color.text_color), new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    finish();
                                }
                            }).show();
                } else {
                    new AlertDialog(VehicleEvaluationActivity.this).builder().setMsg("提交失败:" + ObjectUtil.getString(object.getString("message")))
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.red), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }

            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }

    private void doSubmitApply() {
        JSONObject data = new JSONObject();
        data.put("sheet_id", sheet_id);
        OkHttpUtils.getInstance().PostWithJson("/doSubmitApply", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {

            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {

            }

            @Override
            public void onSuccess(String url, String result_str) {

                JSONObject object = JSONObject.parseObject(result_str);
                if (object.getBoolean("valid")) {
                    new AlertDialog(VehicleEvaluationActivity.this).builder().setMsg(object.getString("message"))
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.wxLv), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();

                } else {
                    new AlertDialog(VehicleEvaluationActivity.this).builder().setMsg(object.getString("message"))
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.red), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }

            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }

    public void doWithdrawApply(View view) {
        JSONObject data = new JSONObject();
        data.put("sheet_id", sheet_id);
        OkHttpUtils.getInstance().PostWithJson("/doWithdrawApply", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {


            @Override
            public void noNetWork() {

            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {

            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject object = JSONObject.parseObject(result_str);
                if (object.getBoolean("valid")) {
                    disabled = false;
                    editTextDisable();
                    new AlertDialog(VehicleEvaluationActivity.this).builder().setMsg("撤回成功")
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.wxLv), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                } else {
                    new AlertDialog(VehicleEvaluationActivity.this).builder().setMsg(object.getString("message"))
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.red), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }


}
