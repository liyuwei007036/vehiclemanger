package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.SeriesAdapter;
import com.tc5u.vehiclemanger.customerstyle.SideBarItemEntity;
import com.tc5u.vehiclemanger.customerstyle.TitleDecoration;
import com.tc5u.vehiclemanger.model.Series;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import core.BaseActivity;

/**
 * 选择车系
 */
public class ChooseSeriesActivity extends BaseActivity {

    private Long brand_id;

    private String brand_name;

    private Long series_id;

    private String series_name;

    private TextView series;

    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_series);
        initView();
        initData();
        initToolBar();
        initSeriesData();
    }

    private void initData() {
        Intent intent = getIntent();
        brand_id = intent.getLongExtra("brand_id", 0L);
        brand_name = intent.getStringExtra("brand_name");
        series_id = intent.getLongExtra("series_id", 0L);
        series_name = intent.getStringExtra("series_name");
        series.setText(ObjectUtil.getString(brand_name) + " " + ObjectUtil.getString(series_name));
    }

    private void initToolBar() {
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
        series = findViewById(R.id.cur_series);
        recycler = findViewById(R.id.series_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
    }

    private void initSeriesData() {
        JSONObject data = new JSONObject();
        data.put("brand_id", brand_id);
        OkHttpUtils.getInstance().PostWithJson("/getVehicleSeriesListV1", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                    JSONObject series = res.getJSONObject("list");
                    recycler.setAdapter(new SeriesAdapter(getSeries(series), series_id));
                    recycler.addItemDecoration(new TitleDecoration(new TitleDecoration.TitleAttributes(ChooseSeriesActivity.this)));
                    recycler.addItemDecoration(new DividerItemDecoration(ChooseSeriesActivity.this, DividerItemDecoration.VERTICAL));
                } else {

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

    private List<SideBarItemEntity<Series>> getSeries(JSONObject series) {
        List<Series> list = new ArrayList<>();
        Set<String> kes = series.keySet();
        for (String parent_name : kes) {
            JSONArray array = series.getJSONArray(parent_name);
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                Long id = object.getLong("id");
                String name = object.getString("name");
                list.add(new Series(id, name, parent_name));
            }
        }
        return fillData(list);
    }


    private List<SideBarItemEntity<Series>> fillData(List<Series> series) {
        List<SideBarItemEntity<Series>> sortList = new ArrayList<>();
        for (Series aDate : series) {
            SideBarItemEntity<Series> item = new SideBarItemEntity<>();
            item.setValue(aDate);
            item.setSortLetters(aDate.getParent_name());
            sortList.add(item);
        }
        return sortList;
    }
}
