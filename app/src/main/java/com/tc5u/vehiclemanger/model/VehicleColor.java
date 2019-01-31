package com.tc5u.vehiclemanger.model;

import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

public class VehicleColor {

    private Long id;

    private String name;

    public VehicleColor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static List<VehicleColor> getColors() {
        List<VehicleColor> vehicleColors = new ArrayList<>();
        vehicleColors.add(new VehicleColor(1L, "黑色"));
        vehicleColors.add(new VehicleColor(2L, "银灰色"));
        vehicleColors.add(new VehicleColor(3L, "红色"));
        vehicleColors.add(new VehicleColor(4L, "绿色"));
        vehicleColors.add(new VehicleColor(5L, "香槟色"));
        vehicleColors.add(new VehicleColor(6L, "橙色"));
        vehicleColors.add(new VehicleColor(7L, "白色"));
        vehicleColors.add(new VehicleColor(8L, "深灰色"));
        vehicleColors.add(new VehicleColor(9L, "蓝色"));
        vehicleColors.add(new VehicleColor(10L, "黄色"));
        vehicleColors.add(new VehicleColor(11L, "紫色"));
        vehicleColors.add(new VehicleColor(12L, "棕色"));
        return vehicleColors;
    }

    @Override
    public String toString() {
        return ObjectUtil.getString(name);
    }
}
