package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.tc5u.vehiclemanger.model.SystemImages;
import com.tc5u.vehiclemanger.model.VehiclePhoto;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.util.List;

import core.BaseActivity;

/**
 * 车辆图片预览适配器
 */
public class VehiclePhotoViewAdapter extends PagerAdapter {

    private List<VehiclePhoto> imageUrls;
    private Context context;

    public VehiclePhotoViewAdapter(List<VehiclePhoto> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final VehiclePhoto url = imageUrls.get(position);
        String path = url.getUrl();
        PhotoView photoView = new PhotoView(context);
        Glide.with(context)
                .load(path)
                .into(photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

