package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.customerstyle.AlertDialog;
import com.tc5u.vehiclemanger.model.SystemImages;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.util.List;

/**
 * 相册适配器
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<SystemImages> images;

    private Context context;

    private Integer width;

    private Integer maxCount;

    private OnItemClickListener mListener;

    private RequestOptions options;


    public AlbumAdapter(List<SystemImages> images, int maxCount, OnItemClickListener mListener) {
        this.images = images;
        this.maxCount = maxCount;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        if (width == null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            width = dm.widthPixels / 4 - 10;
        }

        if (options == null) {
            options = new RequestOptions().override(width, width).error(R.drawable.image_default).placeholder(R.drawable.image_default).centerCrop();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);
        return new AlbumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SystemImages image = images.get(position);
        if (!ObjectUtil.getString(holder.imageView.getTag(R.id.images)).equals(image.getPath()) || ObjectUtil.getBoolean(image.getIs_crop())) {
            holder.imageView.setMaxWidth(width);
            holder.imageView.setMaxHeight(width);
            String path = image.getPath();
            if (ObjectUtil.getBoolean(image.getIs_crop())) {
                path = image.getCrop_path();
            }
            Glide.with(context).load(path)
                    .apply(options)
                    .into(holder.imageView);
            if (image.getIs_select()) {
                holder.check_box.setChecked(true);
            } else {
                holder.check_box.setChecked(false);
            }
            holder.imageView.setTag(R.id.images, image.getPath());
        }
        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canSelect()) {
                    boolean checked = ObjectUtil.getBoolean(image.getIs_select());
                    image.setIs_select(!checked);

                } else {
                    holder.check_box.setChecked(false);
                    image.setIs_select(false);
                    if (!canSelect()) {
                        new AlertDialog(context).builder().setMsg("您最多只能选择" + maxCount + "张图片")
                                .setTitle("提示")
                                .setPositiveButton("知道了", context.getResources().getColor(R.color.red), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .show();
                    }
                }
                mListener.onItemClickListener(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private Boolean canSelect() {
        if (ObjectUtil.getInteger(maxCount) < 1) return true;
        int i = 0;
        for (SystemImages image : images) {
            if (ObjectUtil.getBoolean(image.getIs_select())) i++;
        }
        return maxCount > i;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        AppCompatCheckBox check_box;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sys_img);
            check_box = itemView.findViewById(R.id.check_box);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(SystemImages images);
    }
}
