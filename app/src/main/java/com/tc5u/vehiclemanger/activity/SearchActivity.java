package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.SearchHistoryAdapter;
import com.tc5u.vehiclemanger.customerstyle.ClearableAutoCompleteTextView;
import com.tc5u.vehiclemanger.model.SearchHistory;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.StringUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import core.BaseActivity;

public class SearchActivity extends BaseActivity {

    private Toolbar mToolbarTb;

    private String keyWord;

    private ClearableAutoCompleteTextView searchView;

    private List<SearchHistory> histories = getAllSearchHistory();

    private RecyclerView history_recycler;

    private SearchHistoryAdapter searchHistoryAdapter;
    private LinearLayout history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_acitivity);
        initIntent();
        initView();
        initListener();
    }

    private void initView() {
        mToolbarTb = findViewById(R.id.tb_toolbar);
        searchView = findViewById(R.id.search_view);
        history_recycler = findViewById(R.id.history_recycler);
        history = findViewById(R.id.history_view);
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        searchHistoryAdapter = new SearchHistoryAdapter(histories);
        history_recycler.setLayoutManager(manager);
        history_recycler.setAdapter(searchHistoryAdapter);
        searchView.setAdapter(new ArrayAdapter<>(this, R.layout.auto_complete_item, initSearchData()));
        if (StringUtils.isNotEmpty(keyWord)) searchView.setText(keyWord);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (histories == null || histories.size() < 1) {
            history.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //TODO回车键按下时要执行的操作
                    String history = searchView.getText().toString();
                    saveHistory(history);
                }
                return false;
            }
        });

    }

    private void initIntent() {
        keyWord = getIntent().getStringExtra("keyWord");
    }

    public void deleteAllHistory(View view) {
        LitePal.deleteAll(SearchHistory.class);
        histories.clear();
        history.setVisibility(View.GONE);
        searchHistoryAdapter.notifyDataSetChanged();
    }

    private List<String> initSearchData() {
        String brands = getResources().getString(R.string.brands);
        JSONArray brandsArray = JSONArray.parseArray(brands);
        List<String> brandList = new ArrayList<>();
        for (int i = 0; i < brandsArray.size(); i++) {
            JSONObject brand = brandsArray.getJSONObject(i);
            String brand_name = ObjectUtil.getString(brand.getString("name"));
            if (StringUtils.isEmpty(brand_name)) continue;
            brandList.add(brand_name);
        }
        return brandList;
    }

    public void doSearch(View view) {
        String history = searchView.getText().toString();
        saveHistory(history);
        searchHistoryAdapter.notifyDataSetChanged();
    }


    private List<SearchHistory> getAllSearchHistory() {
        return LitePal.order("search_date desc").find(SearchHistory.class);
    }

    public void saveHistory(String history) {
        SearchHistory searchHistory = get(history);
        if (searchHistory == null) {
            if (StringUtils.isNotEmpty(history)) {
                searchHistory = new SearchHistory();
                searchHistory.setValue(history);
                searchHistory.setSearch_date(new Date());
                searchHistory.save();
            }
        } else {
            searchHistory.setSearch_date(new Date());
            searchHistory.update(searchHistory.getId());
        }
        Intent intent = new Intent();
        intent.putExtra("keyWord", history);
        SearchActivity.this.setResult(6, intent);
        SearchActivity.this.finish();
    }

    private SearchHistory get(String value) {
        return LitePal.where("value=?", value).findFirst(SearchHistory.class);
    }
}
