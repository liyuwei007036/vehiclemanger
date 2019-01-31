package com.tc5u.vehiclemanger.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import core.BaseActivity;

public class VehicleEvaluatePriceResultActivity extends BaseActivity {

    private TextView car_name, cszdj, city, registerDate, distance, desc, price1, price2, price3, vehicle_manager_price, price1_desc, price2_desc, price3_desc;

    private LinearLayout part1, part2, part3;

    private RadioButton button1, button2, button3;

    private Long brand_id, series_id, model_id, province_id, city_id;

    private String brand_name, series_name, model_name, province_name, city_name, date;

    private Double v_distance, ncmsrp;

    private JSONObject a, b, c;

    private String a_desc = "商家销售价表示车商对个人出售车辆的价格";
    private String b_desc = "个人收购价表示个人对个人收车价";
    private String c_desc = "商家收购价表示车商对个人收车价";

    private LinearLayout cur_part;

    private RadioButton cur_button;

    private ProgressDialog dialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setData2View();
                    dialog.dismiss();
                    break;
                case 2: {
                    dialog.dismiss();
                    showToast("数据加载失败");
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_evaluate_price_result);
        initToolBar();
        initView();
        initIntent();
        getEvaluateInfo();
        initListener();
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

    private void initView() {
        car_name = findViewById(R.id.name);
        city = findViewById(R.id.city);
        registerDate = findViewById(R.id.registerDate);
        distance = findViewById(R.id.distance);
        desc = findViewById(R.id.desc);
        price1 = findViewById(R.id.price1);
        price2 = findViewById(R.id.price2);
        price3 = findViewById(R.id.price3);
        vehicle_manager_price = findViewById(R.id.vehicle_manager_price);
        part1 = findViewById(R.id.part1);
        part2 = findViewById(R.id.part2);
        part3 = findViewById(R.id.part3);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        cszdj = findViewById(R.id.cszdj);
        price1_desc = findViewById(R.id.price1_desc);
        price2_desc = findViewById(R.id.price2_desc);
        price3_desc = findViewById(R.id.price3_desc);
        cur_button = button1;
        cur_part = part1;
    }

    public void initIntent() {
        Intent intent = getIntent();
        brand_id = intent.getLongExtra("brand_id", 0L);
        series_id = intent.getLongExtra("series_id", 0L);
        model_id = intent.getLongExtra("model_id", 0L);
        province_id = intent.getLongExtra("provinceId", 0L);
        city_id = intent.getLongExtra("cityId", 0L);
        brand_name = intent.getStringExtra("brandName");
        series_name = intent.getStringExtra("series_name");
        model_name = intent.getStringExtra("model_name");
        city_name = intent.getStringExtra("city_name");
        province_name = intent.getStringExtra("province_name");
        v_distance = intent.getDoubleExtra("distance", 0d);
        date = intent.getStringExtra("registerDate");
    }

    public void getEvaluateInfo() {
        dialog = loadAlertDialog(null, "评估中...");
        dialog.show();
        JSONObject data = new JSONObject();
        data.put("modelId", model_id);
        data.put("distance", v_distance);
        data.put("registerDate", date);
        data.put("cityId", city_id);
        data.put("provinceId", province_id);
        OkHttpUtils.getInstance().PostWithJson("/getEvaluateInfo", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                if (res.getBoolean("valid") != null && res.getBoolean("valid")) {
                    res = res.getJSONObject("data");
                    a = res.getJSONObject("b2CPrices");
                    b = res.getJSONObject("c2CPrices");
                    c = res.getJSONObject("c2BPrices");
                    ncmsrp = res.getDouble("ncmsrp");
                    mHandler.sendEmptyMessage(1);
                } else {
                    dialog.dismiss();
                    DiaLogUtils.alertError(VehicleEvaluatePriceResultActivity.this, "错误", res.getString("message"), new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {
                            finish();
                        }

                        @Override
                        public void fail() {

                        }
                    });
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

    private void setData2View() {
        car_name.setText(ObjectUtil.getString(brand_name) + " " + ObjectUtil.getString(series_name) + " " + ObjectUtil.getString(model_name));
        cszdj.setText(ncmsrp + "万元");
        city.setText(city_name);
        registerDate.setText(date);
        distance.setText(v_distance + "万公里");
        price3.setText(a.getJSONObject("a").getString("mid") + " 万");
        price2.setText(a.getJSONObject("b").getString("mid") + " 万");
        price1.setText(a.getJSONObject("c").getString("mid") + " 万");
        vehicle_manager_price.setText(a.getJSONObject("a").getString("low") + " ~ " + a.getJSONObject("a").getString("up") + " 万");
        desc.setText(a_desc);
    }

    public void initListener() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_button = button1;
                if (cur_part == part1) {
                    price3.setText(a.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(a.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(a.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(a.getJSONObject("a").getString("low") + " ~ " + a.getJSONObject("a").getString("up") + " 万");
                    desc.setText(a_desc);
                } else if (cur_part == part2) {
                    price3.setText(a.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(a.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(a.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(a.getJSONObject("b").getString("low") + " ~ " + a.getJSONObject("b").getString("up") + " 万");
                    desc.setText(a_desc);
                } else if (cur_part == part3) {
                    price3.setText(a.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(a.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(a.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(a.getJSONObject("c").getString("low") + " ~ " + a.getJSONObject("c").getString("up") + " 万");
                    desc.setText(a_desc);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_button = button2;
                if (cur_part == part1) {
                    price3.setText(b.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(b.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(b.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(b.getJSONObject("a").getString("low") + " ~ " + b.getJSONObject("a").getString("up") + " 万");
                    desc.setText(b_desc);
                } else if (cur_part == part2) {
                    price3.setText(b.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(b.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(b.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(a.getJSONObject("b").getString("low") + " ~ " + a.getJSONObject("b").getString("up") + " 万");
                    desc.setText(b_desc);
                } else if (cur_part == part3) {
                    price3.setText(b.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(b.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(b.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(b.getJSONObject("c").getString("low") + " ~ " + b.getJSONObject("c").getString("up") + " 万");
                    desc.setText(b_desc);
                }
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_button = button3;
                if (cur_part == part1) {
                    price3.setText(c.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(c.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(c.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(c.getJSONObject("a").getString("low") + " ~ " + c.getJSONObject("a").getString("up") + " 万");
                    desc.setText(c_desc);
                } else if (cur_part == part2) {
                    price3.setText(c.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(c.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(c.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(c.getJSONObject("b").getString("low") + " ~ " + c.getJSONObject("b").getString("up") + " 万");
                    desc.setText(c_desc);
                } else if (cur_part == part3) {
                    price3.setText(c.getJSONObject("a").getString("mid") + " 万");
                    price2.setText(c.getJSONObject("b").getString("mid") + " 万");
                    price1.setText(c.getJSONObject("c").getString("mid") + " 万");
                    vehicle_manager_price.setText(c.getJSONObject("c").getString("low") + " ~ " + c.getJSONObject("c").getString("up") + " 万");
                    desc.setText(c_desc);
                }
            }
        });


        part1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_part = part1;
                part1.setBackgroundColor(getResources().getColor(R.color.tl));
                price1_desc.setTextColor(getResources().getColor(R.color.white));
                part2.setBackgroundColor(getResources().getColor(R.color.eee));
                price2_desc.setTextColor(getResources().getColor(R.color.black));
                part3.setBackgroundColor(getResources().getColor(R.color.eee));
                price3_desc.setTextColor(getResources().getColor(R.color.black));
                price1.setTextColor(getResources().getColor(R.color.white));
                price2.setTextColor(getResources().getColor(R.color.black));
                price3.setTextColor(getResources().getColor(R.color.black));
                if (cur_button == button1) {
                    vehicle_manager_price.setText(a.getJSONObject("a").getString("low") + " ~ " + a.getJSONObject("a").getString("up") + " 万");
                } else if (cur_button == button2) {
                    vehicle_manager_price.setText(b.getJSONObject("a").getString("low") + " ~ " + b.getJSONObject("a").getString("up") + " 万");
                } else if (cur_button == button3) {
                    vehicle_manager_price.setText(c.getJSONObject("a").getString("low") + " ~ " + c.getJSONObject("a").getString("up") + " 万");
                }
            }
        });


        part2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_part = part2;
                part2.setBackgroundColor(getResources().getColor(R.color.tl));
                price2_desc.setTextColor(getResources().getColor(R.color.white));
                part1.setBackgroundColor(getResources().getColor(R.color.eee));
                price1_desc.setTextColor(getResources().getColor(R.color.black));
                part3.setBackgroundColor(getResources().getColor(R.color.eee));
                price3_desc.setTextColor(getResources().getColor(R.color.black));

                price1.setTextColor(getResources().getColor(R.color.black));
                price2.setTextColor(getResources().getColor(R.color.white));
                price3.setTextColor(getResources().getColor(R.color.black));
                if (cur_button == button1) {
                    vehicle_manager_price.setText(a.getJSONObject("b").getString("low") + " ~ " + a.getJSONObject("b").getString("up") + " 万");
                } else if (cur_button == button2) {
                    vehicle_manager_price.setText(b.getJSONObject("b").getString("low") + " ~ " + b.getJSONObject("b").getString("up") + " 万");
                } else if (cur_button == button3) {
                    vehicle_manager_price.setText(c.getJSONObject("b").getString("low") + " ~ " + c.getJSONObject("b").getString("up") + " 万");
                }
            }
        });


        part3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_part = part3;
                part3.setBackgroundColor(getResources().getColor(R.color.tl));
                price3_desc.setTextColor(getResources().getColor(R.color.white));
                price3.setTextColor(getResources().getColor(R.color.white));

                price1.setTextColor(getResources().getColor(R.color.black));
                part1.setBackgroundColor(getResources().getColor(R.color.eee));
                price1_desc.setTextColor(getResources().getColor(R.color.black));

                part2.setBackgroundColor(getResources().getColor(R.color.eee));
                price2_desc.setTextColor(getResources().getColor(R.color.black));
                price2.setTextColor(getResources().getColor(R.color.black));

                if (cur_button == button1) {
                    vehicle_manager_price.setText(a.getJSONObject("c").getString("low") + " ~ " + a.getJSONObject("c").getString("up") + " 万");
                } else if (cur_button == button2) {
                    vehicle_manager_price.setText(b.getJSONObject("c").getString("low") + " ~ " + b.getJSONObject("c").getString("up") + " 万");
                } else if (cur_button == button3) {
                    vehicle_manager_price.setText(c.getJSONObject("c").getString("low") + " ~ " + c.getJSONObject("c").getString("up") + " 万");
                }
            }
        });
    }

    public void finish(View view) {
        finish();
    }
}
