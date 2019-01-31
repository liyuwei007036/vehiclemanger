package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.BrandsAdapter;
import com.tc5u.vehiclemanger.adapter.HotBrandAdapter;
import com.tc5u.vehiclemanger.customerstyle.LettersComparator;
import com.tc5u.vehiclemanger.customerstyle.SideBarItemEntity;
import com.tc5u.vehiclemanger.customerstyle.SideBarView;
import com.tc5u.vehiclemanger.customerstyle.TitleDecoration;
import com.tc5u.vehiclemanger.model.Brand;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import core.BaseActivity;

public class ChooseBrandActivity extends BaseActivity {

    private TextView cur_brand_text, all_text, all_value;

    private RecyclerView hotRecycler, brandRecycler;

    private SideBarView mBarList;

    private BrandsAdapter adapter;

    private Toolbar mToolbarTb;

    private AppBarLayout appBar;

    private int request_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_brand);
        initView();
        initIntent();
        initData();
        initListener();
    }

    private void initView() {
        cur_brand_text = findViewById(R.id.cur_brand);
        appBar = findViewById(R.id.appBar);
        hotRecycler = findViewById(R.id.hot_recycler);
        mBarList = findViewById(R.id.bar_list);
        brandRecycler = findViewById(R.id.brand_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        hotRecycler.setLayoutManager(gridLayoutManager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        brandRecycler.setLayoutManager(linearLayoutManager);
        brandRecycler.setAdapter(adapter);
        brandRecycler.addItemDecoration(new TitleDecoration(new TitleDecoration.TitleAttributes(this)));
        brandRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mToolbarTb = findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        all_text = findViewById(R.id.all_text);
        all_value = findViewById(R.id.all_value);
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
                } else {
                    hotRecycler.getLayoutManager().scrollToPosition(0);
                }
            }
        });
    }

    private void initIntent() {
        String cur_brand = getIntent().getStringExtra("brand_name");
        cur_brand_text.setText(ObjectUtil.getString(cur_brand));
        request_code = getIntent().getIntExtra("req_code", -1);

        if (request_code != 2) {
            all_text.setVisibility(View.GONE);
            all_value.setVisibility(View.GONE);
        } else {
            all_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("brand_id", 0L);
                    intent.putExtra("brand_name", "品牌");
                    ChooseBrandActivity.this.setResult(9, intent);
                    ChooseBrandActivity.this.finish();
                }
            });
        }
    }


    private void initData() {
        // 热门品牌
        String hots = getResources().getString(R.string.hot_brands);
        JSONArray hotObjs = JSONArray.parseArray(hots);
        List<Brand> hot_brands = new ArrayList<>();
        for (int i = 0; i < hotObjs.size(); i++) {
            JSONObject brand = hotObjs.getJSONObject(i);
            hot_brands.add(new Brand(brand.getLong("id"), brand.getString("src"), brand.getString("name")));
        }
        hotRecycler.setAdapter(new HotBrandAdapter(hot_brands, request_code));

        // 全部品牌
        String brands = getResources().getString(R.string.brands);
        JSONArray brandsArray = JSONArray.parseArray(brands);
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
