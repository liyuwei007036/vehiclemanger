package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.activity.ChooseDealerActivity;
import com.tc5u.vehiclemanger.activity.ChooseModelActivity;
import com.tc5u.vehiclemanger.activity.ChooseSeriesActivity;
import com.tc5u.vehiclemanger.model.CommonModel;

import java.util.List;

public class DealerAdapter extends RecyclerView.Adapter<DealerAdapter.ViewHolder> {

    private Context context;

    private Long model_id;

    private List<CommonModel> list;

    public DealerAdapter(List<CommonModel> list, Long model_id) {
        this.model_id = model_id;
        this.list = list;
    }


    @Override
    public DealerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.series_item, parent, false);
        return new DealerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DealerAdapter.ViewHolder holder, int position) {
        final CommonModel commonModel = list.get(position);
        if (!commonModel.getId().equals(holder.series_text.getTag())) {
            if (model_id == commonModel.getId()) {
                holder.series_text.setTextColor(context.getResources().getColor(R.color.tl));
            }
            holder.series_text.setText(commonModel.getName());
            holder.series_text.setTag(commonModel.getId());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnRes(commonModel);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView series_text;

        public ViewHolder(View itemView) {
            super(itemView);
            series_text = itemView.findViewById(R.id.series_text);
        }
    }

    private void returnRes(CommonModel commonModel) {
        ChooseDealerActivity activity = (ChooseDealerActivity) context;
        Intent intent = new Intent();
        intent.putExtra("dealer_id", commonModel.getId());
        intent.putExtra("dealer_name", commonModel.getName());
        activity.setResult(11, intent);
        activity.finish();
    }
}