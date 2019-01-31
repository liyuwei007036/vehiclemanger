package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.CheckBoxAdapter;
import com.tc5u.vehiclemanger.customerstyle.AlertDialog;
import com.tc5u.vehiclemanger.model.SpinnerModel;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

public class VehiclePublishActivity extends BaseActivity {

    private Long vehicle_id;

    private TextView department;

    private RecyclerView tag1_view, tag2_view;

    private CheckBoxAdapter adapter1, adapter2;

    private List<SpinnerModel> models;

    private Long dep_id;

    private TextView err1, err2;

    private OptionsPickerView<SpinnerModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_publish);
        initToolBar();
        initIntent();
        initView();
        getDepartments();
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
    }

    private void initIntent() {
        Intent intent = getIntent();
        vehicle_id = intent.getLongExtra("vehicle_id", 0L);
    }

    private void initView() {
        department = findViewById(R.id.deps);
        tag1_view = findViewById(R.id.tag_1);
        LinearLayoutManager manager = new LinearLayoutManager(VehiclePublishActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter1 = new CheckBoxAdapter(initTag1(), VehiclePublishActivity.this, 0);
        tag1_view.setAdapter(adapter1);
        tag1_view.setLayoutManager(manager);
        tag1_view.addItemDecoration(new DividerItemDecoration(VehiclePublishActivity.this, LinearLayoutManager.VERTICAL));

        tag2_view = findViewById(R.id.tag_2);
        adapter2 = new CheckBoxAdapter(initTag2(), VehiclePublishActivity.this, 2);
        tag2_view.setAdapter(adapter2);
        LinearLayoutManager manager2 = new LinearLayoutManager(VehiclePublishActivity.this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        tag2_view.setLayoutManager(manager2);
        tag2_view.addItemDecoration(new DividerItemDecoration(VehiclePublishActivity.this, LinearLayoutManager.VERTICAL));

        err1 = findViewById(R.id.error_msg1);
        err2 = findViewById(R.id.error_msg2);
    }

    /**
     * 获取发起部门
     */
    private void getDepartments() {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        okHttpUtils.PostWithJson("/getDepartments", "", new OkHttpUtils.ResultCallBackListener() {
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
                JSONArray object = JSONArray.parseArray(result_str);
                models = new ArrayList<>();
                for (int i = 0; i < object.size(); i++) {
                    models.add(new SpinnerModel(object.getJSONObject(i)));
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

    private List<SpinnerModel> initTag1() {
        List<SpinnerModel> list = new ArrayList<>();
        list.add(new SpinnerModel("畅销车", false));
        list.add(new SpinnerModel("练手车", false));
        list.add(new SpinnerModel("保值车", false));
        list.add(new SpinnerModel("商务座驾", false));
        return list;
    }

    private List<SpinnerModel> initTag2() {
        List<SpinnerModel> list = new ArrayList<>();
        list.add(new SpinnerModel("低首付", false));
        list.add(new SpinnerModel("无户籍限制", false));
        list.add(new SpinnerModel("超长车龄", false));
        list.add(new SpinnerModel("低利率", false));
        list.add(new SpinnerModel("轻松贷车", false));
        list.add(new SpinnerModel("无担保", false));
        list.add(new SpinnerModel("全场代办", false));
        return list;
    }

    public void submitPublishSheet(View view) {
        Boolean can_submit = true;
        if (ObjectUtil.getLong(dep_id) < 1) {
            new AlertDialog(VehiclePublishActivity.this).builder().setMsg("必须选择发起部门")
                    .setTitle("提示")
                    .setPositiveButton("确定", getResources().getColor(R.color.red), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .show();
            can_submit = false;
        }

        List<String> tag1 = adapter1.getSelectName();
        List<String> tag2 = adapter2.getSelectName();

        if (tag1.size() < 1) {
            can_submit = false;
            err1.setVisibility(View.VISIBLE);
        } else {
            err1.setVisibility(View.GONE);
        }

        if (tag2.size() < 1) {
            can_submit = false;
            err2.setVisibility(View.VISIBLE);
        } else {
            err2.setVisibility(View.GONE);
        }
        if (can_submit) doPublish(tag1, tag2);
    }

    private void doPublish(List<String> tag1, List<String> tag2) {
        String url = "/vehiclePublish";
        JSONObject data = new JSONObject();
        JSONObject dep = new JSONObject();
        dep.put("id", dep_id);
        data.put("dept", dep.toJSONString());
        data.put("v_id", vehicle_id);
        JSONArray array = new JSONArray();
        for (String s : tag1) {
            JSONObject object = new JSONObject();
            object.put("name", s);
            object.put("checked", true);
            array.add(object);
        }
        data.put("indexTagsItems", array);
        JSONArray array2 = new JSONArray();
        for (String s : tag2) {
            JSONObject object = new JSONObject();
            object.put("name", s);
            object.put("checked", true);
            array2.add(object);
        }
        data.put("detailTagsItems", array2);
        OkHttpUtils.getInstance().PostWithJson(url, data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                JSONObject object = JSONObject.parseObject(result_str);
                if (object.getInteger("status") == 0) {
                    new AlertDialog(VehiclePublishActivity.this).builder().setMsg("已成功提交上架申请")
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.wxLv), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                } else {

                    new AlertDialog(VehiclePublishActivity.this).builder().setMsg(object.getString("msg"))
                            .setTitle("提示")
                            .setPositiveButton("确定", getResources().getColor(R.color.red), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                showToast("onFailure");
                e.printStackTrace();
            }

            @Override
            public void onError(String url, String result_str) {
                showToast(result_str);
            }
        });
    }

    public void chooseDepartment(View view) {
        initOptionPicker();
    }

    private void initOptionPicker() {
        options = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                SpinnerModel model = models.get(options1);
                dep_id = ObjectUtil.getLong(model.getValue());
                department.setText(model.getName());
            }
        }).setTitleText("部门选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .setSubmitText("确定")//确认按钮文字
                .setCancelText("取消")//取消按钮文字
                .setBgColor(Color.WHITE)
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.BLACK)
                .setCancelColor(getResources().getColor(R.color.text_color))//取消按钮文字颜色
                .setSubmitColor(getResources().getColor(R.color.wxLv))
                .setTextColorCenter(Color.BLACK)
                .setTitleSize(16)
                .setContentTextSize(16)
                .setSubCalSize(16)
                .setContentTextSize(14)
                .setLineSpacingMultiplier(2f)
                .build();
        options.setPicker(models);
        options.show();
    }

}
