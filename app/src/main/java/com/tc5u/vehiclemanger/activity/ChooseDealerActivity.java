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
import com.tc5u.vehiclemanger.adapter.DealerAdapter;
import com.tc5u.vehiclemanger.model.CommonModel;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;


import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

/**
 * 选择商户
 */
public class ChooseDealerActivity extends BaseActivity {

    private TextView dealer;

    private RecyclerView recycler;

    private Long dealer_id;

    private String dealer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dealer);
        initView();
        initToolBar();
        initData();
        getDealerList();
    }


    private void initData() {
        Intent intent = getIntent();
        dealer_id = intent.getLongExtra("dealer_id", 0L);
        dealer_name = intent.getStringExtra("dealer_name");
        dealer.setText(ObjectUtil.getString(dealer_name));
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
        dealer = findViewById(R.id.cur_dealer);
        recycler = findViewById(R.id.dealer_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
    }


    private void getDealerList() {
        OkHttpUtils.getInstance().PostWithJson("/getDealers", "", new OkHttpUtils.ResultCallBackListener() {
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
                    List<CommonModel> list = new ArrayList<>();
                    JSONArray dealers = res.getJSONArray("dealers");
                    for (int i = 0; i < dealers.size(); i++) {
                        list.add(new CommonModel(dealers.getJSONObject(i)));
                    }
                    recycler.setAdapter(new DealerAdapter(list, dealer_id));
                    recycler.addItemDecoration(new DividerItemDecoration(ChooseDealerActivity.this, DividerItemDecoration.VERTICAL));

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
}
