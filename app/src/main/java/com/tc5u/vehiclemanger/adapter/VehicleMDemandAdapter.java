package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.CusDemand;

import java.util.List;

public class VehicleMDemandAdapter extends RecyclerView.Adapter<VehicleMDemandAdapter.ViewHolder> {

    private List<CusDemand> demands;

    private Context context;

    public VehicleMDemandAdapter(List<CusDemand> demands) {
        this.demands = demands;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_demand_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CusDemand demand = demands.get(position);
        holder.city.setText(demand.getCity());
        holder.customer_name.setText(demand.getCustomer_name());
        holder.date.setText(demand.getDate());
        holder.remark.setText(demand.getRemark());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = demand.getCreator_mobile();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return demands == null ? 0 : demands.size();
    }


    public void setDemands(List<CusDemand> demands) {
        this.demands.clear();
        this.demands.addAll(demands);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView customer_name, remark, city, date;
        ImageView call;

        public ViewHolder(View itemView) {
            super(itemView);
            customer_name = itemView.findViewById(R.id.customer_name);
            remark = itemView.findViewById(R.id.remark);
            city = itemView.findViewById(R.id.city);
            date = itemView.findViewById(R.id.date);
            call = itemView.findViewById(R.id.call);
        }
    }
}
