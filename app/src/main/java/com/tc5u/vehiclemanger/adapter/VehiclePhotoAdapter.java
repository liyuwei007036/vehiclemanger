package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.VehiclePhoto;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.util.List;

/**
 * 相册适配器
 */
public class VehiclePhotoAdapter extends RecyclerView.Adapter<VehiclePhotoAdapter.ViewHolder> {

    private List<VehiclePhoto> images;

    private Context context;

    private Integer width;

    private OnItemClickListener mListener;

    private RequestOptions options;


    public VehiclePhotoAdapter(List<VehiclePhoto> images, OnItemClickListener mListener) {
        this.images = images;
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

        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_photo_item, parent, false);
        return new VehiclePhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.v_photo.setMaxHeight(width);
        holder.v_photo.setMaxWidth(width);
        if (position < images.size()) {
            final VehiclePhoto image = images.get(position);
            String path = image.getUrl();
            Glide.with(context).load(path)
                    .apply(options)
                    .into(holder.v_photo);
            holder.v_photo.setTag(R.id.images, path);
            if (ObjectUtil.getBoolean(image.getIs_main())) {
                holder.main_icon.setVisibility(View.VISIBLE);
            } else {
                holder.main_icon.setVisibility(View.GONE);
            }
        } else {
            holder.main_icon.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.add_img)
                    .apply(options)
                    .into(holder.v_photo);
            holder.v_photo.setTag(R.id.images, R.drawable.add_img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images == null ? 1 : images.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView v_photo;
        TextView main_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            v_photo = itemView.findViewById(R.id.v_photo);
            main_icon = itemView.findViewById(R.id.main_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int images);
    }
}
