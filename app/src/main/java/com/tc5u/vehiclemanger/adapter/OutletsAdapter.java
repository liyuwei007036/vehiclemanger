package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.activity.ChangeOutletActivity;
import com.tc5u.vehiclemanger.customerstyle.SideBarItemEntity;
import com.tc5u.vehiclemanger.model.Outlet;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.util.List;

public class OutletsAdapter extends SideBarBaseAdapter<Outlet, OutletsAdapter.ViewHolder> {

    private Context context;

    private Boolean is_all;

    public OutletsAdapter(List<SideBarItemEntity<Outlet>> dataList, Boolean is_all) {
        super(dataList);
        this.is_all = is_all;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.outlet_item, parent, false);
        return new OutletsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("onBindViewHolder", position + "");
        final Outlet outlet = mDataList.get(position).getValue();
        holder.outlet_name.setText(outlet.getName());
        if (outlet.getIs_select()) {
            holder.outlet_name.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.outlet_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_all) {
                    returnRes(outlet);
                } else {
                    changeOutlet(outlet);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        View outlet_name_b;
        RadioButton outlet_name;

        public ViewHolder(View itemView) {
            super(itemView);
            outlet_name = itemView.findViewById(R.id.outlet_name);
            outlet_name_b = itemView.findViewById(R.id.outlet_name_b);
        }
    }


    private void changeOutlet(Outlet outlet) {
        String url = "/doChangeOutlet";
        JSONObject object = new JSONObject();
        object.put("o.outlet_id", outlet.getId());
        OkHttpUtils.getInstance().PostWithJson(url, object.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {

            }

            @Override
            public void onSuccess(String url, String result_str) {
                ChangeOutletActivity activity = (ChangeOutletActivity) context;
                activity.setResult(4);
                activity.finish();
            }

            @Override
            public void onFailure(String url, Exception e) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String url, String result_str) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void returnRes(Outlet outlet) {
        ChangeOutletActivity activity = (ChangeOutletActivity) context;
        Intent intent = new Intent();
        intent.putExtra("outlet_id", outlet.getId());
        intent.putExtra("outlet_name", outlet.getName());
        activity.setResult(8, intent);
        activity.finish();
    }
}
