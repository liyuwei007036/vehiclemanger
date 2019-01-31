package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.VehicleSearchModel;

import java.util.List;

import core.BaseApplication;

public class SearchAdapter extends BaseAdapter {

    private List<VehicleSearchModel> list;

    private Context mContext = BaseApplication.getInstance().getContext();

    public SearchAdapter(List<VehicleSearchModel> list) {
        super();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_item, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VehicleSearchModel model = list.get(i);
        holder.search.setText(model.getName());
        return convertView;
    }


    class ViewHolder {
        TextView search;
        public ViewHolder(View view) {
            search = view.findViewById(R.id.search);
            view.setTag(this);
        }
    }
}
