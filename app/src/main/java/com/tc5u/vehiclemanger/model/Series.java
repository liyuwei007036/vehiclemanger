package com.tc5u.vehiclemanger.model;

public class Series {

    private Long id;

    private String name;

    private String parent_name;

    public Series(Long id, String name, String parent_name) {
        this.id = id;
        this.name = name;
        this.parent_name = parent_name;
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

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }
}
