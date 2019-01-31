package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.customerstyle.TextArea;
import com.tc5u.vehiclemanger.model.Vehicle;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import core.BaseActivity;

/**
 * 客户新建编辑
 */
public class EditCustomerActivity extends BaseActivity {

    private EditText customer_phone, qq, wechat, customer_name, owner_price;

    private TextView geren, shanghu, man, woman, dealer;

    private TextArea remark;

    private Long vid, owner_id, dealer_id;

    private Boolean sex;

    private String customer_type, dealer_name;

    private LinearLayout chooseVehicleDealer, chooseVehicleDealerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        initIntent();
        initToolBar();
        initView();
        loadParams();
    }

    public void initToolBar() {
        Toolbar mToolbarTb = findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(null, IndexActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(null, IndexActivity.class);
    }

    private void initIntent() {
        vid = getIntent().getLongExtra("vid", 0L);
    }

    private void initView() {
        customer_phone = findViewById(R.id.customer_phone);
        qq = findViewById(R.id.qq);
        wechat = findViewById(R.id.wechat);
        customer_name = findViewById(R.id.customer_name);
        owner_price = findViewById(R.id.owner_price);
        geren = findViewById(R.id.geren);
        shanghu = findViewById(R.id.shanghu);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        remark = findViewById(R.id.remark);
        chooseVehicleDealerTitle = findViewById(R.id.chooseVehicleDealerTitle);
        chooseVehicleDealer = findViewById(R.id.chooseVehicleDealer);
        dealer = findViewById(R.id.dealer);
        customer_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                if (value.length() == 11) {
                    getCustomer(value);
                } else {
                    clearView();
                }

            }
        });
    }

    public void check(View view) {
        Drawable drawable = getResources().getDrawable(R.drawable.check);
        drawable.setBounds(0, 0, 28, 28);
        switch (view.getId()) {
            case R.id.man: {
                sex = true;
                man.setCompoundDrawables(null, null, drawable, null);
                woman.setCompoundDrawables(null, null, null, null);
                break;
            }
            case R.id.woman: {
                sex = false;
                woman.setCompoundDrawables(null, null, drawable, null);
                man.setCompoundDrawables(null, null, null, null);
                break;
            }
            case R.id.geren: {
                customer_type = Vehicle.OWNER_PERSONAL;
                geren.setCompoundDrawables(null, null, drawable, null);
                shanghu.setCompoundDrawables(null, null, null, null);
                hideDealerView();
                break;
            }
            case R.id.shanghu: {
                customer_type = Vehicle.OWNER_VENDOR;
                shanghu.setCompoundDrawables(null, null, drawable, null);
                geren.setCompoundDrawables(null, null, null, null);
                hideDealerView();
                break;
            }
        }
    }

    public void chooseVehicleDealer(View view) {
        Intent intent = new Intent(EditCustomerActivity.this, ChooseDealerActivity.class);
        intent.putExtra("dealer_name", dealer_name);
        intent.putExtra("dealer_id", dealer_id);
        startActivityForResult(intent, 3);
    }

    private void hideDealerView() {
        if (ObjectUtil.getString(customer_type).equals(Vehicle.OWNER_VENDOR)) {
            chooseVehicleDealer.setVisibility(View.VISIBLE);
            chooseVehicleDealerTitle.setVisibility(View.VISIBLE);
        } else {
            chooseVehicleDealer.setVisibility(View.GONE);
            chooseVehicleDealerTitle.setVisibility(View.GONE);
        }
    }

    private void getCustomer(String phone) {
        JSONObject data = new JSONObject();
        data.put("phone", phone);
        OkHttpUtils.getInstance().PostWithJson("/getCustomer", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                    setDataToView(res, true);
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

    private void loadParams() {
        JSONObject data = new JSONObject();
        data.put("vid", vid);
        OkHttpUtils.getInstance().PostWithJson("/editCustomer", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                    setDataToView(res, false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 11) {
            dealer_id = data.getLongExtra("dealer_id", 0L);
            dealer_name = data.getStringExtra("dealer_name");
            dealer.setText(dealer_name);
        }
    }

    private void clearView() {
        qq.setText(null);
        wechat.setText(null);
        customer_name.setText(null);
        customer_type = null;
        owner_id = null;
        owner_id = null;
        sex = null;
        wechat.setText(null);
        changeView();
    }

    private void setDataToView(JSONObject object, Boolean listener) {
        if (!listener) {
            customer_phone.setText(object.getString("customer_phone"));
            if (ObjectUtil.getDouble(object.getString("owner_price")) > 0) {
                owner_price.setText(object.getString("owner_price"));
            } else {
                owner_price.setText(null);
            }
            remark.setText(object.getString("remark"));
        }
        qq.setText(object.getString("qq"));
        wechat.setText(object.getString("wechat"));
        customer_name.setText(object.getString("customer_name"));
        customer_type = object.getString("customerType");
        owner_id = object.getLong("c_id");
        if (ObjectUtil.getString(object.getString("sex")).equals("男")) {
            sex = true;
        } else if (ObjectUtil.getString(object.getString("sex")).equals("女")) {
            sex = false;
        }

        dealer_id = object.getLong("dealer_id");

        dealer_name = object.getString("dealer_name");
        dealer.setText(dealer_name);
        changeView();
    }

    private void changeView() {
        hideDealerView();
        Drawable drawable = getResources().getDrawable(R.drawable.check);
        drawable.setBounds(0, 0, 28, 28);
        if (ObjectUtil.getString(customer_type).equals(Vehicle.OWNER_VENDOR)) {
            shanghu.setCompoundDrawables(null, null, drawable, null);
            geren.setCompoundDrawables(null, null, null, null);
        } else if (ObjectUtil.getString(customer_type).equals(Vehicle.OWNER_PERSONAL)) {
            geren.setCompoundDrawables(null, null, drawable, null);
            shanghu.setCompoundDrawables(null, null, null, null);
        } else {
            geren.setCompoundDrawables(null, null, null, null);
            shanghu.setCompoundDrawables(null, null, null, null);
        }

        if (sex != null) {
            if (sex) {
                man.setCompoundDrawables(null, null, drawable, null);
                woman.setCompoundDrawables(null, null, null, null);
            } else {
                woman.setCompoundDrawables(null, null, drawable, null);
                man.setCompoundDrawables(null, null, null, null);
            }
        } else {
            man.setCompoundDrawables(null, null, null, null);
            woman.setCompoundDrawables(null, null, null, null);
        }
    }

    private JSONObject getFormData() {
        JSONObject data = new JSONObject();
        data.put("v.id", vid);
        data.put("c.id", owner_id);
        data.put("c.mobile", ObjectUtil.getString(customer_phone.getText()));
        data.put("c.name", ObjectUtil.getString(customer_name.getText()));
        data.put("c.customer_type", customer_type);
        data.put("c.dealer_id", dealer_id);
        data.put("c.qq", ObjectUtil.getString(qq.getText()));
        data.put("c.wx_account", ObjectUtil.getString(wechat.getText()));
        data.put("v.owner_price", ObjectUtil.getDouble(owner_price.getText()));
        data.put("o.owner_remark", ObjectUtil.getString(remark.getText()));
        data.put("c.sex", sex);
        return data;
    }


    public void submit(View view) {
        OkHttpUtils.getInstance().PostWithJson("/saveCustomer", getFormData().toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                    DiaLogUtils.alert(EditCustomerActivity.this, "保存成功", "是否继续补充车辆其他信息", new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {
                            Intent intent = new Intent(EditCustomerActivity.this, CreateVehicleDetail3Activity.class);
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
                    DiaLogUtils.alertError(EditCustomerActivity.this, "错误", ObjectUtil.getString(res.getString("msg")), new DiaLogUtils.DiaLogResultListener() {
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
        Intent intent = new Intent(EditCustomerActivity.this, CreateVehicleDetail3Activity.class);
        intent.putExtra("vid", vid);
        startActivity(intent);
    }

}
