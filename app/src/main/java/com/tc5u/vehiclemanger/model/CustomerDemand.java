package com.tc5u.vehiclemanger.model;

/**
 * 客户需求列表
 */
public class CustomerDemand {
    private Long id;

    private String remark;

    private String cus_name;

    private String date;

    private String phone;

    private String cars;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCars() {
        return cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }
}
