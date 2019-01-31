package com.tc5u.vehiclemanger.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.VehiclePhotoAdapter;
import com.tc5u.vehiclemanger.customerstyle.ActionSheet;
import com.tc5u.vehiclemanger.model.SystemImages;
import com.tc5u.vehiclemanger.model.Vehicle;
import com.tc5u.vehiclemanger.model.VehiclePhoto;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.ImgUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.BaseActivity;

public class UploadVehiclePhotoActivity extends BaseActivity {

    private Long vid;

    private RecyclerView ztwg, zkyb, zyns, qt;

    private List<VehiclePhoto> photo_ztwg = new ArrayList<>();
    private List<VehiclePhoto> photo_zkyb = new ArrayList<>();
    private List<VehiclePhoto> photo_zyns = new ArrayList<>();
    private List<VehiclePhoto> photo_qt = new ArrayList<>();

    private List<VehiclePhoto> photos = new ArrayList<>();

    private VehiclePhotoAdapter adapter_ztwg, adapter_zkyb, adapter_zyns, adapter_qt;

    private Uri imageUri;

    // 请求码 用于区分是哪一种类型的图片
    private int code = 0;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vehicle_photo);
        initAdapter();
        initToolBar();
        initIntent();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVehiclePhoto();
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
        vid = getIntent().getLongExtra("vid", 167518L);
    }

    private void initAdapter() {
        adapter_ztwg = new VehiclePhotoAdapter(photo_ztwg, new VehiclePhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int point) {
                if (point == photo_ztwg.size()) {
                    ActionSheet.showSheet(UploadVehiclePhotoActivity.this, new ActionSheet.OnActionSheetSelected() {
                        @Override
                        public void onClick(int whichButton) {
                            code = 1;
                            chooseImg(whichButton);
                        }
                    }, null);
                } else {
                    photoPreview(photos.indexOf(photo_ztwg.get(point)));
                }
            }
        });

        adapter_zkyb = new VehiclePhotoAdapter(photo_zkyb, new VehiclePhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int point) {
                if (point == photo_zkyb.size()) {
                    ActionSheet.showSheet(UploadVehiclePhotoActivity.this, new ActionSheet.OnActionSheetSelected() {
                        @Override
                        public void onClick(int whichButton) {
                            code = 2;
                            chooseImg(whichButton);
                        }
                    }, null);
                } else {
                    photoPreview(photos.indexOf(photo_zkyb.get(point)));
                }
            }
        });

        adapter_zyns = new VehiclePhotoAdapter(photo_zyns, new VehiclePhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int point) {
                if (point == photo_zyns.size()) {
                    ActionSheet.showSheet(UploadVehiclePhotoActivity.this, new ActionSheet.OnActionSheetSelected() {
                        @Override
                        public void onClick(int whichButton) {
                            code = 3;
                            chooseImg(whichButton);
                        }
                    }, null);
                } else {
                    photoPreview(photos.indexOf(photo_zyns.get(point)));
                }
            }
        });

        adapter_qt = new VehiclePhotoAdapter(photo_qt, new VehiclePhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int point) {
                if (point == photo_qt.size()) {
                    ActionSheet.showSheet(UploadVehiclePhotoActivity.this, new ActionSheet.OnActionSheetSelected() {
                        @Override
                        public void onClick(int whichButton) {
                            code = 4;
                            chooseImg(whichButton);
                        }
                    }, null);
                } else {
                    photoPreview(photos.indexOf(photo_qt.get(point)));
                }
            }
        });
    }

    public void initView() {
        ztwg = findViewById(R.id.ztwg);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        ztwg.setLayoutManager(manager);
        ztwg.setAdapter(adapter_ztwg);

        zkyb = findViewById(R.id.zkyb);
        GridLayoutManager manager1 = new GridLayoutManager(this, 4);
        zkyb.setLayoutManager(manager1);
        zkyb.setAdapter(adapter_zkyb);

        zyns = findViewById(R.id.zyns);
        GridLayoutManager manager2 = new GridLayoutManager(this, 4);
        zyns.setLayoutManager(manager2);
        zyns.setAdapter(adapter_zyns);

        qt = findViewById(R.id.qt);
        GridLayoutManager manager3 = new GridLayoutManager(this, 4);
        qt.setLayoutManager(manager3);
        qt.setAdapter(adapter_qt);
    }

    public void loadVehiclePhoto() {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        JSONObject object = new JSONObject();
        object.put("vid", vid);
        object.put("head", okHttpUtils.IMG_URL);
        okHttpUtils.PostWithJson("/editVehiclePhoto", object.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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

                if (res.getInteger("status") == 0) {

                    JSONArray array_ztwg = res.getJSONArray("ztwg");
                    loadPhoto(array_ztwg, photo_ztwg, "整体外观", adapter_ztwg);

                    JSONArray array_zkyb = res.getJSONArray("zkyb");
                    loadPhoto(array_zkyb, photo_zkyb, "中控仪表", adapter_zkyb);


                    JSONArray array_zyns = res.getJSONArray("zyns");
                    loadPhoto(array_zyns, photo_zyns, "座椅内饰", adapter_zyns);


                    JSONArray array_qt = res.getJSONArray("qt");
                    loadPhoto(array_qt, photo_qt, "其他", adapter_qt);

                    photos.clear();
                    photos.addAll(photo_ztwg);
                    photos.addAll(photo_zkyb);
                    photos.addAll(photo_zyns);
                    photos.addAll(photo_qt);
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

    private void loadPhoto(JSONArray array, List<VehiclePhoto> photos, String type, VehiclePhotoAdapter adapter) {
        photos.clear();
        for (int i = 0; i < array.size(); i++) {
            photos.add(new VehiclePhoto(array.getJSONObject(i), vid, type));
        }
        adapter.notifyDataSetChanged();
    }

    private void chooseImg(int whichButton) {
        switch (whichButton) {
            case ActionSheet.CHOOSE_PICTURE:
                //相册
                Intent intent = new Intent(this, AlbumActivity.class);
                intent.putExtra("maxCount", 9);
                startActivityForResult(intent, code);
                break;
            case ActionSheet.TAKE_PICTURE:
                //拍照
                takePic();
                break;
            case ActionSheet.CANCEL:
                //取消
                break;
        }
    }


    /**
     * 打开相机
     */
    public void openCamera() {
        File outPutImage = new File(getExternalCacheDir(), "TC5U-" + System.currentTimeMillis() + ".jpg");
        try {
            if (outPutImage.exists()) {
                outPutImage.delete();
            }
            outPutImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.tc5u.vehicleManger.fileProvider", outPutImage);
        } else {
            imageUri = Uri.fromFile(outPutImage);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, code);
    }

    /**
     * 打开相机前检查权限
     */
    public void takePic() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return;
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    showToast("相机权限禁用了。请务必开启相机权限");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        uploadImages(resultCode, data);
    }

    private void uploadImages(int resultCode, Intent data) {
        if (resultCode == 0) {
            Log.i("UploadPhoto", "用户点击了取消按钮");
            return;
        }
        try {
            List<SystemImages> images = null;
            if (resultCode == 2) {
                images = (List<SystemImages>) data.getSerializableExtra("images");
            } else if (resultCode == -1) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    SystemImages image = new SystemImages();
                    image.setWeight(ObjectUtil.getDouble(bitmap.getWidth()));
                    image.setHeight(ObjectUtil.getDouble(bitmap.getHeight()));
                    image.setPath(ImgUtils.saveImageToGallery(this, bitmap));
                    if (image.getPath() == null) throw new FileNotFoundException("文件不存在");
                    images = new ArrayList<>();
                    images.add(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    DiaLogUtils.alertError(UploadVehiclePhotoActivity.this, "错误", e.getMessage(), new DiaLogUtils.DiaLogResultListener() {
                        @Override
                        public void success() {

                        }

                        @Override
                        public void fail() {

                        }
                    });
                }
            }

            if (images == null || images.size() < 1) {
                throw new Exception("上传失败,图片数量为0");
            }

            List<String> img_paths = new ArrayList<>();
            int i = 1;
            for (SystemImages image : images) {
                String path = image.getPath();
                if (ObjectUtil.getBoolean(image.getIs_crop())) {
                    path = image.getCrop_path();
                } else {
                    if (image.getWeight() <= image.getHeight())
                        throw new Exception("第" + i + "张图片长宽比例错误");
                }
                i++;
                img_paths.add(path);
            }

            String type;
            switch (code) {
                case 1:
                    type = "整体外观";
                    break;
                case 2:
                    type = "中控仪表";
                    break;
                case 3:
                    type = "座椅内饰";
                    break;
                default:
                    type = "其他";
                    break;
            }
            String url = "/uploadVehiclePhoto?vehicle_id=" + vid + "&type=" + type;

            progressDialog = loadAlertDialog(null, "上传中...");

            OkHttpUtils.getInstance().PostWithFile(url, img_paths, 60, new OkHttpUtils.ResultCallBackListener() {
                @Override
                public void noNetWork() {

                }

                @Override
                public void onLoadingShow() {
                    progressDialog.show();
                }

                @Override
                public void onLoadingDismiss() {
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(String url, String result_str) {
                    JSONObject res = JSONObject.parseObject(result_str);
                    photo_zkyb.clear();
                    photo_ztwg.clear();
                    photo_zyns.clear();
                    photo_qt.clear();
                    loadVehiclePhoto();
                }

                @Override
                public void onFailure(String url, Exception e) {

                }

                @Override
                public void onError(String url, String result_str) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            DiaLogUtils.alertError(UploadVehiclePhotoActivity.this, "错误", e.getMessage(), new DiaLogUtils.DiaLogResultListener() {
                @Override
                public void success() {
                }

                @Override
                public void fail() {

                }
            });
        }
    }

    private void photoPreview(int point) {
        Intent intent = new Intent(this, VehiclePhotoPreviewActivity.class);
        intent.putExtra("images", (Serializable) photos);
        intent.putExtra("currentPosition", point);
        startActivity(intent);
    }
}
