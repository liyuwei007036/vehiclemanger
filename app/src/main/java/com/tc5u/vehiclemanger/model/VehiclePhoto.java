package com.tc5u.vehiclemanger.model;

import com.alibaba.fastjson.JSONObject;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.io.Serializable;

public class VehiclePhoto implements Serializable {
    private Long vid;

    private String url;

    private String type;

    private String uuid;

    private Boolean is_main;

    public VehiclePhoto(JSONObject object, Long vid, String type) {
        this.vid = vid;
        this.url = ObjectUtil.getString(object.getString("src"));
        this.is_main = ObjectUtil.getBoolean(object.getBoolean("main"));
        this.uuid = ObjectUtil.getString(object.getString("uuid"));
        this.type = type;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getIs_main() {
        return is_main;
    }

    public void setIs_main(Boolean is_main) {
        this.is_main = is_main;
    }
}
