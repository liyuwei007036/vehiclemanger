package com.tc5u.vehiclemanger.model;

/**
 * 车辆信息
 */
public class VehicleInfo {
    private Long id;

    private Long user_id;

    private Boolean can_move;

    private Double distance;

    private Double sell_price;

    private String img_url;

    private String city;

    private String name;

    private String register_date;

    private String type;

    private Boolean hasPublishApply;

    private Boolean published;

    private Long customer_demand_id;

    public Long getCustomer_demand_id() {
        return customer_demand_id;
    }

    public void setCustomer_demand_id(Long customer_demand_id) {
        this.customer_demand_id = customer_demand_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Boolean getCan_move() {
        return can_move;
    }

    public void setCan_move(Boolean can_move) {
        this.can_move = can_move;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getSell_price() {
        return sell_price;
    }

    public void setSell_price(Double sell_price) {
        this.sell_price = sell_price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getHasPublishApply() {
        return hasPublishApply;
    }

    public void setHasPublishApply(Boolean hasPublishApply) {
        this.hasPublishApply = hasPublishApply;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
