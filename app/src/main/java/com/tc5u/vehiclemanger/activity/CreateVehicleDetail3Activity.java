package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.customerstyle.TextArea;
import com.tc5u.vehiclemanger.utils.DateUtils;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.OptionPickUtils;


import core.BaseActivity;

public class CreateVehicleDetail3Activity extends BaseActivity {

    private Long vid;

    private TextView check_date, environment_date, tci_date, insurance_date;

    private EditText tci_no, insurance_no;

    private Switch warrantyAble, can_mortgaged, available;

    private TextArea sales_remark;

    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle_detail3);
        initToolBar();
        initIntent();
        initView();
        loadVehicleDetailParams();
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

    private void initIntent() {
        vid = getIntent().getLongExtra("vid", 0L);
    }

    private void initView() {
        check_date = findViewById(R.id.check_date);
        environment_date = findViewById(R.id.environment_date);
        tci_date = findViewById(R.id.tci_date);
        insurance_date = findViewById(R.id.insurance_date);
        tci_no = findViewById(R.id.tci_no);
        insurance_no = findViewById(R.id.insurance_no);
        warrantyAble = findViewById(R.id.warrantyAble);
        can_mortgaged = findViewById(R.id.can_mortgaged);
        available = findViewById(R.id.available);
        sales_remark = findViewById(R.id.sales_remark);
    }


    public void choose(View view) {
        switch (view.getId()) {
            case R.id.check_date: {
                OptionPickUtils.getInstance().initTimePicker(this, pvTime, check_date, false, 2000, 0, 1);
                break;
            }
            case R.id.environment_date: {
                OptionPickUtils.getInstance().initTimePicker(this, pvTime, environment_date, false, 2000, 0, 1);
                break;
            }
            case R.id.tci_date: {
                OptionPickUtils.getInstance().initTimePicker(this, pvTime, tci_date, false, 2000, 0, 1);
                break;
            }
            case R.id.insurance_date: {
                OptionPickUtils.getInstance().initTimePicker(this, pvTime, insurance_date, false, 2000, 0, 1);
                break;
            }
        }
    }


    private void loadVehicleDetailParams() {
        JSONObject data = new JSONObject();
        data.put("vid", vid);
        OkHttpUtils.getInstance().PostWithJson("/editVehicleDetail", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                if (res.getInteger("status") == 0) {
                    filter(check_date, res.getString("check_date"));
                    filter(environment_date, res.getString("environment_date"));
                    filter(tci_date, res.getString("tci_date"));
                    filter(insurance_date, res.getString("insurance_date"));
                    insurance_no.setText(res.getString("insurance_no"));
                    tci_no.setText(res.getString("tci_no"));
                    sales_remark.setText(res.getString("sales_remark"));
                    warrantyAble.setChecked(ObjectUtil.getBoolean(res.getBoolean("warrantyAble")));
                    can_mortgaged.setChecked(ObjectUtil.getBoolean(res.getBoolean("can_mortgaged")));
                    available.setChecked(ObjectUtil.getBoolean(res.getBoolean("available")));
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


    private void filter(TextView textView, String s) {
        if (ObjectUtil.getString(s).equals("--年--月")) {
            textView.setText(null);
        } else {
            textView.setText(s);
        }
    }

    private JSONObject getFormData() {
        JSONObject data = new JSONObject();
        data.put("o.id", vid);
        data.put("o.check_date", check_date.getText().toString());
        data.put("o.environment_date", environment_date.getText().toString());
        data.put("o.tci_date", tci_date.getText().toString());
        data.put("o.tci_no", tci_no.getText().toString());
        data.put("o.insurance_date", insurance_date.getText().toString());
        data.put("o.insurance_no", insurance_no.getText().toString());
        data.put("o.sales_remark", sales_remark.getText().toString());
        data.put("o.warrantyAble", warrantyAble.isChecked());
        data.put("o.can_mortgaged", can_mortgaged.isChecked());
        data.put("o.available", available.isChecked());
        return data;
    }

    public void submit(View view) {
        OkHttpUtils.getInstance().PostWithJson("/saveVehicleDetail", getFormData().toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                if (res.getInteger("status") == 0) {
                    DiaLogUtils.alert(CreateVehicleDetail3Activity.this, "保存成功", "是否上传车辆相关图片", new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {
                            Intent intent = new Intent(CreateVehicleDetail3Activity.this, UploadVehiclePhotoActivity.class);
                            intent.putExtra("vid", vid);
                            startActivity(intent);
                        }

                        @Override
                        public void fail() {
                            startActivity(null, IndexActivity.class);
                            finish();
                        }
                    });
                } else {
                    DiaLogUtils.alertError(CreateVehicleDetail3Activity.this, "错误", ObjectUtil.getString(res.getString("msg")), new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {

                        }

                        @Override
                        public void fail() {

                        }
                    });
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

    public void pass(View view) {
        Intent intent = new Intent(CreateVehicleDetail3Activity.this, UploadVehiclePhotoActivity.class);
        intent.putExtra("vid", vid);
        startActivity(intent);
    }
}
