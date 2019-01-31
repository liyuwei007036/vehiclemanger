package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.OutletsAdapter;
import com.tc5u.vehiclemanger.customerstyle.LettersComparator;
import com.tc5u.vehiclemanger.customerstyle.SideBarItemEntity;
import com.tc5u.vehiclemanger.customerstyle.SideBarView;
import com.tc5u.vehiclemanger.customerstyle.TitleDecoration;
import com.tc5u.vehiclemanger.model.Outlet;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import core.BaseActivity;

public class ChangeOutletActivity extends BaseActivity {

    private String cur_city, cur_outlet;
    private TextView cityText;
    private Toolbar mToolbarTb;
    private RecyclerView recyclerView;
    private OutletsAdapter adapter;
    private SideBarView mBarList;
    private Boolean is_all = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_outlet);
        Intent intent = getIntent();
        cur_city = intent.getStringExtra("city");
        cur_outlet = intent.getStringExtra("outlet");
        is_all = intent.getBooleanExtra("is_all", false);
        getCities();
        initView();
        initEvent();
        initListener();
    }

    private void initView() {
        cityText = findViewById(R.id.cur_city);
        cityText.setText(cur_outlet);
        recyclerView = findViewById(R.id.outlets);
        mBarList = findViewById(R.id.bar_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new TitleDecoration(new TitleDecoration.TitleAttributes(this)));
        recyclerView.setAdapter(adapter);
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

    }

    private void getCities() {
        String url = "/getCityList";
        JSONObject object = new JSONObject();
        object.put("isAll", is_all);
        OkHttpUtils.getInstance().PostWithJson(url, object.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                doData(res);
            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }

    private void initEvent() {
        mBarList.setOnLetterChangeListener(new SideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = adapter.getSortLettersFirstPosition(letter);
                if (position != -1) {
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        recyclerView.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
    }

    private void doData(JSONObject res) {
        HashSet<String> strings = new HashSet<>();
        List<Outlet> outlet2 = new ArrayList<>();
        for (String key : res.keySet()) {
            JSONObject city_obj = res.getJSONObject(key);
            for (String city_k : city_obj.keySet()) {
                JSONObject out_obj = city_obj.getJSONObject(city_k);
                for (String out_k : out_obj.keySet()) {
                    Long out_id = ObjectUtil.getLong(out_k);
                    String out_name = out_obj.getString(out_k);
                    String f = PinyinUtils.getFirstSpell(out_name).substring(0, 1).toUpperCase();
                    // 卖场
                    if (!strings.contains(f)) strings.add(f);
                    Outlet out = new Outlet();
                    out.setId(out_id);
                    out.setIs_letter(false);
                    out.setLetter(f);
                    out.setName(out_name);
                    out.setIs_select(ObjectUtil.getString(cur_outlet).equals(out_name));
                    outlet2.add(out);
                }
            }
        }

        List<String> ss = new ArrayList<>(strings);
        Collections.sort(ss);
        mBarList.setMLetters(ss);
        List<SideBarItemEntity<Outlet>> dateList = fillData(outlet2);
        Collections.sort(dateList, new LettersComparator());
        recyclerView.setAdapter(adapter = new OutletsAdapter(dateList, is_all));
    }

    private List<SideBarItemEntity<Outlet>> fillData(List<Outlet> outlets) {
        List<SideBarItemEntity<Outlet>> sortList = new ArrayList<>();
        for (Outlet aDate : outlets) {
            SideBarItemEntity<Outlet> item = new SideBarItemEntity<>();
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
