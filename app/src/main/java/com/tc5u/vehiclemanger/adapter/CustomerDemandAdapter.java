package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.activity.DemandMatchActivity;
import com.tc5u.vehiclemanger.model.CustomerDemand;

import java.util.List;

public class CustomerDemandAdapter extends RecyclerView.Adapter<CustomerDemandAdapter.ViewHolder> {

    private List<CustomerDemand> demands;

    private Context context;

    public CustomerDemandAdapter(List<CustomerDemand> demandList) {
        demands = demandList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View v = LayoutInflater.from(context).inflate(R.layout.demand_item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CustomerDemand demand = demands.get(position);
        holder.car.setText(demand.getCars());
        holder.remark.setText(demand.getRemark());
        holder.customer_name.setText(demand.getCus_name());
        holder.date.setText(demand.getDate());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.call) {
                    String phone = demand.getPhone();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    context.startActivity(intent);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMatchActivity(demand.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return demands.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView car, remark, customer_name, date;
        ImageView call;
        LinearLayout customer_name_date;

        public ViewHolder(View view) {
            super(view);
            car = view.findViewById(R.id.car);
            remark = view.findViewById(R.id.remark);
            customer_name = view.findViewById(R.id.customer_name);
            date = view.findViewById(R.id.date);
            call = view.findViewById(R.id.call);
            customer_name_date = view.findViewById(R.id.customer_name_date);

        }
    }


    private void startMatchActivity(Long id) {
        Intent intent = new Intent(context, DemandMatchActivity.class);
        intent.putExtra("demand_id", id);
        context.startActivity(intent);
    }
}
