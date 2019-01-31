package com.tc5u.vehiclemanger.model;


import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.utils.StringUtils;

public class CommonModel {

    private Long id;

    private String name;

    public CommonModel(JSONObject object) {
        this.id = object.getLong("id");
        if (StringUtils.isNotEmpty(object.getString("value"))) {
            this.name = object.getString("value");
        }
        if (StringUtils.isNotEmpty(object.getString("name"))) {
            this.name = object.getString("name");
        }
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


}
