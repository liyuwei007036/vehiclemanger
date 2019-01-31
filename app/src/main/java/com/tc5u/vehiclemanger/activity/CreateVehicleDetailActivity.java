package com.tc5u.vehiclemanger.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.VehicleColor;
import com.tc5u.vehiclemanger.model.Vehicle;
import com.tc5u.vehiclemanger.utils.DateUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OptionPickUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import core.BaseActivity;

public class CreateVehicleDetailActivity extends BaseActivity {

    private Vehicle vehicle = Vehicle.searchVehicle();

    private TextView color, gearbox, emission, status, source, vehicleType, registerDate, factoryDate;

    private OptionsPickerView<Object> pvOptions;

    private TimePickerView pvTime;

    private Long color_id = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle_detail);
        initToolBar();
        initView();
        initDataToView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vehicle = Vehicle.searchVehicle();
        initDataToView();
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
        color = findViewById(R.id.color);
        gearbox = findViewById(R.id.gearbox);
        emission = findViewById(R.id.emission);
        status = findViewById(R.id.status);
        source = findViewById(R.id.source);
        vehicleType = findViewById(R.id.vehicleType);
        registerDate = findViewById(R.id.registerDate);
        factoryDate = findViewById(R.id.factoryDate);
    }


    private void initDataToView() {
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
    }

    public void finish(View view) {
        finish();
    }

    public void setDataToCache(View view) {
        vehicle.setColor_id(color_id);
        vehicle.setColor_name(color.getText().toString());
        vehicle.setGearbox(gearbox.getText().toString());
        vehicle.setEmission(emission.getText().toString());
        vehicle.setStatus(status.getText().toString());
        vehicle.setSource(source.getText().toString());
        vehicle.setOwner_type(vehicleType.getText().toString());
        vehicle.setRegister_date(DateUtils.parseDateString(registerDate.getText().toString()));
        vehicle.setFactory_date(DateUtils.parseDateString(factoryDate.getText().toString()));
        vehicle.save();
        startActivity(null, CreateVehicleDetail2Activity.class);
    }

    public void choose(View view) {
        switch (view.getId()) {
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


}
