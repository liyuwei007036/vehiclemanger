package com.tc5u.vehiclemanger.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class MapModel extends LitePalSupport {
    private int id;

    @Column(unique = true)
    private String key;


    @Column(nullable = false)
    private String value;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
