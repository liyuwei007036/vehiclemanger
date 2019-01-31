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
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chaychan.viewlib.PowerfulEditText;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.customerstyle.ActionSheet;
import com.tc5u.vehiclemanger.model.SystemImages;
import com.tc5u.vehiclemanger.model.Vehicle;
import com.tc5u.vehiclemanger.utils.DiaLogUtils;
import com.tc5u.vehiclemanger.utils.FileUtils;
import com.tc5u.vehiclemanger.utils.ImgUtils;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;
import com.tc5u.vehiclemanger.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import core.BaseActivity;

public class UploadVehicleActivity extends BaseActivity implements ActionSheet.OnActionSheetSelected {

    private PowerfulEditText powerfulEditText;

    private TextView err_msg;

    private ProgressDialog progressDialog;

    private Uri imageUri;

    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vehicle);
        initToolBar();
        initView();
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
        vehicle = Vehicle.searchVehicle();
        powerfulEditText = findViewById(R.id.vin);
        if (vehicle != null) powerfulEditText.setText(vehicle.getVin());
        err_msg = findViewById(R.id.error_msg);
        powerfulEditText.setOnRightClickListener(new PowerfulEditText.OnRightClickListener() {
            @Override
            public void onClick(EditText editText) {
                ActionSheet.showSheet(UploadVehicleActivity.this, UploadVehicleActivity.this, null);
            }
        });


        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_upload);
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
                            case R.id.item_mine:
                                startActivity(null, MineActivity.class);
                                finish();
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onClick(int whichButton) {
        switch (whichButton) {
            case ActionSheet.CHOOSE_PICTURE:
                //相册
                Intent intent = new Intent(this, AlbumActivity.class);
                startActivityForResult(intent, 200);
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
     * 图片上传
     *
     * @param images
     */
    private void uploadImg(List<SystemImages> images) throws Exception {
        List<String> img_paths = new ArrayList<>();
        for (SystemImages image : images) {
            String path = image.getPath();
            if (ObjectUtil.getBoolean(image.getIs_crop())) {
                path = image.getCrop_path();
            }
            img_paths.add(path);
        }
        OkHttpUtils.getInstance().PostWithFile("/getVinWord", img_paths, 60, new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
                showToast("网络错误");
            }

            @Override
            public void onLoadingShow() {
                progressDialog = loadAlertDialog("", "识别中...");
                progressDialog.show();
            }

            @Override
            public void onLoadingDismiss() {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject res = JSONObject.parseObject(result_str);
                if (res.getInteger("status") == 0) {
                    String vin = res.getString("vin");
                    powerfulEditText.setText(vin);
                    err_msg.setVisibility(View.GONE);
                } else {
                    err_msg.setText("无法识别,请上传清晰照片");
                    err_msg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String url, Exception e) {
                progressDialog.dismiss();
            }

            @Override
            public void onError(String url, String result_str) {
                progressDialog.dismiss();
            }
        });
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
        startActivityForResult(intent, 1);
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

    public void saveVin(View view) {
        String vin = ObjectUtil.getString(powerfulEditText.getText());
        if (vin.length() != 17) {
            err_msg.setText("VIN码的长度为17位，请检查后重新输入");
            err_msg.setVisibility(View.VISIBLE);
        } else {
            String pattern = "([A-Z]{1})([A-Z]|\\d){16}";
            boolean r = Pattern.matches(pattern, vin.toUpperCase());
            if (r) {
                checkVIN(vin);
            } else {
                err_msg.setText("请正确填写VIN码");
                err_msg.setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkVIN(final String vin) {
        JSONObject data = new JSONObject();
        data.put("vin", vin);
        OkHttpUtils.getInstance().PostWithJson("/getModelByVin", data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                    if (vehicle == null) {
                        vehicle = new Vehicle();
                        vehicle.setIs_edit(false);
                    }
                    if (ObjectUtil.getLong(vehicle.getBrand_id()) < 1) {
                        vehicle.setBrand_id(ObjectUtil.getLong(res.getLong("brandId")));
                    }
                    if (StringUtils.isEmpty(vehicle.getBrand_name())) {
                        vehicle.setBrand_name(ObjectUtil.getString(res.getString("brandName")));
                    }
                    if (ObjectUtil.getLong(vehicle.getSeries_id()) < 1) {
                        vehicle.setSeries_id(ObjectUtil.getLong(res.getLong("seriesId")));
                    }
                    if (StringUtils.isEmpty(vehicle.getSeries_name())) {
                        vehicle.setSeries_name(ObjectUtil.getString(res.getString("seriesName")));
                    }
                    if (ObjectUtil.getLong(vehicle.getModel_id()) < 1) {
                        vehicle.setModel_id(ObjectUtil.getLong(res.getLong("modelId")));
                    }
                    if (StringUtils.isEmpty(vehicle.getModel_name())) {
                        vehicle.setModel_name(ObjectUtil.getString(res.getString("modelName")));
                    }
                    if (StringUtils.isEmpty(vehicle.getBody_type())) {
                        vehicle.setBody_type(ObjectUtil.getString(res.getString("bodyType")));
                    }
                    if (StringUtils.isEmpty(vehicle.getGearbox())) {
                        vehicle.setGearbox(ObjectUtil.getString(res.getString("gearbox")));
                    }
                    if (StringUtils.isEmpty(vehicle.getEngine_no())) {
                        vehicle.setEngine_no(ObjectUtil.getString(res.getString("engine")));
                    }
                    if (ObjectUtil.getDouble(vehicle.getVolume()) <= 0d) {
                        vehicle.setVolume(ObjectUtil.getDouble(res.getString("volume")));
                    }
                    if (StringUtils.isEmpty(vehicle.getEmission())) {
                        vehicle.setEmission(ObjectUtil.getString(res.getString("emission")));
                    }
                    if (StringUtils.isEmpty(vehicle.getOutlet_name())) {
                        vehicle.setOutlet_name(ObjectUtil.getString(res.getString("outletName")));
                    }
                    if (ObjectUtil.getLong(vehicle.getOutlet_id()) < 1) {
                        vehicle.setOutlet_id(ObjectUtil.getLong(res.getLong("outletId")));
                    }
                    vehicle.setVin(vin);
                    vehicle.setCity(ObjectUtil.getString(res.getString("city")));
                    vehicle.save();
                    err_msg.setText("");
                    err_msg.setVisibility(View.GONE);
                    startActivity(null, CreateVehicleActivity.class);
                } else {
                    err_msg.setText(res.getString("msg"));
                    err_msg.setVisibility(View.VISIBLE);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 表示从相册选择图片完成
        if (requestCode == 200 && resultCode == 2) {
            List<SystemImages> selectImages = (List<SystemImages>) data.getSerializableExtra("images");
            try {
                uploadImg(selectImages);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 拍照并返回结果
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                SystemImages image = new SystemImages();
                image.setPath(ImgUtils.saveImageToGallery(this, bitmap));
                if (image.getPath() == null) throw new FileNotFoundException("文件不存在");
                List<SystemImages> o = new ArrayList<>();
                o.add(image);
                try {
                    uploadImg(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                DiaLogUtils.alertError(UploadVehicleActivity.this, "错误", e.getMessage(), new DiaLogUtils.DiaLogResultListener() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void fail() {

                    }
                });
            }
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
    protected void onResume() {
        super.onResume();
        vehicle = Vehicle.searchVehicle();
    }
}
