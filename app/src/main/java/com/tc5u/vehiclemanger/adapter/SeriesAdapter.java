package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.activity.ChooseSeriesActivity;
import com.tc5u.vehiclemanger.customerstyle.SideBarItemEntity;
import com.tc5u.vehiclemanger.model.Series;

import java.util.List;

import core.BaseActivity;

public class SeriesAdapter extends SideBarBaseAdapter<Series, SeriesAdapter.ViewHolder> {

    private Context context;

    private Long series_id;


    public SeriesAdapter(List<SideBarItemEntity<Series>> dataList, Long series_id) {
        super(dataList);
        this.series_id = series_id;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.series_item, parent, false);
        return new SeriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Series series = mDataList.get(position).getValue();
        if (!series.getId().equals(holder.series_text.getTag())) {
            if (series_id == series.getId()) {
                holder.series_text.setTextColor(context.getResources().getColor(R.color.tl));
            }
            holder.series_text.setText(series.getName());
            holder.series_text.setTag(series.getId());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnRes(series);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView series_text;

        public ViewHolder(View itemView) {
            super(itemView);
            series_text = itemView.findViewById(R.id.series_text);
        }
    }

    private void returnRes(Series series) {
        BaseActivity activity = (BaseActivity) context;
        Intent intent = new Intent();
        intent.putExtra("series_id", series.getId());
        intent.putExtra("series_name", series.getName());
        activity.setResult(10, intent);
        activity.finish();
    }
}
