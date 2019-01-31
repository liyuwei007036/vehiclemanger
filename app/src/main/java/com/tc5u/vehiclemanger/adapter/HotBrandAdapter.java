package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.activity.ChooseBrandActivity;
import com.tc5u.vehiclemanger.model.Brand;

import java.util.List;

public class HotBrandAdapter extends RecyclerView.Adapter<HotBrandAdapter.ViewHolder> {

    private Context context;

    private List<Brand> brands;

    private int request_code;

    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.placeholder) // 加载成功之前占位图
            .error(R.drawable.img_error) // 加载错误之后的错误图
            .fitCenter();

    public HotBrandAdapter(List<Brand> brands, int request_code) {
        this.brands = brands;
        this.request_code = request_code;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.hot_brand_items, parent, false);
        return new HotBrandAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Brand brand = brands.get(position);

        if (!brand.getId().equals(holder.brand_name.getTag())) {
            Glide.with(context).load(brand.getUrl()).apply(options).into(holder.logo);
            holder.brand_name.setText(brand.getName());
            holder.brand_name.setTag(brand.getId());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnRes(brand);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brands == null ? 0 : brands.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView brand_name;

        public ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.hot_url);
            brand_name = itemView.findViewById(R.id.hot_text);
        }
    }


    private void returnRes(Brand brand) {
        ChooseBrandActivity activity = (ChooseBrandActivity) context;
        Intent intent = new Intent();
        intent.putExtra("brand_id", brand.getId());
        intent.putExtra("brand_name", brand.getName());
        activity.setResult(9, intent);
        activity.finish();
    }

}
