package com.tc5u.vehiclemanger.model;

import java.io.Serializable;

public class SystemImages implements Serializable {
    private Long id;

    private String path;

    private String title;

    private Long size;

    private Double weight;

    private Double height;

    private String type;

    private int position;

    private Boolean is_select;

    private Boolean is_crop;

    private String crop_path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean getIs_select() {
        return is_select;
    }

    public void setIs_select(Boolean is_select) {
        this.is_select = is_select;
    }

    public Boolean getIs_crop() {
        return is_crop;
    }

    public void setIs_crop(Boolean is_crop) {
        this.is_crop = is_crop;
    }

    public String getCrop_path() {
        return crop_path;
    }

    public void setCrop_path(String crop_path) {
        this.crop_path = crop_path;
    }
}
