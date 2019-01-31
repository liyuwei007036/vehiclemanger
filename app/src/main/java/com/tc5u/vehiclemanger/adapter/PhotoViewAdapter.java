package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.tc5u.vehiclemanger.model.SystemImages;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.util.List;

import core.BaseActivity;

/**
 * 图片预览适配器
 */
public class PhotoViewAdapter extends PagerAdapter {

    private List<SystemImages> imageUrls;
    private Context context;

    public PhotoViewAdapter(List<SystemImages> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final SystemImages url = imageUrls.get(position);
        String path = url.getPath();
        if (ObjectUtil.getBoolean(url.getIs_crop())) {
            path = url.getCrop_path();
        }
        PhotoView photoView = new PhotoView(context);
        Glide.with(context)
                .load(path)
                .into(photoView);
        container.addView(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity activity = (BaseActivity) context;
                activity.finish();
            }
        });
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

