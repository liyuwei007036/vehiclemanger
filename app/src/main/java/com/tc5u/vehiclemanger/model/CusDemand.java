package com.tc5u.vehiclemanger.model;


import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

public class CusDemand {
    private String brand_name;
    private String series_name;
    private String city;
    private String creator_mobile;
    private String cus_mobile;
    private String customer_name;
    private String date;
    private String demand_type;
    private String remark;


    public CusDemand(JSONObject object) {
        this.brand_name = ObjectUtil.getString(object.getString("brand_name"));
        this.series_name = ObjectUtil.getString(object.getString("series_name"));
        this.city = ObjectUtil.getString(object.getString("city"));
        this.cus_mobile = ObjectUtil.getString(object.getString("cus_mobile"));
        this.creator_mobile = ObjectUtil.getString(object.getString("creator_mobile"));
        this.customer_name = ObjectUtil.getString(object.getString("customer_name"));
        this.date = ObjectUtil.getString(object.getString("date"));
        this.demand_type = ObjectUtil.getString(object.getString("demand_type"));
        this.remark = ObjectUtil.getString(object.getString("remark"));
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreator_mobile() {
        return creator_mobile;
    }

    public void setCreator_mobile(String creator_mobile) {
        this.creator_mobile = creator_mobile;
    }

    public String getCus_mobile() {
        return cus_mobile;
    }

    public void setCus_mobile(String cus_mobile) {
        this.cus_mobile = cus_mobile;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDemand_type() {
        return demand_type;
    }

    public void setDemand_type(String demand_type) {
        this.demand_type = demand_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
