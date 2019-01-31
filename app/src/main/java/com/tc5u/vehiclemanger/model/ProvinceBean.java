package com.tc5u.vehiclemanger.model;

import com.alibaba.fastjson.JSONObject;

public class ProvinceBean {

    private String name;

    private Long id;

    public ProvinceBean(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public ProvinceBean(JSONObject object) {
        this.name = object.getString("cityName");
        this.id = object.getLong("cityId");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
