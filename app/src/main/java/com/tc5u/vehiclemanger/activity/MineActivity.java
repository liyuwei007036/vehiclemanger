package com.tc5u.vehiclemanger.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.utils.CheckUpdateUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import core.ActivityController;
import core.BaseActivity;

public class MineActivity extends BaseActivity {

    private TextView act, plan, user_name, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();
        getPersonalVehicleInfo();
    }

    private void initView() {
        act = findViewById(R.id.act);
        plan = findViewById(R.id.plan);
        user_name = findViewById(R.id.user_name);
        exit = findViewById(R.id.exit);

        TextView update = findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdate();
            }
        });

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_mine);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        if (item.isChecked()) return false;
                        item.setChecked(true);
                        switch (item.getItemId()) {
                            case R.id.item_home:
                                startActivity(null, IndexActivity.class);
                                finish();
                                break;
                            case R.id.item_upload:
                                startActivity(null, UploadVehicleActivity.class);
                                finish();
                                break;

                        }
                        return false;
                    }
                });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityController.finishAll();
            }
        });
    }


    public void getPersonalVehicleInfo() {
        OkHttpUtils.getInstance().PostWithJson("/getPersonalVehicleInfo", "", new OkHttpUtils.ResultCallBackListener() {
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
                act.setText(res.getString("total"));
                user_name.setText(res.getString("user_name"));
                plan.setText(res.getString("plan_total"));
            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }

    private void checkUpdate() {
        CheckUpdateUtil checkUpdateUtil = CheckUpdateUtil.getInstance();
        Boolean need_update = checkUpdateUtil.checkUpdate(this);
        if (need_update) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, mPermissionList, 100);
            checkUpdateUtil.showUpdateDialog(this);
        } else {
            Toast.makeText(this, "当前为最新版本", Toast.LENGTH_SHORT).show();
        }
    }

}
