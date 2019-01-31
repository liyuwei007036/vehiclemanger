package com.tc5u.vehiclemanger.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.MapModel;
import com.tc5u.vehiclemanger.utils.CheckUpdateUtil;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import org.litepal.LitePal;

import core.BaseActivity;
import core.BaseApplication;

/**
 * 登陆
 */
public class LoginActivity extends BaseActivity {

    private EditText account, pwd;

    private TextView error_msg;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = findViewById(R.id.account);
        pwd = findViewById(R.id.pwd);
        error_msg = findViewById(R.id.error_msg);
        Intent intent = getIntent();
        if (intent != null) {
            error_msg.setVisibility(View.VISIBLE);
            error_msg.setText(intent.getStringExtra("msg"));
        }

        MapModel account_model = getMap("account");
        if (account_model != null) {
            account.setText(ObjectUtil.getString(account_model.getValue()));
        }

        MapModel pwd_model = getMap("password");
        if (pwd_model != null) {
            pwd.setText(ObjectUtil.getString(pwd_model.getValue()));
        }
    }

    public void doLogin(View view) {
        dialog = loadAlertDialog(null, "登陆中...");
        final String ac = ObjectUtil.getString(account.getText());
        final String pw = ObjectUtil.getString(pwd.getText());

        if (StringUtils.isEmpty(ac)) {
            error_msg.setText("请输入账号");
            error_msg.setVisibility(View.VISIBLE);
            return;
        }

        if (StringUtils.isEmpty(pw)) {
            error_msg.setText("请输入密码");
            error_msg.setVisibility(View.VISIBLE);
            return;
        }

        JSONObject object = new JSONObject();
        object.put("pwd", pw);
        object.put("account", ac);
        String url = "/login";

        OkHttpUtils.getInstance().PostWithJson(url, object.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                if (res.getInteger("status") == 0) {
                    BaseApplication application = BaseApplication.getInstance();
                    application.user_id = res.getLong("user_id");
                    application.user_name = res.getString("user_name");
                    startActivity(null, IndexActivity.class);
                    MapModel model = getMap("account");
                    if (model == null) {
                        model = new MapModel();
                        model.setKey("account");
                        model.setValue(ac);
                        model.save();
                    } else {
                        model.setValue(ac);
                        model.update(model.getId());
                    }

                    MapModel model2 = getMap("password");
                    if (model2 == null) {
                        model2 = new MapModel();
                        model2.setKey("password");
                        model2.setValue(pw);
                        model2.save();
                    } else {
                        model2.setValue(pw);
                        model2.update(model2.getId());
                    }
                    finish();
                } else {
                    error_msg.setText(res.getString("msg"));
                    error_msg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                showToast("登陆失败");
            }

            @Override
            public void onError(String url, String result_str) {
                showToast("登陆失败");
            }
        });

    }

    private MapModel getMap(String key) {
        return LitePal.where("key=?", key).findFirst(MapModel.class);
    }

}
