package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.Vehicle;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import core.BaseActivity;

public class CreateVehicleActivity extends BaseActivity {

    private Vehicle vehicle = Vehicle.searchVehicle();

    private TextView outlet, brand, series, model, body_type;

    private EditText other_series, other_model, volume;

    private Switch is_other_model, turbo;

    private Long outlet_id, brand_id, series_id, model_id;

    private String outlet_name, brand_name, series_name, model_name, body_type_text;

    private OptionsPickerView<String> pvOptions;

    private List<String> body_types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);
        initToolBar();
        initView();
        setDataToView();
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

    @Override
    protected void onResume() {
        super.onResume();
        vehicle = Vehicle.searchVehicle();
    }

    private void initView() {
        outlet = findViewById(R.id.outlet);
        brand = findViewById(R.id.brand);
        model = findViewById(R.id.model);
        series = findViewById(R.id.series);
        other_series = findViewById(R.id.other_series);
        other_model = findViewById(R.id.other_v_model);
        is_other_model = findViewById(R.id.other_model);
        turbo = findViewById(R.id.turbo);
        body_type = findViewById(R.id.bodyType);
        volume = findViewById(R.id.volume);
    }

    public void changeSeries(View view) {
        if (is_other_model.isChecked()) {
            other_model.setVisibility(View.VISIBLE);
            other_series.setVisibility(View.VISIBLE);
            model.setVisibility(View.GONE);
            series.setVisibility(View.GONE);
        } else {
            other_model.setVisibility(View.GONE);
            other_series.setVisibility(View.GONE);
            model.setVisibility(View.VISIBLE);
            series.setVisibility(View.VISIBLE);
        }
    }

    public void choose(View view) {
        switch (view.getId()) {
            case R.id.outlet: {
                Intent data = new Intent(CreateVehicleActivity.this, ChangeOutletActivity.class);
                data.putExtra("outlet", vehicle.getOutlet_name());
                data.putExtra("city", vehicle.getCity());
                data.putExtra("is_all", true);
                startActivityForResult(data, 2);
                break;
            }
            case R.id.brand: {
                Intent data = new Intent(CreateVehicleActivity.this, ChooseBrandActivity.class);
                data.putExtra("brand_name", brand_name);
                startActivityForResult(data, 3);
                break;
            }
            case R.id.series: {
                if (ObjectUtil.getLong(brand_id) > 0) {
                    Intent intent = new Intent(this, ChooseSeriesActivity.class);
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
                    Intent intent = new Intent(this, ChooseModelActivity.class);
                    intent.putExtra("brand_id", brand_id);
                    intent.putExtra("brand_name", brand_name);
                    intent.putExtra("series_id", series_id);
                    intent.putExtra("series_name", series_name);
                    startActivityForResult(intent, 5);
                }
                break;
            }
            case R.id.bodyType: {
                body_types = Arrays.asList(Vehicle.BODY_LIST);
                pvOptions = initOptionPicker(body_types);
                pvOptions.show();
                break;
            }
        }
    }

    private void setDataToView() {
        brand_id = vehicle.getBrand_id();
        brand_name = vehicle.getBrand_name();

        series_id = vehicle.getSeries_id();
        series_name = vehicle.getSeries_name();

        model_id = vehicle.getModel_id();
        model_name = vehicle.getModel_name();

        outlet_id = vehicle.getOutlet_id();
        outlet_name = vehicle.getOutlet_name();

        body_type_text = vehicle.getBody_type();

        outlet.setText(vehicle.getOutlet_name());

        if (StringUtils.isNotEmpty(vehicle.getBrand_name())) brand.setText(vehicle.getBrand_name());
        is_other_model.setChecked(ObjectUtil.getBoolean(vehicle.getOther_model()));
        if (StringUtils.isNotEmpty(vehicle.getSeries_name())) {
            series.setText(vehicle.getSeries_name());
            other_series.setText(vehicle.getSeries_name());
        }

        if (StringUtils.isNotEmpty(vehicle.getModel_name())) {
            model.setText(vehicle.getModel_name());
            other_model.setText(vehicle.getModel_name());
        }

        if (StringUtils.isNotEmpty(vehicle.getBody_type())) {
            body_type.setText(vehicle.getBody_type());
        }

        turbo.setChecked(ObjectUtil.getBoolean(vehicle.getTurbo()));

        if (ObjectUtil.getDouble(vehicle.getVolume()) > 0) {
            volume.setText(vehicle.getVolume().toString());
        }


        if (is_other_model.isChecked()) {
            other_model.setVisibility(View.VISIBLE);
            other_series.setVisibility(View.VISIBLE);
            model.setVisibility(View.GONE);
            series.setVisibility(View.GONE);
        } else {
            other_model.setVisibility(View.GONE);
            other_series.setVisibility(View.GONE);
            model.setVisibility(View.VISIBLE);
            series.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择完卖场
        if (requestCode == 2 && resultCode == 8) {
            outlet_id = data.getLongExtra("outlet_id", 0L);
            outlet_name = data.getStringExtra("outlet_name");
            outlet.setText(outlet_name);
        }

        if (requestCode == 3 && resultCode == 9) {
            Long new_brand_id = data.getLongExtra("brand_id", 0L);
            if (brand_id != new_brand_id) {
                resetSeries();
                resetModel();
            }
            brand_id = new_brand_id;
            brand_name = data.getStringExtra("brand_name");
            brand.setText(brand_name);
            if (!is_other_model.isChecked()) {
                Intent intent = new Intent(this, ChooseSeriesActivity.class);
                intent.putExtra("brand_id", brand_id);
                intent.putExtra("brand_name", brand_name);
                startActivityForResult(intent, 4);
            }
        }

        if (requestCode == 4 && resultCode == 10) {
            Long new_series_id = data.getLongExtra("series_id", 0L);
            series_name = data.getStringExtra("series_name");
            if (series_id != new_series_id) {
                resetModel();
            }
            series_id = new_series_id;
            series.setText(series_name);
            other_series.setText(series_name);
            Intent intent = new Intent(this, ChooseModelActivity.class);
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
            other_model.setText(model_name);
        }
    }

    private void resetSeries() {
        series_id = 0L;
        series_name = "";
        series.setText(null);
        other_series.setText(null);
    }

    private void resetModel() {
        model_id = 0L;
        model_name = "";
        model.setText(null);
        other_model.setText(null);
    }

    private OptionsPickerView initOptionPicker(final List<String> list) {
        int cur_index = 0;

        if (StringUtils.isNotEmpty(body_type_text)) {
            cur_index = list.indexOf(body_type_text);
        }

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                body_type.setText(body_types.get(options1));
                body_type_text = body_types.get(options1);
            }
        }).setTitleText("车身级别")
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

    public void setDataToCache(View view) {
        if (is_other_model.isChecked()) {
            series_name = other_series.getText().toString();
            model_name = other_model.getText().toString();
        }
        vehicle.setOutlet_id(outlet_id);
        vehicle.setOutlet_name(outlet_name);
        vehicle.setBrand_id(brand_id);
        vehicle.setBrand_name(brand_name);
        vehicle.setOther_model(is_other_model.isChecked());
        vehicle.setSeries_id(series_id);
        vehicle.setSeries_name(series_name);
        vehicle.setModel_id(model_id);
        vehicle.setModel_name(model_name);
        vehicle.setBody_type(body_type_text);
        vehicle.setTurbo(turbo.isChecked());
        vehicle.setVolume(ObjectUtil.getDouble(volume.getText()));
        vehicle.save();
        startActivity(null, CreateVehicleDetailActivity.class);
    }

    public void finish(View view) {
        finish();
    }
}
