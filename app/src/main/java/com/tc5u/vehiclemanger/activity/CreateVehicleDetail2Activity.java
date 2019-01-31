package com.tc5u.vehiclemanger.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.Vehicle;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.StringUtils;

import core.BaseActivity;


public class CreateVehicleDetail2Activity extends BaseActivity {

    private Vehicle vehicle = Vehicle.searchVehicle();

    private EditText distance, new_price, sell_price, engine_no, licence_plate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle_detail2);
        initToolBar();
        initView();
        setData2View();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vehicle = Vehicle.searchVehicle();
        setData2View();
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
        distance = findViewById(R.id.distance);
        new_price = findViewById(R.id.new_price);
        sell_price = findViewById(R.id.sell_price);
        engine_no = findViewById(R.id.engine_no);
        licence_plate = findViewById(R.id.licence_plate);
    }

    private void setData2View() {
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
    }

    public void finish(View view) {
        finish();
    }

    public void setDataToCache(View view) {
        vehicle.setDistance(ObjectUtil.getDouble(distance.getText()));
        vehicle.setNew_price(ObjectUtil.getDouble(new_price.getText()));
        vehicle.setSell_price(ObjectUtil.getDouble(sell_price.getText()));
        vehicle.setEngine_no(ObjectUtil.getString(engine_no.getText()));
        vehicle.setLicence_plate(ObjectUtil.getString(licence_plate.getText()));
        vehicle.save();
        startActivity(null, VehiclePreviewActivity.class);
    }


}
