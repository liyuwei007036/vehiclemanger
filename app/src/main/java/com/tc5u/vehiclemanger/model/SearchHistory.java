package com.tc5u.vehiclemanger.model;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class SearchHistory extends LitePalSupport {
    private int id;

    private String value;

    private Date search_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getSearch_date() {
        return search_date;
    }

    public void setSearch_date(Date search_date) {
        this.search_date = search_date;
    }
}
