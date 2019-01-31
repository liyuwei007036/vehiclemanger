package com.tc5u.vehiclemanger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.adapter.VehicleInfoAdapter;
import com.tc5u.vehiclemanger.customerstyle.SwipeItemLayout;
import com.tc5u.vehiclemanger.model.VehicleInfo;

import java.util.ArrayList;
import java.util.List;


public class VehicleFragment extends Fragment {

    private View view;

    private VehicleInfoAdapter adapter;

    private List<VehicleInfo> vehicleInfos = new ArrayList<>();

    public Context context;

    private int cur_tap = 0;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_vehicle, container, false);
            if (context == null) context = getContext();
            recyclerView = view.findViewById(R.id.vehicles);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(context));
            adapter = new VehicleInfoAdapter(vehicleInfos, context);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        }
        return view;
    }


    public void setVehicleInfosForAllVehicle(List<VehicleInfo> list) {
        vehicleInfos.clear();
        vehicleInfos.addAll(list);
        adapter.notifyDataSetChanged();
    }


    public void setVehicleInfos(List<VehicleInfo> sameinfo, List<VehicleInfo> difinfo, int tap) {
        cur_tap = tap;
        vehicleInfos.clear();
        if (cur_tap == 1) {
            vehicleInfos.addAll(difinfo);
        } else {
            vehicleInfos.addAll(sameinfo);
        }
        adapter.notifyDataSetChanged();
    }

}
