package com.tc5u.vehiclemanger.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.VehicleMDemandAdapter;
import com.tc5u.vehiclemanger.customerstyle.BadgeRadioButton;
import com.tc5u.vehiclemanger.model.CusDemand;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

public class VehicleMatchActivity extends BaseActivity {

    private Long vehicle_id;

    private TextView car_name, city, register_date, distance, sell_price;

    private ImageView car_img;

    private Boolean in_outlet;

    private Menu menu;

    private int cur_tap = 0;

    private SmartRefreshLayout smartRefreshLayout;

    private RecyclerView recyclerView;

    private RadioGroup radioGroup;

    private BadgeRadioButton local_btn, other_btn;

    private List<CusDemand> local_demands = new ArrayList<>();
    private List<CusDemand> other_demands = new ArrayList<>();
    private List<CusDemand> demands = new ArrayList<>();

    private Long local_total = 0L, other_total = 0L;

    private int local_page = 0, other_page = 0;

    private VehicleMDemandAdapter demandAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_match);
        intIntent();
        initToolBar();
        initView();
        getVehicleInfo();
        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.tool_bar_vehicle_match, menu);
        if (in_outlet == null) return true;
        if (in_outlet) {
//            menu.findItem(R.id.in).setVisible(false);
        } else {
//            menu.findItem(R.id.out).setVisible(false);
        }
        return true;
    }

    private void initView() {
        car_img = findViewById(R.id.car_img);
        car_name = findViewById(R.id.car_name);
        city = findViewById(R.id.city);
        register_date = findViewById(R.id.register_date);
        distance = findViewById(R.id.distance);
        sell_price = findViewById(R.id.sell_price);
        radioGroup = findViewById(R.id.group);
        local_btn = findViewById(R.id.local);
        other_btn = findViewById(R.id.other);
        smartRefreshLayout = findViewById(R.id.smart_layout);
        recyclerView = findViewById(R.id.demands);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        demandAdapter = new VehicleMDemandAdapter(demands);
        recyclerView.setAdapter(demandAdapter);

    }

    private void intIntent() {
        vehicle_id = getIntent().getLongExtra("vehicle_id", 0L);
    }


    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.local: {
                        if (cur_tap == 0) break;
                        cur_tap = 0;
                        if (local_demands.size() < 1) {
                            getDemands();
                        } else {
                            changeView();
                        }
                        break;
                    }
                    case R.id.other: {
                        if (cur_tap == 1) break;
                        cur_tap = 1;
                        if (other_demands.size() < 1) {
                            getDemands();
                        } else {
                            changeView();
                        }
                        break;
                    }
                }
            }
        });

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getDemands();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (cur_tap == 0) {
                    local_page = 0;
                } else {
                    other_page = 0;
                }
                getDemands();
            }
        });
        smartRefreshLayout.autoRefresh();

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
        mToolbarTb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.in:
//                        Toast.makeText(VehicleMatchActivity.this, "入库" + vehicle_id, Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.out:
//                        Toast.makeText(VehicleMatchActivity.this, "出库" + vehicle_id, Toast.LENGTH_SHORT).show();
//                        break;
                    case R.id.eve:
                        JSONObject date = new JSONObject();
                        date.put("vehicle_id", vehicle_id);
                        startActivity(date, VehicleEvaluationActivity.class);
                        break;
                }
                return true;
            }
        });
    }

    private void changeView() {
        if (cur_tap == 0) {
            demandAdapter.setDemands(local_demands);
        } else {
            demandAdapter.setDemands(other_demands);
        }
    }

    private void getVehicleInfo() {
        String url = "/getVehicleInfo";
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        JSONObject data = new JSONObject();
        data.put("v_id", vehicle_id);
        data.put("headUrl", okHttpUtils.IMG_URL);
        okHttpUtils.PostWithJson(url, data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                car_name.setText(ObjectUtil.getString(res.getString("name")));
                distance.setText(ObjectUtil.getString(res.getString("distance")));
                city.setText(ObjectUtil.getString(res.getString("city")));
                in_outlet = ObjectUtil.getBoolean(res.getBoolean("in_outlet"));
                register_date.setText(ObjectUtil.getString(res.getString("register_date")));
                sell_price.setText(ObjectUtil.getString(res.getString("sell_price")));
                String src = ObjectUtil.getString(res.getString("src"));
                if (src.startsWith("http")) {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.placeholder) //加载成功之前占位图
                            .error(R.drawable.img_error)//加载错误之后的错误图
                            .fitCenter();
                    Glide.with(VehicleMatchActivity.this).load(src).apply(options).into(car_img);
                } else {
                    car_img.setImageResource(R.drawable.img_error);
                }

                if (menu != null) {
//                    if (in_outlet) {
//                        menu.findItem(R.id.in).setVisible(false);
//                    } else {
//                        menu.findItem(R.id.out).setVisible(false);
//                    }
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                showToast("请求错误");
            }

            @Override
            public void onError(String url, String result_str) {
                showToast("请求失败");
            }
        });
    }

    private void getDemands() {
        String local_url = "/getLocalVehicleDemand";
        String other_url = "/getRemoteVehicleDemand";

        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        JSONObject data = new JSONObject();
        data.put("v_id", vehicle_id);
        data.put("currentPage", cur_tap == 0 ? local_page : other_page);

        okHttpUtils.PostWithJson(cur_tap == 0 ? local_url : other_url, data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (res.getInteger("status") == 0) {
                    addDemands(res);
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                showToast("请求错误");
            }

            @Override
            public void onError(String url, String result_str) {
                showToast("请求失败");
            }
        });
    }

    private void addDemands(JSONObject res) {
        JSONArray demand = res.getJSONArray("demand");
        Long total = res.getLong("total");
        if (cur_tap == 0) {
            local_total = total;
            local_btn.setBadgeNumber(ObjectUtil.getInteger(total)).setBadgeGravity(Gravity.RIGHT);
        } else {
            other_total = total;
            other_btn.setBadgeNumber(ObjectUtil.getInteger(total)).setBadgeGravity(Gravity.RIGHT);
        }

        for (int i = 0; i < demand.size(); i++) {
            CusDemand d = new CusDemand(demand.getJSONObject(i));
            if (cur_tap == 0) local_demands.add(d);
            else other_demands.add(d);
        }

        demands.clear();
        if (cur_tap == 0) {
            demands.addAll(local_demands);
            local_page++;
        } else {
            demands.addAll(other_demands);
            other_page++;
        }
        changeView();
    }
}
