package com.tc5u.vehiclemanger.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.AlbumAdapter;
import com.tc5u.vehiclemanger.model.SystemImages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

/**
 * 相册
 */
public class AlbumActivity extends BaseActivity {

    private List<SystemImages> images = new ArrayList<>();

    private List<SystemImages> select_images = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    private Integer maxCount;

    private TextView sure;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
            switch (msg.what) {
                case 1:
                    //关闭进度条
                    adapter = new AlbumAdapter(images, maxCount, new AlbumAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClickListener(SystemImages images) {
                            if (images.getIs_select()) {
                                select_images.add(images);
                            } else {
                                select_images.remove(images);
                            }
                            sure.setText("确定（" + select_images.size() + "/" + maxCount + ")");
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private RecyclerView recyclerView;

    private AlbumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        initIntent();
        initToolBar();
        initView();
        getAuth();
    }

    private void initIntent() {
        maxCount = getIntent().getIntExtra("maxCount", 1);
    }

    private void initView() {
        recyclerView = findViewById(R.id.images);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);
        sure = findViewById(R.id.sure);
        sure.setText("确定(" + select_images.size() + "/" + maxCount + ")");


        // 预览按钮点击事件
        TextView pev = findViewById(R.id.pev);
        pev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_images.size() > 0) {
                    Intent intent = new Intent(AlbumActivity.this, PhotoViewActivity.class);
                    intent.putExtra("images", (Serializable) select_images);
                    startActivityForResult(intent, 200);
                }
            }
        });

        //确定按钮点击事件
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_images.size() > 0) {
                    Intent intent = new Intent(AlbumActivity.this, PhotoViewActivity.class);
                    intent.putExtra("images", (Serializable) select_images);
                    setResult(2, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
    private void getPhotos() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        //显示进度条
        mProgressDialog = mProgressDialog.show(AlbumActivity.this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AlbumActivity.this.getContentResolver();

                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg"},
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    Long size = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)); // 大小byte

                    Double weight = mCursor.getDouble(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH));

                    Double height = mCursor.getDouble(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));

                    String type = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));

                    Long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID));

                    String title = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.TITLE));

                    SystemImages image = new SystemImages();
                    image.setHeight(height);
                    image.setId(id);
                    image.setIs_select(false);
                    image.setPath(path);
                    image.setSize(size);
                    image.setWeight(weight);
                    image.setType(type);
                    image.setTitle(title);
                    image.setPosition(images.size());
                    images.add(image);
                }
                mCursor.close();
                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(1);

            }
        }).start();
    }

    // 动态获取权限
    private void getAuth() {
        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, mPermissionList, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                getPhotos();
            } else {
                Toast.makeText(this, "权限不足: 无法访问系统图片", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initToolBar() {
        Toolbar mToolbarTb = findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(-100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == 200) {
            // 点击了完成 直接返回数据至调用界面 上传文件
            setResult(2, data);
            finish();
        }

        if (requestCode == 200 && resultCode == 300) {
            // 获取数据重新组合 修改缩略图
            select_images = (List<SystemImages>) data.getSerializableExtra("images");
            for (SystemImages image : select_images) {
                int position = image.getPosition();
                images.remove(position);
                images.add(position, image);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
