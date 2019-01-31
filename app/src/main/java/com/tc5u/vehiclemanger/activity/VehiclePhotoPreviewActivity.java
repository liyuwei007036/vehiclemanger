package com.tc5u.vehiclemanger.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.VehiclePhotoViewAdapter;
import com.tc5u.vehiclemanger.customerstyle.PhotoViewPager;
import com.tc5u.vehiclemanger.model.VehiclePhoto;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.List;

import core.BaseActivity;

public class VehiclePhotoPreviewActivity extends BaseActivity {

    private List<VehiclePhoto> images;
    private PhotoViewPager mViewPager;
    private VehiclePhotoViewAdapter adapter;
    private int currentPosition;

    private TextView delete, mark_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_photo_preview);
        initIntent();
        initView();
        initData();
    }

    private void initIntent() {
        images = (List<VehiclePhoto>) getIntent().getSerializableExtra("images");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mark_main = findViewById(R.id.mark_main);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto();
            }
        });

        mark_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markMainPhoto();
            }
        });
    }

    public void initData() {
        adapter = new VehiclePhotoViewAdapter(images, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        VehiclePhoto photo = images.get(currentPosition);
        if (photo.getIs_main()) mark_main.setVisibility(View.GONE);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                VehiclePhoto photo = images.get(position);
                if (ObjectUtil.getBoolean(photo.getIs_main())) {
                    mark_main.setVisibility(View.GONE);
                } else {
                    mark_main.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void deletePhoto() {
        final VehiclePhoto photo = images.get(currentPosition);
        JSONObject data = new JSONObject();
        data.put("vid", photo.getVid());
        data.put("uuid", photo.getUuid());
        OkHttpUtils.getInstance().PostWithJson("/removeVehiclePhoto", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                    images.remove(photo);
                    if (images.size() == 0) {
                        finish();
                    }
                    adapter.notifyDataSetChanged();
                    currentPosition -= 1;
                    if (currentPosition < 0) currentPosition = 0;
                    mViewPager.setCurrentItem(currentPosition, false);
                    showToast("删除成功");
                } else {
                    showToast(object.getString("msg"));
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


    private void markMainPhoto() {
        final VehiclePhoto photo = images.get(currentPosition);
        JSONObject data = new JSONObject();
        data.put("vid", photo.getVid());
        data.put("uuid", photo.getUuid());
        OkHttpUtils.getInstance().PostWithJson("/markVehiclePhotoMainPhoto", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                    changeMainPhoto();
                    photo.setIs_main(true);
                    adapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(currentPosition, false);
                    mark_main.setVisibility(View.GONE);
                    showToast("标记成功");
                } else {
                    showToast(object.getString("msg"));
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

    private void changeMainPhoto() {
        for (VehiclePhoto photo : images) {
            photo.setIs_main(false);
        }
    }
}
