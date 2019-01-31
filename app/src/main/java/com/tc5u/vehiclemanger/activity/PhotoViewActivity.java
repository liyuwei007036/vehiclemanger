package com.tc5u.vehiclemanger.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.PhotoViewAdapter;
import com.tc5u.vehiclemanger.customerstyle.PhotoViewPager;
import com.tc5u.vehiclemanger.model.SystemImages;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import core.BaseActivity;

/**
 * 图片预览
 */
public class PhotoViewActivity extends BaseActivity {

    private List<SystemImages> images;
    private PhotoViewPager mViewPager;
    private TextView mTvImageCount;
    private PhotoViewAdapter adapter;
    private int currentPosition;
    private TextView crop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        initIntent();
        initView();
        initData();
    }


    private void initIntent() {
        images = (List<SystemImages>) getIntent().getSerializableExtra("images");
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mTvImageCount = findViewById(R.id.mTvImageCount);
        crop = findViewById(R.id.crop);
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sourceUri = Uri.fromFile(new File(images.get(currentPosition).getPath()));
                Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "TC5U-" + System.currentTimeMillis() + ".jpg"));
                UCrop ucrop = UCrop.of(sourceUri, destinationUri);

                //初始化UCrop配置
                UCrop.Options options = new UCrop.Options();
                //设置裁剪图片可操作的手势
                options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
                //是否隐藏底部容器，默认显示
                options.setHideBottomControls(true);
                //设置toolbar颜色
                options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                //设置状态栏颜色
                options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                //是否能调整裁剪框
                options.setFreeStyleCropEnabled(false);
                ucrop.withAspectRatio(4, 3);
                //UCrop配置
                ucrop.withOptions(options);
                ucrop.start(PhotoViewActivity.this);
            }
        });
        TextView sure = findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("images", (Serializable) images);
                setResult(200, intent);
                finish();
            }
        });
    }

    public void initData() {
        adapter = new PhotoViewAdapter(images, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + images.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + images.size());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            SystemImages cur_img = images.get(currentPosition);
            cur_img.setIs_crop(true);
            cur_img.setCrop_path(resultUri.getPath());
            adapter.notifyDataSetChanged();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("images", (Serializable) images);
        setResult(300, intent);
        finish();
    }
}
