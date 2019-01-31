package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.customerstyle.BadgeRadioButton;
import com.tc5u.vehiclemanger.fragment.VehicleFragment;
import com.tc5u.vehiclemanger.model.VehicleInfo;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

public class DemandMatchActivity extends BaseActivity {

    private BadgeRadioButton local, other;

    private long customer_demand_id;

    private VehicleFragment fragment;

    private int samePage = 0, difPage = 0, cur_tap = 0;

    private List<VehicleInfo> sameVehicles = new ArrayList();

    private List<VehicleInfo> difVehicles = new ArrayList();

    private int sameTotal = 0, difTotal = 0;

    private TextView cars, city, customer_name, date, remark, price, distance;

    private RadioGroup radioGroup;

    private SmartRefreshLayout refreshLayout;

    private Toolbar mToolbarTb;

    private JSONObject cus_demand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        Intent intent = getIntent();
        customer_demand_id = intent.getLongExtra("demand_id", 0L);
        initData();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_demand_match);
        local = findViewById(R.id.local);
        other = findViewById(R.id.other);
        cars = findViewById(R.id.cars);
        city = findViewById(R.id.city);
        customer_name = findViewById(R.id.customer_name);
        date = findViewById(R.id.date);
        remark = findViewById(R.id.remark);
        price = findViewById(R.id.price);
        distance = findViewById(R.id.distance);
        radioGroup = findViewById(R.id.group);
        refreshLayout = findViewById(R.id.refreshLayout);
        fragment = (VehicleFragment) getSupportFragmentManager().findFragmentById(R.id.vehicles_fragment);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mToolbarTb = findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.local: {
                        if (cur_tap == 0) break;
                        cur_tap = 0;
                        if (sameVehicles.size() < 1) {
                            getMatchVehicle();
                        } else {
                            changeView();
                        }
                        break;
                    }
                    case R.id.other: {
                        if (cur_tap == 1) break;
                        cur_tap = 1;
                        if (difVehicles.size() < 1) {
                            getMatchVehicle();
                        } else {
                            changeView();
                        }
                        break;
                    }
                }
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (cur_tap == 0) {
                    if (sameTotal <= sameVehicles.size()) {
                        refreshLayout.finishLoadMore();
                        return;
                    }
                } else {
                    if (difTotal <= difVehicles.size()) {
                        refreshLayout.finishLoadMore();
                        return;
                    }
                }

                if (cur_tap == 0) samePage++;
                else difPage++;
                getMatchVehicle();
            }
        });


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (cur_tap == 0) {
                    samePage = 0;
                    sameVehicles.clear();
                } else {
                    difPage = 0;
                    difVehicles.clear();
                }
                getMatchVehicle();
            }
        });


        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        String url = "/getDemandMatchData";
        JSONObject object = new JSONObject();
        object.put("id", customer_demand_id);
        object.put("headUrl", okHttpUtils.IMG_URL);
        okHttpUtils.PostWithJson(url, object.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
                refreshLayout.finishLoadMore(false);
                refreshLayout.finishRefresh(false);
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
                    renderData(res.getJSONObject("demand"));
                    renderDataToFra(res.getJSONObject("match"));
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                Log.i("s", "onFailure");
            }

            @Override
            public void onError(String url, String result_str) {
                Log.i("s", "onError");
            }
        });
    }

    private void getMatchVehicle() {
        String url = "/getMatchVehicle";
        JSONObject object = new JSONObject();
        object.put("id", customer_demand_id);
        object.put("headUrl", "https://ei.tc5u.cn");
        object.put("is_same_city", !ObjectUtil.getBoolean(cur_tap));
        object.put("page", cur_tap == 0 ? samePage : difPage);
        OkHttpUtils.getInstance().PostWithJson(url, object.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
                refreshLayout.finishLoadMore(false);
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (res.getInteger("status") == 0) {
                    renderDataToFra(res);
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

    private void renderData(JSONObject demand) {
        cus_demand = demand;
        String title = "";
        JSONArray cars_name = demand.getJSONArray("car_name");
        for (int i = 0; i < cars_name.size(); i++) {
            title += cars_name.getString(i);
            if (i < cars_name.size() - 1) title += "\n";
        }
        cars.setText(title);
        city.setText(ObjectUtil.getString(demand.getString("city")));
        customer_name.setText(ObjectUtil.getString(demand.getString("customer")));
        date.setText(ObjectUtil.getString(demand.getString("date")));
        remark.setText(ObjectUtil.getString(demand.getString("remark")));
        price.setText(ObjectUtil.getString(demand.getString("price")));
        distance.setText(ObjectUtil.getString(demand.getString("distance")));
    }

    private void renderDataToFra(JSONObject match) {
        if (match == null) match = new JSONObject();
        int total = ObjectUtil.getInteger(match.getInteger("total"));
        if (cur_tap == 0) {
            local.setBadgeNumber(total).setBadgeGravity(Gravity.RIGHT);
            sameTotal = total;
        } else {
            other.setBadgeNumber(total).setBadgeGravity(Gravity.RIGHT);
            difTotal = total;
        }
        JSONArray array = match.getJSONArray("array");
        if (array == null) return;
        JSONObject object;
        for (int i = 0; i < array.size(); i++) {
            object = array.getJSONObject(i);
            VehicleInfo vehicleInfo = new VehicleInfo();
            vehicleInfo.setName(object.getString("name"));
            vehicleInfo.setCan_move(object.getBoolean("can_move"));
            vehicleInfo.setId(object.getLong("id"));
            vehicleInfo.setHasPublishApply(object.getBoolean("hasPublishApply"));
            vehicleInfo.setCity(object.getString("city"));
            vehicleInfo.setDistance(object.getDoubleValue("distance"));
            vehicleInfo.setRegister_date(object.getString("register_date"));
            vehicleInfo.setImg_url(object.getString("src"));
            vehicleInfo.setUser_id(object.getLong("uid"));
            vehicleInfo.setType(object.getString("type"));
            vehicleInfo.setCustomer_demand_id(customer_demand_id);
            vehicleInfo.setSell_price(object.getDouble("sell_price"));
            vehicleInfo.setPublished(object.getBoolean("published"));
            if (cur_tap == 0) {
                sameVehicles.add(vehicleInfo);
            } else {
                difVehicles.add(vehicleInfo);
            }
        }
        changeView();
    }

    private void changeView() {
        fragment.setVehicleInfos(sameVehicles, difVehicles, cur_tap);
    }

    public void call(View view) {
        String phone = cus_demand.getString("phone");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        DemandMatchActivity.this.startActivity(intent);
    }
}
