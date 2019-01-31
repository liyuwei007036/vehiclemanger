package com.tc5u.vehiclemanger.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.ProvinceBean;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.OptionPickUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

/**
 * 车架价格评估
 */
public class VehicleEvaluatePriceActivity extends BaseActivity {

    private List<ProvinceBean> options1Items = new ArrayList<>();

    private List<List<ProvinceBean>> options2Items = new ArrayList<>();

    private TextView brand, series, model, registerDate, cur_city, distance;

    private String brand_name, series_name, model_name;

    private Long brand_id, series_id, model_id;

    private TimePickerView pvTime;

    private ProgressDialog dialog;

    private ProvinceBean province, city;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dialog.dismiss();
                    break;
                case 2: {
                    dialog.dismiss();
                    showToast("数据加载失败");
                    break;
                }
            }
        }
    };
    private OptionsPickerView<ProvinceBean> pvOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_evaluate_price);
        initView();
        initToolBar();
        getProvinces();
        dialog = loadAlertDialog(null, "数据初始化中");
        dialog.show();
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
        brand = findViewById(R.id.brand);
        series = findViewById(R.id.series);
        model = findViewById(R.id.model);
        registerDate = findViewById(R.id.registerDate);
        cur_city = findViewById(R.id.city);
        distance = findViewById(R.id.distance);
    }

    public void choose(View view) {
        switch (view.getId()) {
            case R.id.brand: {
                Intent data = new Intent(this, EveChooseBrandActivity.class);
                data.putExtra("brand_name", brand_name);
                startActivityForResult(data, 3);
                break;
            }
            case R.id.series: {
                if (ObjectUtil.getLong(brand_id) > 0) {
                    Intent intent = new Intent(this, EveChooseSeriesActivity.class);
                    intent.putExtra("brand_id", brand_id);
                    intent.putExtra("brand_name", brand_name);
                    startActivityForResult(intent, 4);
                } else {
                    DiaLogUtils.alertError(this, "错误", "请先选择品牌", new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {

                        }

                        @Override
                        public void fail() {

                        }
                    });
                }
                break;
            }
            case R.id.model: {
                if (ObjectUtil.getLong(series_id) < 1) {
                    DiaLogUtils.alertError(this, "错误", "请先选择车系", new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {

                        }

                        @Override
                        public void fail() {

                        }
                    });
                } else {
                    Intent intent = new Intent(this, EveChooseModelActivity.class);
                    intent.putExtra("brand_id", brand_id);
                    intent.putExtra("brand_name", brand_name);
                    intent.putExtra("series_id", series_id);
                    intent.putExtra("series_name", series_name);
                    startActivityForResult(intent, 5);
                }
                break;
            }
            case R.id.registerDate: {
                OptionPickUtils.getInstance().initTimePicker(this, pvTime, registerDate, true, 2000, 0, 1);
                break;
            }
            case R.id.city: {
                OptionPickUtils.getInstance().initOptionPick2(this, pvOptions, options1Items, options2Items, province, city, "城市选择", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        province = options1Items.get(options1);
                        city = options2Items.get(options1).get(options2);
                        cur_city.setText(city.getName());
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == 9) {
            Long new_brand_id = data.getLongExtra("brand_id", 0L);
            if (brand_id != new_brand_id) {
                resetSeries();
                resetModel();
            }
            brand_id = new_brand_id;
            brand_name = data.getStringExtra("brand_name");
            brand.setText(brand_name);
            Intent intent = new Intent(this, EveChooseSeriesActivity.class);
            intent.putExtra("brand_id", brand_id);
            intent.putExtra("brand_name", brand_name);
            startActivityForResult(intent, 4);
        }

        if (requestCode == 4 && resultCode == 10) {
            Long new_series_id = data.getLongExtra("series_id", 0L);
            series_name = data.getStringExtra("series_name");
            if (series_id != new_series_id) {
                resetModel();
            }
            series_id = new_series_id;
            series.setText(series_name);
            Intent intent = new Intent(this, EveChooseModelActivity.class);
            intent.putExtra("brand_id", brand_id);
            intent.putExtra("brand_name", brand_name);
            intent.putExtra("series_id", series_id);
            intent.putExtra("series_name", series_name);
            startActivityForResult(intent, 5);
        }

        if (requestCode == 5 && resultCode == 11) {
            model_id = data.getLongExtra("model_id", 0L);
            model_name = data.getStringExtra("model_name");
            model.setText(model_name);
        }
    }

    private void resetSeries() {
        series_id = 0L;
        series_name = "";
        series.setText(null);
    }

    private void resetModel() {
        model_id = 0L;
        model_name = "";
        model.setText(null);
    }

    private void getProvinces() {
        OkHttpUtils.getInstance().PostWithJson("/getProvincesForAPP", "", new OkHttpUtils.ResultCallBackListener() {
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
                    JSONArray province = res.getJSONArray("data");
                    for (int i = 0; i < province.size(); i++) {
                        JSONObject pro = province.getJSONObject(i);
                        ProvinceBean bean = new ProvinceBean(pro.getString("cityName"), pro.getLong("cityId"));
                        options1Items.add(bean);
                        JSONArray cit_arr = pro.getJSONArray("citys");
                        List<ProvinceBean> beans = new ArrayList<>();
                        for (int j = 0; j < cit_arr.size(); j++) {
                            beans.add(new ProvinceBean(cit_arr.getJSONObject(j)));
                        }
                        options2Items.add(beans);
                    }
                    mHandler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onError(String url, String result_str) {
                mHandler.sendEmptyMessage(2);
            }
        });
    }

    public void submit(View view) {
        Intent data = getData();
        if (data != null) {
            startActivity(data);
        }
    }

    private Intent getData() {
        try {
            Intent object = new Intent(this, VehicleEvaluatePriceResultActivity.class);
            if (StringUtils.isEmpty(brand_name)) throw new Exception("请先选择品牌");
            if (ObjectUtil.getLong(brand_id) < 0) throw new Exception("请先选择品牌");
            object.putExtra("brandName", brand_name);
            object.putExtra("brand_id", brand_id);

            if (StringUtils.isEmpty(series_name)) throw new Exception("请先选择车系");
            if (ObjectUtil.getLong(series_id) < 0) throw new Exception("请先选择车系");
            object.putExtra("series_name", series_name);
            object.putExtra("series_id", series_id);

            if (StringUtils.isEmpty(model_name)) throw new Exception("请先选择车型");
            if (ObjectUtil.getLong(model_id) < 0) throw new Exception("请先选择车型");
            object.putExtra("model_name", model_name);
            object.putExtra("model_id", model_id);

            if (StringUtils.isEmpty(registerDate.getText().toString())) {
                throw new Exception("请先选择上牌时间");
            }

            object.putExtra("registerDate", registerDate.getText().toString());

            if (province == null || city == null) throw new Exception("请先选择城市");
            object.putExtra("provinceId", province.getId());
            object.putExtra("cityId", city.getId());
            object.putExtra("city_name", city.getName());
            object.putExtra("province_name", province.getName());


            if (ObjectUtil.getDouble(distance.getText()) <= 0) {
                throw new Exception("请先填写里程");
            }
            object.putExtra("distance", ObjectUtil.getDouble(distance.getText()));
            return object;
        } catch (Exception e) {
            DiaLogUtils.alertError(this, "错误", e.getMessage(), new DiaLogUtils.DiaLogResultListener() {
                @Override
                public void success() {

                }

                @Override
                public void fail() {

                }
            });
            return null;
        }
    }
}