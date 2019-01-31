package com.tc5u.vehiclemanger.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.CustomerDemandAdapter;
import com.tc5u.vehiclemanger.model.CustomerDemand;
import com.tc5u.vehiclemanger.utils.CheckUpdateUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;


/**
 * 首页
 */
public class IndexActivity extends BaseActivity {
    private int page = 0;
    private int dateFlag = 0;
    private List<CustomerDemand> demandList = new ArrayList<>();
    private TextView cws, outlet_name, kc, sh, zy, gr, cus_demand_num;
    private CustomerDemandAdapter adapter;
    private RefreshLayout refreshLayout;
    private Boolean is_refresh;
    private Button day, week, month;
    private String city;
    private String cur_outlet;
    private Long outlet_id = 0L;
    private BottomNavigationView bottomNavigationView;

    private CheckUpdateUtil checkUpdateUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
        initListener();
        getIndexData();
        checkUpdateUtil = CheckUpdateUtil.getInstance();
        Boolean need_update = checkUpdateUtil.checkUpdate(IndexActivity.this);

        if (need_update) {
            getAuth();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.item_home);
    }


    private void initView() {
        cws = findViewById(R.id.cws);
        outlet_name = findViewById(R.id.outlet_name);
        kc = findViewById(R.id.kc);
        sh = findViewById(R.id.sh);
        gr = findViewById(R.id.gr);
        zy = findViewById(R.id.zy);
        cus_demand_num = findViewById(R.id.custom_demand_num);
        RecyclerView recyclerView = findViewById(R.id.recycler_demand_list);
        adapter = new CustomerDemandAdapter(demandList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableAutoLoadMore(false);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                is_refresh = true;
                page = 0;
                getIndexData();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                is_refresh = false;
                getDemandData();
            }

        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        if (item.isChecked()) return false;
                        item.setChecked(true);
                        switch (item.getItemId()) {
                            case R.id.item_upload:
                                startActivity(null, UploadVehicleActivity.class);
                                break;
                            case R.id.item_mine:
                                startActivity(null, MineActivity.class);
                                break;
                        }
                        return false;
                    }
                });
    }

    /**
     * 获取首页数据包括需求
     */
    private void getIndexData() {
        String url = "/getIndexData";
        JSONObject body = new JSONObject();
        body.put("page", page);
        body.put("dateFlag", dateFlag);
        OkHttpUtils.getInstance().PostWithJson(url, body.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
                if (is_refresh != null && is_refresh) {
                    refreshLayout.finishRefresh(false);
                }
            }

            @Override
            public void onLoadingShow() {
            }

            @Override
            public void onLoadingDismiss() {
                if (is_refresh != null && is_refresh) {
                    refreshLayout.finishRefresh();
                }
            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (res.getInteger("status") == 0) {
                    demandList.clear();
                    initData(res);
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                showToast("获取数据失败，请刷新");
            }

            @Override
            public void onError(String url, String result_str) {
                showToast("获取数据失败，请刷新");
            }
        });
    }

    private void getDemandData() {
        String url = "/getDemandData";
        JSONObject body = new JSONObject();
        body.put("page", page + 1);
        body.put("dateFlag", dateFlag);
        OkHttpUtils.getInstance().PostWithJson(url, body.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
                if (is_refresh != null && !is_refresh) {
                    refreshLayout.finishLoadMore(false);
                }
            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {
                if (is_refresh != null && !is_refresh) {
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (res.getInteger("status") == 0) {
                    initDemands(res);
                    page += 1;
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

    private void initData(JSONObject res) {
        String oname = res.getString("o_name");
        String park_space = res.getString("park_space");
        String kcs = res.getString("inout");
        String shc = res.getString("other");
        String zyc = res.getString("owner");
        String grc = res.getString("personal");
        JSONObject demands = res.getJSONObject("demands");
        city = res.getString("city");
        outlet_id = res.getLong("o_id");
        cur_outlet = oname;
        outlet_name.setText(oname);
        cws.setText(park_space);
        kc.setText(kcs);
        sh.setText(shc);
        zy.setText(zyc);
        gr.setText(grc);
        initDemands(demands);
    }

    private void initDemands(JSONObject demands) {
        String demands_num = demands.getString("total");
        String a = "客户需求 ( " + demands_num + " 条)";
        cus_demand_num.setText(a);
        JSONArray array = demands.getJSONArray("cars");
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            CustomerDemand demand = new CustomerDemand();
            demand.setId(object.getLong("id"));
            JSONObject object1 = object.getJSONObject("info");
            if (object1 != null) {
                JSONArray array1 = object1.getJSONArray("cars");
                String cars = "";
                for (int j = 0; j < array1.size(); j++) {
                    cars += array1.getString(j);
                    if (j < array1.size() - 1) cars += "\n";
                }
                demand.setCars(cars);
            } else {
                demand.setCars("无品牌车系");
            }
            demand.setRemark(object.getString("remark"));
            demand.setCus_name(object.getString("customer"));
            demand.setDate(object.getString("date"));
            demand.setPhone(object.getString("phone"));
            demandList.add(demand);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 时间点击事件
     *
     * @param view
     */
    public void showDemand(View view) {
        day = findViewById(R.id.day);
        week = findViewById(R.id.week);
        month = findViewById(R.id.month);
        switch (view.getId()) {
            case R.id.day:
                if (dateFlag == 1) {
                    dateFlag = 0;
                    day.setBackgroundColor(getResources().getColor(R.color.white));
                    day.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                    day.setTextColor(getResources().getColor(R.color.tl));
                } else {
                    dateFlag = 1;
                    day.setBackground(getResources().getDrawable(R.drawable.un_hollow_btn));
                    day.setTextColor(getResources().getColor(R.color.white));
                }

                week.setBackgroundColor(getResources().getColor(R.color.white));
                week.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                week.setTextColor(getResources().getColor(R.color.tl));

                month.setBackgroundColor(getResources().getColor(R.color.white));
                month.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                month.setTextColor(getResources().getColor(R.color.tl));
                break;
            case R.id.week:
                if (dateFlag == 2) {
                    dateFlag = 0;
                    week.setBackgroundColor(getResources().getColor(R.color.white));
                    week.setTextColor(getResources().getColor(R.color.tl));
                    week.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                } else {
                    dateFlag = 2;
                    week.setBackground(getResources().getDrawable(R.drawable.un_hollow_btn));
                    week.setTextColor(getResources().getColor(R.color.white));
                }
                day.setBackgroundColor(getResources().getColor(R.color.white));
                day.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                day.setTextColor(getResources().getColor(R.color.tl));

                month.setBackgroundColor(getResources().getColor(R.color.white));
                month.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                month.setTextColor(getResources().getColor(R.color.tl));
                break;
            case R.id.month:
                if (dateFlag == 3) {
                    dateFlag = 0;
                    month.setBackgroundColor(getResources().getColor(R.color.white));
                    month.setTextColor(getResources().getColor(R.color.tl));
                    month.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                } else {
                    dateFlag = 3;
                    month.setBackground(getResources().getDrawable(R.drawable.un_hollow_btn));
                    month.setTextColor(getResources().getColor(R.color.white));
                }
                day.setBackgroundColor(getResources().getColor(R.color.white));
                day.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                day.setTextColor(getResources().getColor(R.color.tl));

                week.setBackgroundColor(getResources().getColor(R.color.white));
                week.setBackground(getResources().getDrawable(R.drawable.hollow_btn));
                week.setTextColor(getResources().getColor(R.color.tl));
                break;
        }
        page = -1;
        demandList.clear();
        getDemandData();
    }

    /**
     * 车源查询
     *
     * @param view
     */
    public void showVehicle(View view) {
        JSONObject object = new JSONObject();
        switch (view.getId()) {
            case R.id.eve:
                startActivity(null, VehicleEvaluatePriceActivity.class);
                break;
            case R.id.mycar:
                object.put("ownerType", "");
                object.put("in_outlet", 0);
                object.put("outlet_id", outlet_id);
                object.put("range", "me");
                startActivity(object, AllVehicleActivity.class);
                break;
            case R.id.allcar:
                object.put("ownerType", "");
                object.put("in_outlet", 0);
                object.put("outlet_id", outlet_id);
                object.put("city", city);
                object.put("range", "all");
                startActivity(object, AllVehicleActivity.class);
                break;
            case R.id.kc_view:
                object.put("ownerType", "");
                object.put("in_outlet", 1);
                object.put("outlet_id", outlet_id);
                object.put("city", city);
                object.put("range", "all");
                startActivity(object, AllVehicleActivity.class);
                break;
            case R.id.sh_view:
                object.put("ownerType", "商户");
                object.put("in_outlet", 1);
                object.put("outlet_id", outlet_id);
                object.put("city", city);
                object.put("range", "all");
                startActivity(object, AllVehicleActivity.class);
                break;
            case R.id.zy_view:
                object.put("ownerType", "自营");
                object.put("in_outlet", 1);
                object.put("outlet_id", outlet_id);
                object.put("city", city);
                object.put("range", "all");
                startActivity(object, AllVehicleActivity.class);
                break;
            case R.id.gr_view:
                object.put("ownerType", "个人");
                object.put("in_outlet", 1);
                object.put("city", city);
                object.put("outlet_id", outlet_id);
                object.put("range", "all");
                startActivity(object, AllVehicleActivity.class);
                break;
            case R.id.outlet_name_view:
                Intent intent = new Intent(this, ChangeOutletActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("outlet", cur_outlet);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            page = 0;
            dateFlag = 0;
            getIndexData();
        }
    }

    // 动态获取权限
    private void getAuth() {
        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, mPermissionList, 100);
        checkUpdateUtil.showUpdateDialog(this);
    }


}
