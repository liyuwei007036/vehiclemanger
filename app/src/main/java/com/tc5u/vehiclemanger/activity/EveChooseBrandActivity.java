package com.tc5u.vehiclemanger.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.BrandsAdapter;
import com.tc5u.vehiclemanger.customerstyle.LettersComparator;
import com.tc5u.vehiclemanger.customerstyle.SideBarItemEntity;
import com.tc5u.vehiclemanger.customerstyle.SideBarView;
import com.tc5u.vehiclemanger.customerstyle.TitleDecoration;
import com.tc5u.vehiclemanger.model.Brand;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import core.BaseActivity;

public class EveChooseBrandActivity extends BaseActivity {


    private TextView cur_brand_text;

    private RecyclerView brandRecycler;

    private SideBarView mBarList;

    private BrandsAdapter adapter;

    private Toolbar mToolbarTb;

    private AppBarLayout appBar;

    private int request_code;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eve_choose_brand);
        initView();
        initListener();
        initIntent();
        getBrands();
    }

    private void initView() {
        cur_brand_text = findViewById(R.id.cur_brand);
        appBar = findViewById(R.id.appBar);
        mBarList = findViewById(R.id.bar_list);
        brandRecycler = findViewById(R.id.brand_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        brandRecycler.setLayoutManager(linearLayoutManager);
        brandRecycler.setAdapter(adapter);
        brandRecycler.addItemDecoration(new TitleDecoration(new TitleDecoration.TitleAttributes(this)));
        brandRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mToolbarTb = findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListener() {
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBarList.setOnLetterChangeListener(new SideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = adapter.getSortLettersFirstPosition(letter);
                if (position != -1) {
                    if (brandRecycler.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) brandRecycler.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                        appBar.setExpanded(false, false);
                    } else {
                        brandRecycler.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
    }

    private void initIntent() {
        String cur_brand = getIntent().getStringExtra("brand_name");
        cur_brand_text.setText(ObjectUtil.getString(cur_brand));
        request_code = getIntent().getIntExtra("req_code", -1);

    }


    private void getBrands() {
        dialog = loadAlertDialog(null, "品牌加载中...");
        OkHttpUtils.getInstance().PostWithJson("/getBrands", "", new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {

            }

            @Override
            public void onLoadingShow() {
                dialog.show();
            }

            @Override
            public void onLoadingDismiss() {
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (ObjectUtil.getBoolean(res.getBoolean("valid"))) {
                    JSONObject o = res.getJSONObject("data");
                    JSONArray brandsArray = new JSONArray();
                    for (String key : o.keySet()) {
                        brandsArray.addAll(o.getJSONArray(key));
                    }
                    List<Brand> brandList = new ArrayList<>();
                    for (int i = 0; i < brandsArray.size(); i++) {
                        JSONObject brand = brandsArray.getJSONObject(i);
                        brandList.add(new Brand(brand.getLong("id"), brand.getString("src"), brand.getString("name")));
                    }
                    List<SideBarItemEntity<Brand>> dateList = fillData(brandList);
                    Collections.sort(dateList, new LettersComparator());
                    brandRecycler.setAdapter(adapter = new BrandsAdapter(dateList, request_code));
                    List<String> letters = Arrays.asList(getResources().getStringArray(R.array.brand_index_search));
                    Collections.sort(letters);
                    mBarList.setMLetters(letters);
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


    private List<SideBarItemEntity<Brand>> fillData(List<Brand> brands) {
        List<SideBarItemEntity<Brand>> sortList = new ArrayList<>();
        for (Brand aDate : brands) {
            SideBarItemEntity<Brand> item = new SideBarItemEntity<>();
            item.setValue(aDate);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(aDate.getName());
            //取第一个首字母
            String letters = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (letters.matches("[A-Z]")) {
                item.setSortLetters(letters.toUpperCase());
            } else {
                item.setSortLetters("#");
            }
            sortList.add(item);
        }
        return sortList;
    }
}

