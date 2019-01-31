package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.Vehicle;
import com.tc5u.vehiclemanger.model.VehicleColor;
import com.tc5u.vehiclemanger.utils.DateUtils;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.OptionPickUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import org.litepal.LitePal;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import core.BaseActivity;

public class VehiclePreviewActivity extends BaseActivity {

    private Vehicle vehicle;

    private Long vid;

    private TextView outlet, brand, series, model, body_type, color, gearbox, emission, status, source, vehicleType, registerDate, factoryDate;

    private EditText other_series, other_model, volume, distance, new_price, sell_price, engine_no, licence_plate;

    private Switch is_other_model, turbo;

    private Long outlet_id, brand_id, series_id, model_id, color_id = 0L;

    private String outlet_name, brand_name, series_name, model_name;

    private OptionsPickerView<Object> pvOptions;

    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_preview);
        initView();
        initIntent();
        initToolBar();
    }

    private void initIntent() {
        vid = getIntent().getLongExtra("vid", 0L);
        Boolean edit = getIntent().getBooleanExtra("edit", false);
        if (edit) {
            Button back = findViewById(R.id.back);
            back.setVisibility(View.GONE);
            loadVehicleParameter();
        } else {
            vehicle = Vehicle.searchVehicle();
            setDataToView();
        }
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

    private void loadVehicleParameter() {
        JSONObject data = new JSONObject();
        data.put("v_id", vid);
        OkHttpUtils.getInstance().PostWithJson("/loadVehicleParameterV1", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                if (res.getBoolean("err") != null && !res.getBoolean("err")) {
                    Vehicle new_vehicle = new Vehicle();
                    new_vehicle.setIs_edit(true);
                    new_vehicle.setVehicle_id(res.getLong("vid"));
                    new_vehicle.setVin(res.getString("vin"));
                    new_vehicle.setOutlet_name(res.getString("outletName"));
                    new_vehicle.setOutlet_id(res.getLong("outletId"));
                    new_vehicle.setCity(res.getString("city"));
                    new_vehicle.setProvince(res.getString("province"));
                    new_vehicle.setBrand_name(res.getString("brandName"));
                    new_vehicle.setBrand_id(res.getLong("brandId"));
                    new_vehicle.setOther_model(res.getBoolean("otherModel"));
                    new_vehicle.setSeries_id(res.getLong("seriesId"));
                    new_vehicle.setSeries_name(res.getString("seriesName"));
                    new_vehicle.setModel_id(res.getLong("modelId"));
                    new_vehicle.setModel_name(res.getString("modelName"));
                    new_vehicle.setBody_type(res.getString("bodyType"));
                    new_vehicle.setColor_name(res.getString("colorName"));
                    new_vehicle.setColor_id(res.getLong("colorId"));
                    new_vehicle.setGearbox(res.getString("gearbox"));
                    new_vehicle.setEmission(res.getString("emission"));
                    new_vehicle.setStatus(res.getString("status"));
                    new_vehicle.setSource(res.getString("source"));
                    new_vehicle.setRegister_date(res.getDate("registerDate"));
                    new_vehicle.setFactory_date(res.getDate("factoryDate"));
                    new_vehicle.setTurbo(res.getBoolean("turbo"));
                    new_vehicle.setVolume(res.getDouble("volume"));
                    new_vehicle.setDistance(res.getDouble("distance"));
                    new_vehicle.setNew_price(res.getDouble("newPrice"));
                    new_vehicle.setSell_price(res.getDouble("sellPrice"));
                    new_vehicle.setEngine_no(res.getString("engine"));
                    new_vehicle.setLicence_plate(res.getString("licencePlate"));
                    new_vehicle.setOwner_type(res.getString("ownerType"));
                    LitePal.deleteAll(Vehicle.class, "is_edit =?", "1");
                    new_vehicle.save();
                    vehicle = new_vehicle;
                    setDataToView();
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
        color = findViewById(R.id.color);
        gearbox = findViewById(R.id.gearbox);
        emission = findViewById(R.id.emission);
        status = findViewById(R.id.status);
        source = findViewById(R.id.source);
        vehicleType = findViewById(R.id.vehicleType);
        registerDate = findViewById(R.id.registerDate);
        factoryDate = findViewById(R.id.factoryDate);

        distance = findViewById(R.id.distance);
        new_price = findViewById(R.id.new_price);
        sell_price = findViewById(R.id.sell_price);
        engine_no = findViewById(R.id.engine_no);
        licence_plate = findViewById(R.id.licence_plate);
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
                Intent data = new Intent(VehiclePreviewActivity.this, ChangeOutletActivity.class);
                data.putExtra("outlet", vehicle.getOutlet_name());
                data.putExtra("city", vehicle.getCity());
                data.putExtra("is_all", true);
                startActivityForResult(data, 2);
                break;
            }
            case R.id.brand: {
                Intent data = new Intent(VehiclePreviewActivity.this, ChooseBrandActivity.class);
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
                final List<String> body_types = Arrays.asList(Vehicle.BODY_LIST);
                pvOptions = OptionPickUtils.getInstance().initOptionPicker(this, pvOptions, body_types, ObjectUtil.getString(body_type.getText()), "车身级别", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        String gear_box = body_types.get(options1);
                        gearbox.setText(gear_box);
                    }
                });
                pvOptions.show();
                break;
            }
            case R.id.color: {
                final List<VehicleColor> colors = VehicleColor.getColors();
                VehicleColor c = null;
                for (VehicleColor color : colors) {
                    if (color.getId() == color_id) c = color;
                }
                pvOptions = OptionPickUtils.getInstance().initOptionPicker(this, pvOptions, colors, c, "车身颜色", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        VehicleColor vehicleColor = colors.get(options1);
                        color.setText(vehicleColor.getName());
                        color_id = vehicleColor.getId();
                    }
                });
                pvOptions.show();
                break;
            }
            case R.id.gearbox: {
                final List<String> gearBoxs = Arrays.asList(Vehicle.GEAR_LIST);
                pvOptions = OptionPickUtils.getInstance().initOptionPicker(this, pvOptions, gearBoxs, ObjectUtil.getString(gearbox.getText()), "变速箱", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        String gear_box = gearBoxs.get(options1);
                        gearbox.setText(gear_box);
                    }
                });
                pvOptions.show();
                break;
            }
            case R.id.emission: {
                final List<String> emissionlist = Arrays.asList(Vehicle.EMISSION_LIST);
                pvOptions = OptionPickUtils.getInstance().initOptionPicker(this, pvOptions, emissionlist, ObjectUtil.getString(emission.getText()), "排放标准", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        String emiss = emissionlist.get(options1);
                        emission.setText(emiss);
                    }
                });
                pvOptions.show();
                break;
            }
            case R.id.status: {
                final List<String> strings = Arrays.asList(Vehicle.STATUS_LIST);
                pvOptions = OptionPickUtils.getInstance().initOptionPicker(this, pvOptions, strings, ObjectUtil.getString(status.getText()), "车况", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        String emiss = strings.get(options1);
                        status.setText(emiss);
                    }
                });
                pvOptions.show();
                break;
            }
            case R.id.source: {
                final List<String> strings = Arrays.asList(Vehicle.SOURCE_LIST);
                pvOptions = OptionPickUtils.getInstance().initOptionPicker(this, pvOptions, strings, ObjectUtil.getString(source.getText()), "来源渠道", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        String emiss = strings.get(options1);
                        source.setText(emiss);
                    }
                });
                pvOptions.show();
                break;
            }
            case R.id.vehicleType: {
                final List<String> strings = Arrays.asList(Vehicle.OWNER_LIST);
                pvOptions = OptionPickUtils.getInstance().initOptionPicker(this, pvOptions, strings, ObjectUtil.getString(vehicleType.getText()), "车源类型", new OptionPickUtils.OptionPickListener() {
                    @Override
                    public void click(int options1, int options2, int options3, View v) {
                        String emiss = strings.get(options1);
                        vehicleType.setText(emiss);
                    }
                });
                pvOptions.show();
                break;
            }
            case R.id.registerDate: {
                OptionPickUtils.getInstance().initTimePicker(this, pvTime, registerDate, true, 2000, 0, 1);
                break;
            }
            case R.id.factoryDate: {
                OptionPickUtils.getInstance().initTimePicker(this, pvTime, factoryDate, true, 2000, 0, 1);
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

        outlet.setText(vehicle.getOutlet_name().toString());

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

        if (StringUtils.isNotEmpty(vehicle.getColor_name())) {
            color.setText(vehicle.getColor_name());
        }
        color_id = ObjectUtil.getLong(vehicle.getColor_id());

        if (StringUtils.isNotEmpty(vehicle.getGearbox())) {
            gearbox.setText(vehicle.getGearbox());
        }

        if (StringUtils.isNotEmpty(vehicle.getEmission())) {
            emission.setText(vehicle.getEmission());
        }

        if (StringUtils.isNotEmpty(vehicle.getStatus())) {
            status.setText(vehicle.getStatus());
        }

        if (StringUtils.isNotEmpty(vehicle.getOwner_type())) {
            vehicleType.setText(vehicle.getOwner_type());
        }

        if (vehicle.getRegister_date() != null) {
            registerDate.setText(DateUtils.dateToStr(vehicle.getRegister_date(), "yyyy-MM-dd"));
        }

        if (vehicle.getFactory_date() != null) {
            factoryDate.setText(DateUtils.dateToStr(vehicle.getFactory_date(), "yyyy-MM-dd"));
        }

        if (StringUtils.isNotEmpty(vehicle.getSource())) {
            source.setText(vehicle.getSource());
        }

        if (ObjectUtil.getDouble(vehicle.getDistance()) > 0) {
            distance.setText(vehicle.getDistance().toString());
        }

        if (ObjectUtil.getDouble(vehicle.getNew_price()) > 0) {
            new_price.setText(vehicle.getNew_price().toString());
        }

        if (ObjectUtil.getDouble(vehicle.getSell_price()) > 0) {
            sell_price.setText(vehicle.getSell_price().toString());
        }

        if (StringUtils.isNotEmpty(vehicle.getEngine_no())) {
            engine_no.setText(vehicle.getEngine_no());
        }

        if (StringUtils.isNotEmpty(vehicle.getLicence_plate())) {
            licence_plate.setText(vehicle.getLicence_plate());
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
        vehicle.setBody_type(body_type.getText().toString());
        vehicle.setTurbo(turbo.isChecked());
        vehicle.setVolume(ObjectUtil.getDouble(volume.getText()));
        vehicle.setColor_id(color_id);
        vehicle.setColor_name(color.getText().toString());
        vehicle.setGearbox(gearbox.getText().toString());
        vehicle.setEmission(emission.getText().toString());
        vehicle.setStatus(status.getText().toString());
        vehicle.setSource(source.getText().toString());
        vehicle.setOwner_type(vehicleType.getText().toString());
        vehicle.setRegister_date(DateUtils.parseDateString(registerDate.getText().toString()));
        vehicle.setFactory_date(DateUtils.parseDateString(factoryDate.getText().toString()));
        vehicle.setDistance(ObjectUtil.getDouble(distance.getText()));
        vehicle.setNew_price(ObjectUtil.getDouble(new_price.getText()));
        vehicle.setSell_price(ObjectUtil.getDouble(sell_price.getText()));
        vehicle.setEngine_no(ObjectUtil.getString(engine_no.getText()));
        vehicle.setLicence_plate(ObjectUtil.getString(licence_plate.getText()));
        vehicle.save();
        vehicle = Vehicle.searchVehicle();
        JSONObject object = ObjectUtil.objectToJson(vehicle);
        Set<String> keys = object.keySet();
        JSONObject data = new JSONObject();
        for (String key : keys) {
            Object value = object.get(key);
            if (key.equals("vehicle_id")) key = "vid";
            if (!key.equals("series_name") || !key.equals("model_name")) {
                if (key.contains("_")) {
                    String[] names = key.split("\\_");
                    key = names[0] + names[1].substring(0, 1).toUpperCase() + names[1].substring(1);
                }
            }

            data.put(key, value);
        }
        saveVehicle(data);
    }

    private void saveVehicle(JSONObject data) {
        JSONObject post = new JSONObject();
        post.put("fromData", data);
        OkHttpUtils.getInstance().PostWithJson("/saveVehicleV2", post.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                final JSONObject res = JSONObject.parseObject(result_str);
                String msg = res.getString("msg");
                if (res.getBoolean("is_success")) {
                    DiaLogUtils.alert(VehiclePreviewActivity.this, "保存成功", "是否继续添加客户信息", new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {
                            LitePal.delete(Vehicle.class, vehicle.getId());
                            Intent intent = new Intent(VehiclePreviewActivity.this, EditCustomerActivity.class);
                            intent.putExtra("vid", res.getLong("vid"));
                            startActivity(intent);
                        }

                        @Override
                        public void fail() {
                            LitePal.delete(Vehicle.class, vehicle.getId());
                            startActivity(null, IndexActivity.class);
                        }
                    });
                } else {
                    DiaLogUtils.alertError(VehiclePreviewActivity.this, "错误", msg, new DiaLogUtils.DiaLogResultListener() {
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

    public void finish(View view) {
        finish();
    }

}
