package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.activity.AllVehicleActivity;
import com.tc5u.vehiclemanger.activity.DemandMatchActivity;
import com.tc5u.vehiclemanger.activity.VehicleMatchActivity;
import com.tc5u.vehiclemanger.activity.VehiclePreviewActivity;
import com.tc5u.vehiclemanger.activity.VehiclePublishActivity;
import com.tc5u.vehiclemanger.customerstyle.SwipeItemLayout;
import com.tc5u.vehiclemanger.model.VehicleInfo;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import core.BaseApplication;

public class VehicleInfoAdapter extends RecyclerView.Adapter<VehicleInfoAdapter.ViewHolder> {

    private List<VehicleInfo> vehicleInfos;

    private Context context;

    private NumberFormat nf = new DecimalFormat("0.00");

    public VehicleInfoAdapter(List<VehicleInfo> vehicleInfoList, Context context) {
        this.context = context;
        vehicleInfos = vehicleInfoList;
    }

    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.placeholder) //加载成功之前占位图
            .error(R.drawable.img_error)//加载错误之后的错误图
            .fitCenter();        //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final VehicleInfo vehicleInfo = vehicleInfos.get(position);

        if (!vehicleInfo.getImg_url().equals(holder.car_img.getTag(R.id.car_img))) {
            if (vehicleInfo.getImg_url().startsWith("http")) {
                Glide.with(context).load(vehicleInfo.getImg_url()).apply(options).into(holder.car_img);
            } else {
                holder.car_img.setImageResource(R.drawable.img_error);
            }
            holder.car_img.setTag(R.id.car_img, vehicleInfo.getPublished());
            holder.car_name.setText(vehicleInfo.getName());
            holder.city.setText(vehicleInfo.getCity());
            holder.date.setText(vehicleInfo.getRegister_date());
            holder.distance.setText(nf.format(vehicleInfo.getDistance()));
            holder.sell_price.setText(ObjectUtil.getString(nf.format(vehicleInfo.getSell_price())));
            holder.type.setText(ObjectUtil.getString(vehicleInfo.getType()));
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DemandMatchActivity.class.isInstance(context)) {
                        Toast.makeText(context, "查看" + vehicleInfo.getId(), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, VehicleMatchActivity.class);
                        intent.putExtra("vehicle_id", vehicleInfo.getId());
                        context.startActivity(intent);
                    }
                }
            });

            if (DemandMatchActivity.class.isInstance(context)) {
                if (!vehicleInfo.getCan_move()) {
                    holder.move.setVisibility(View.GONE);
                }
                holder.send.setVisibility(View.VISIBLE);
                holder.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.send) {
                            recommendVehicle(vehicleInfo, view);
                        }
                        holder.swipeItemLayout.close();
                    }
                });
            }
            if (AllVehicleActivity.class.isInstance(context)) {
                if (vehicleInfo.getPublished()) {
                    holder.unPublish.setVisibility(View.VISIBLE);
                    holder.publish.setVisibility(View.GONE);
                } else {
                    holder.publish.setVisibility(View.VISIBLE);
                    holder.unPublish.setVisibility(View.GONE);
                }
                Long user_id = BaseApplication.getInstance().user_id;
                if (vehicleInfo.getUser_id().equals(user_id)) {
                    holder.edit.setVisibility(View.VISIBLE);
                } else {
                    holder.edit.setVisibility(View.GONE);
                }

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.swipeItemLayout.close();
                        Intent intent = new Intent(context, VehiclePreviewActivity.class);
                        intent.putExtra("vid", vehicleInfo.getId());
                        intent.putExtra("edit", true);
                        context.startActivity(intent);
                    }
                });

                holder.unPublish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.swipeItemLayout.close();
                        if (view.getId() == R.id.unPublish) {
                            takeDwnVehicle(vehicleInfo, view);
                        }
                    }
                });

                holder.publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.swipeItemLayout.close();
                        if (view.getId() == R.id.publish) {
                            Intent intent = new Intent(context, VehiclePublishActivity.class);
                            intent.putExtra("vehicle_id", vehicleInfo.getId());
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return vehicleInfos == null ? 0 : vehicleInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView car_img;
        TextView car_name, city, date, distance, sell_price, type;
        Button move, send, publish, unPublish, edit;
        SwipeItemLayout swipeItemLayout;
        View main;

        public ViewHolder(View itemView) {
            super(itemView);
            car_img = itemView.findViewById(R.id.car_img);
            car_name = itemView.findViewById(R.id.car_name);
            city = itemView.findViewById(R.id.city);
            date = itemView.findViewById(R.id.register_date);
            distance = itemView.findViewById(R.id.distance);
            sell_price = itemView.findViewById(R.id.sell_price);
            type = itemView.findViewById(R.id.type);
            move = itemView.findViewById(R.id.move);
            send = itemView.findViewById(R.id.send);
            publish = itemView.findViewById(R.id.publish);
            unPublish = itemView.findViewById(R.id.unPublish);
            edit = itemView.findViewById(R.id.edit);
            swipeItemLayout = itemView.findViewById(R.id.swipeItemLayout);
            main = itemView.findViewById(R.id.main);
        }
    }

    /**
     * 车源推荐
     *
     * @param vehicleInfo
     * @param view
     */
    private void recommendVehicle(VehicleInfo vehicleInfo, final View view) {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        String url = "/recommendVehicle";
        JSONObject data = new JSONObject();
        data.put("remand_id", vehicleInfo.getCustomer_demand_id());
        data.put("vehicle_id", vehicleInfo.getId());
        okHttpUtils.PostWithJson(url, data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                JSONObject object = JSONObject.parseObject(result_str);
                Snackbar snackbar;
                if (object.getInteger("status") == 0) {
                    snackbar = Snackbar.make(view, "推荐成功", Snackbar.LENGTH_LONG)
                            .setAction("知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                } else {
                    snackbar = Snackbar.make(view, object.getString("msg"), Snackbar.LENGTH_LONG)
                            .setAction("知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }
                snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.red));
                snackbar.setActionTextColor(context.getResources().getColor(R.color.black));
                snackbar.show();
            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });

    }


    private void takeDwnVehicle(final VehicleInfo vehicleInfo, final View view) {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        String url = "/takeDwnVehicle";
        JSONObject data = new JSONObject();
        data.put("vid", vehicleInfo.getId());
        okHttpUtils.PostWithJson(url, data.toJSONString(), new OkHttpUtils.ResultCallBackListener() {
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
                JSONObject object = JSONObject.parseObject(result_str);
                Snackbar snackbar;
                if (object.getInteger("status") == 0) {
                    snackbar = Snackbar.make(view, "下架成功", Snackbar.LENGTH_LONG)
                            .setAction("知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                    if (AllVehicleActivity.class.isInstance(context)) {

                    }
                } else {
                    snackbar = Snackbar.make(view, object.getString("msg"), Snackbar.LENGTH_LONG)
                            .setAction("知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }
                snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.red));
                snackbar.setActionTextColor(context.getResources().getColor(R.color.black));
                snackbar.show();

                if (AllVehicleActivity.class.isInstance(context)) {
                    AllVehicleActivity activity = (AllVehicleActivity) context;
                    activity.autoRefresh(vehicleInfo);
                }
            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });

    }
}
