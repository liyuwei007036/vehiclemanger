package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.SearchAdapter;
import com.tc5u.vehiclemanger.fragment.VehicleFragment;
import com.tc5u.vehiclemanger.model.VehicleInfo;
import com.tc5u.vehiclemanger.model.VehicleSearchModel;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

public class AllVehicleActivity extends BaseActivity {

    private Toolbar mToolbarTb;

    private TextView search_no, search_publish, search_in_outlet, search_owner_type, search_brand;

    private ListView listView;

    private SearchAdapter adapter;

    private List<VehicleSearchModel> publishArgs = new ArrayList<>();

    private List<VehicleSearchModel> outletArgs = new ArrayList<>();

    private List<VehicleSearchModel> ownerTypeArgs = new ArrayList<>();

    private List<VehicleSearchModel> search_source = new ArrayList<>();

    // 查询条件
    private Boolean publish_value = null, inoutlet_value = null;

    private String publish_name = "是否上架", inoutlet_name = "场内外", owner_type_name = "车源类型", brand_name = "品牌", cur_city;

    private String keyWord = "", owner_type_value = "";

    private String range;

    private int page = 0;

    private Long brand_id = 0L, outletId = 0L;

    private String outlet_name;

    private VehicleFragment fragment;

    private List<VehicleInfo> vehicleInfos = new ArrayList<>();

    private Long total;

    private TextView city_text;

    private SmartRefreshLayout smartRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_vehicle);
        initIntent();
        initView();
        initSearchData();
        initListener();
        initSearchValue();
        smartRefresh.autoRefresh();
    }

    private void initIntent() {
        Intent intent = getIntent();
        int in_outlet = ObjectUtil.getInteger(intent.getStringExtra("in_outlet"));
        outletId = ObjectUtil.getLong(intent.getStringExtra("outlet_id"));
        range = intent.getStringExtra("range");
        switch (in_outlet) {
            case 0:
                inoutlet_value = null;
                inoutlet_name = "场内外";
                break;
            case 1:
                inoutlet_value = true;
                inoutlet_name = "场内";
                break;
            case 2:
                inoutlet_value = false;
                inoutlet_name = "场外";
                break;
        }
        String owner_type = intent.getStringExtra("ownerType");
        cur_city = intent.getStringExtra("city");
        if (StringUtils.isEmpty(owner_type)) {
            owner_type_value = "";
            owner_type_name = "车源类型";
        } else {
            owner_type_name = owner_type_value = owner_type;
        }
    }

    private void initView() {
        fragment = (VehicleFragment) getSupportFragmentManager().findFragmentById(R.id.vehicles_fragment);
        mToolbarTb = findViewById(R.id.tb_toolbar);
        search_no = findViewById(R.id.search_no);
        search_publish = findViewById(R.id.search_publish);
        search_in_outlet = findViewById(R.id.search_in_outlet);
        search_owner_type = findViewById(R.id.search_owner_type);
        search_brand = findViewById(R.id.search_brand);
        city_text = findViewById(R.id.city);
        listView = findViewById(R.id.search_list);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new SearchAdapter(search_source);
        listView.setAdapter(adapter);
        if (range.equals("me")) city_text.setVisibility(View.GONE);
        smartRefresh = findViewById(R.id.refreshLayout);
        smartRefresh.setEnableLoadMoreWhenContentNotFull(true);
    }

    private void initSearchValue() {
        search_publish.setText(publish_name);
        search_in_outlet.setText(inoutlet_name);
        search_owner_type.setText(owner_type_name);
        search_brand.setText(brand_name);
        city_text.setText(cur_city);
        page = 0;
        vehicleInfos.clear();
    }

    private void initListener() {
        setSmartRefreshListener();
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VehicleSearchModel model = search_source.get(i);
                switch (model.getType()) {
                    case VehicleSearchModel.IN_OUTLET: {
                        if (model.getValue() == null) {
                            inoutlet_value = null;
                            inoutlet_name = "场内外";
                        } else {
                            inoutlet_value = ObjectUtil.getBoolean(model.getValue());
                            inoutlet_name = model.getName();
                        }
                        break;
                    }
                    case VehicleSearchModel.OWNER_TYPE: {
                        if (StringUtils.isEmpty(ObjectUtil.getString(model.getValue()))) {
                            owner_type_name = "车源类型";
                            owner_type_value = "";
                        } else {
                            owner_type_name = model.getName();
                            owner_type_value = ObjectUtil.getString(model.getValue());
                        }
                        break;
                    }
                    case VehicleSearchModel.PUBLISH: {
                        if (model.getValue() == null) {
                            publish_value = null;
                            publish_name = "是否上架";
                        } else {
                            publish_value = ObjectUtil.getBoolean(model.getValue());
                            publish_name = model.getName();
                        }
                        break;
                    }
                }
                listView.setVisibility(View.GONE);
                initSearchValue();
                getVehicleRequest();

            }
        });

        city_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllVehicleActivity.this, ChangeOutletActivity.class);
                intent.putExtra("city", cur_city);
                intent.putExtra("outlet", outlet_name);
                intent.putExtra("is_all", true);
                startActivityForResult(intent, 1);
            }
        });

        search_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllVehicleActivity.this, ChooseBrandActivity.class);
                intent.putExtra("brand_name", brand_name);
                intent.putExtra("req_code", 2);
                AllVehicleActivity.this.startActivityForResult(intent, 2);
            }
        });

        search_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllVehicleActivity.this, SearchActivity.class);
                intent.putExtra("keyWord", keyWord);
                AllVehicleActivity.this.startActivityForResult(intent, 3);
            }
        });


    }

    private void initSearchData() {
        publishArgs.add(new VehicleSearchModel("全部", null, VehicleSearchModel.PUBLISH));
        publishArgs.add(new VehicleSearchModel("已上架", true, VehicleSearchModel.PUBLISH));
        publishArgs.add(new VehicleSearchModel("未上架", false, VehicleSearchModel.PUBLISH));

        outletArgs.add(new VehicleSearchModel("全部", null, VehicleSearchModel.IN_OUTLET));
        outletArgs.add(new VehicleSearchModel("场内", true, VehicleSearchModel.IN_OUTLET));
        outletArgs.add(new VehicleSearchModel("场外", false, VehicleSearchModel.IN_OUTLET));

        ownerTypeArgs.add(new VehicleSearchModel("全部", "", VehicleSearchModel.OWNER_TYPE));
        ownerTypeArgs.add(new VehicleSearchModel("自营", "自营", VehicleSearchModel.OWNER_TYPE));
        ownerTypeArgs.add(new VehicleSearchModel("个人", "个人", VehicleSearchModel.OWNER_TYPE));
        ownerTypeArgs.add(new VehicleSearchModel("商户", "商户", VehicleSearchModel.OWNER_TYPE));
    }

    public void searchOnclick(View view) {
        if (listView.getVisibility() == View.VISIBLE) {
            listView.setVisibility(View.GONE);
            return;
        }
        switch (view.getId()) {
            case R.id.search_in_outlet: {
                search_source.clear();
                search_source.addAll(outletArgs);
                break;
            }
            case R.id.search_publish: {
                search_source.clear();
                search_source.addAll(publishArgs);
                break;
            }
            case R.id.search_owner_type: {
                search_source.clear();
                search_source.addAll(ownerTypeArgs);
                break;
            }

        }
        listView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void getVehicleRequest() {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        String url = "/getVehicleDataV1";
        JSONObject data = new JSONObject();
        data.put("page", page);
        data.put("is_publish", publish_value);
        data.put("in_outlet", inoutlet_value);
        data.put("headUrl", okHttpUtils.IMG_URL);
        data.put("owner_type", owner_type_value);
        data.put("brand_id", brand_id);
        data.put("keyWord", keyWord);
        data.put("range", range);
        data.put("outletId", outletId);
        okHttpUtils.PostWithJson(url, data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {

            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {
                smartRefresh.finishLoadMore(true);
                smartRefresh.finishRefresh(true);
            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (res.getInteger("status") == 0) {
                    initVehicleData(res);
                } else {
                    showToast(ObjectUtil.getString(res.getString("msg")));
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                smartRefresh.finishLoadMore(false);
                smartRefresh.finishRefresh(false);
            }

            @Override
            public void onError(String url, String result_str) {
                smartRefresh.finishLoadMore(false);
                smartRefresh.finishRefresh(false);
            }
        });
    }

    private void initVehicleData(JSONObject object) {
        cur_city = object.getString("city");
        if (range.equals("all")) city_text.setText(cur_city);
        outletId = object.getLong("o_id");
        outlet_name = ObjectUtil.getString(object.getString("outlet"));
        total = object.getLong("total");
        JSONArray cars = object.getJSONArray("cars");
        loadVehicle(cars);
    }

    private void loadVehicle(JSONArray cars) {
        for (int i = 0; i < cars.size(); i++) {
            JSONObject object = cars.getJSONObject(i);
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
            vehicleInfo.setSell_price(object.getDouble("sell_price"));
            vehicleInfo.setPublished(object.getBoolean("published"));
            vehicleInfos.add(vehicleInfo);
        }
        if (total <= vehicleInfos.size()) {
            smartRefresh.finishLoadMoreWithNoMoreData();
        } else {
            smartRefresh.setNoMoreData(false);
        }
        fragment.setVehicleInfosForAllVehicle(vehicleInfos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 8) {
            // 切换卖场
            outletId = data.getLongExtra("outlet_id", 0L);
            outlet_name = data.getStringExtra("outlet_name");
            page = 0;
            vehicleInfos.clear();
            getVehicleRequest();
        }

        if (requestCode == 2 && resultCode == 9) {
            brand_id = data.getLongExtra("brand_id", 0L);
            brand_name = data.getStringExtra("brand_name");
            search_brand.setText(brand_name);
            page = 0;
            vehicleInfos.clear();
            getVehicleRequest();
        }

        if (requestCode == 3 && resultCode == 6) {
            keyWord = data.getStringExtra("keyWord");
            if (StringUtils.isNotEmpty(keyWord)) {
                search_no.setText(keyWord);
            } else {
                search_no.setText(null);
            }
            page = 0;
            vehicleInfos.clear();
            getVehicleRequest();
        }
    }

    private void setSmartRefreshListener() {

        smartRefresh.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
            }

            @Override
            public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
            }

            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getVehicleRequest();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 0;
                vehicleInfos.clear();
                getVehicleRequest();

            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

            }
        });
    }


    public void autoRefresh(VehicleInfo vehicleInfo) {
        if (publish_value == null || !publish_value) {
            int i = vehicleInfos.indexOf(vehicleInfo);
            vehicleInfo.setPublished(false);
            vehicleInfos.remove(i);
            vehicleInfos.add(i, vehicleInfo);
        } else {
            vehicleInfos.remove(vehicleInfo);
        }
        fragment.setVehicleInfosForAllVehicle(vehicleInfos);
    }

}
