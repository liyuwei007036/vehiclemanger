package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.activity.SearchActivity;
import com.tc5u.vehiclemanger.model.SearchHistory;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private List<SearchHistory> histories;

    private Context context;


    public SearchHistoryAdapter(List<SearchHistory> histories) {
        this.histories = histories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchHistory history = histories.get(position);
        if (!ObjectUtil.getString(history.getId()).equals(holder.name.getTag(R.id.history_value))) {
            holder.name.setTag(R.id.history_value, history.getId());
            holder.name.setText(ObjectUtil.getString(history.getValue()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchActivity activity = (SearchActivity) context;
                    activity.saveHistory(history.getValue());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return histories == null ? 0 : histories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.history_value);

        }
    }
}
