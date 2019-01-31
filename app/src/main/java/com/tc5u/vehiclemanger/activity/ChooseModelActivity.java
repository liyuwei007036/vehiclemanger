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
import com.tc5u.vehiclemanger.adapter.ModelAdapter;
import com.tc5u.vehiclemanger.model.CommonModel;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

public class ChooseModelActivity extends BaseActivity {

    private Long brand_id;

    private String brand_name;

    private Long series_id;

    private String series_name;

    private Long model_id;

    private String model_name;

    private TextView series;

    private RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model);
        initToolBar();
        initView();
        initData();
        getModelData();
    }

    private void initView() {
        series = findViewById(R.id.cur_model);
        recycler = findViewById(R.id.model_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
    }

    private void initData() {
        Intent intent = getIntent();
        brand_id = intent.getLongExtra("brand_id", 0L);
        brand_name = intent.getStringExtra("brand_name");
        series_id = intent.getLongExtra("series_id", 0L);
        series_name = intent.getStringExtra("series_name");
        model_id = intent.getLongExtra("model_id", 0L);
        model_name = intent.getStringExtra("model_name");
        series.setText(ObjectUtil.getString(brand_name) + " " + ObjectUtil.getString(series_name) + " " + ObjectUtil.getString(model_name));
    }

    private void getModelData() {
        JSONObject data = new JSONObject();
        data.put("series_id", series_id);
        OkHttpUtils.getInstance().PostWithJson("/getVehicleModelList", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                if (!res.getBoolean("error")) {
                    JSONArray models = res.getJSONArray("list");
                    recycler.setAdapter(new ModelAdapter(getModels(models), model_id));
                    recycler.addItemDecoration(new DividerItemDecoration(ChooseModelActivity.this, DividerItemDecoration.VERTICAL));
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

    private List<CommonModel> getModels(JSONArray data) {
        List<CommonModel> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            list.add(new CommonModel(data.getJSONObject(i)));
        }
        return list;
    }

}
