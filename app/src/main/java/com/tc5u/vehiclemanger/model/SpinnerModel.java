package com.tc5u.vehiclemanger.model;


import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

public class SpinnerModel {

    private String name;

    private Object value;

    public SpinnerModel(JSONObject object) {
        this.name = ObjectUtil.getString(object.getString("value"));
        this.value = object.get("id");
    }


    public SpinnerModel(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return this.name;
    }
}
