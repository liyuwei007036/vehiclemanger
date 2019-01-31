package com.tc5u.vehiclemanger.model;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Vehicle extends LitePalSupport {

    public static final String OWNER_PERSONAL = "个人";
    public static final String OWNER_VENDOR = "商户";
    public static final String[] OWNER_LIST = new String[]{OWNER_PERSONAL, OWNER_VENDOR};

    public static final String STATUS_EXCLLENT = "很好";
    public static final String STATUS_GOOD = "较好";
    public static final String STATUS_NORMAL = "一般";
    public static final String STATUS_BAD = "较差";
    public static final String STATUS_POOR = "很差";
    public static final String STATUS_OTHER = "未知";
    public static final String[] STATUS_LIST = new String[]{STATUS_EXCLLENT, STATUS_GOOD, STATUS_NORMAL, STATUS_BAD, STATUS_POOR, STATUS_OTHER};

    public static final String SOURCE_HEADQUARTERS = "总部渠道";
    public static final String SOURCE_OUTLET = "店面渠道";
    public static final String SOURCE_SELF = "个人渠道";
    public static final String[] SOURCE_LIST = new String[]{SOURCE_HEADQUARTERS, SOURCE_OUTLET, SOURCE_SELF};


    public static final String[] BODY_LIST = new String[]{"微型车", "小型车", "紧凑型车", "中型车", "中大型车",
            "大型车", "小型SUV", "紧凑型SUV", "中型SUV", "大中型SUV", "大型SUV", "MPV", "跑车", "皮卡", "微面", "轻客"};

    public static final String[] GEAR_LIST = new String[]{"手动", "自动", "手自一体"};

    public static final String[] EMISSION_LIST = new String[]{"国二", "国三", "国四", "国五"};

    private Long id;

    private Long vehicle_id;

    private String vin;

    private Long brand_id;

    private String brand_name;

    private Long series_id;

    private String series_name;

    private Long model_id;

    private String model_name;

    private Boolean other_model;

    private String engine_no;

    private Long color_id;

    private String color_name;

    private String body_type;

    private Date factory_date;

    private Date register_date;

    private String licence_plate;

    private Double distance;

    private String emission;

    private String gearbox;

    private Double volume;

    private Boolean turbo;

    private Double new_price;

    private String province;

    private String city;

    private Long outlet_id;

    private String outlet_name;

    private Double sell_price;

    private String status;

    private String owner_type;

    private String source;

    private Boolean is_edit = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(Long vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public Long getSeries_id() {
        return series_id;
    }

    public void setSeries_id(Long series_id) {
        this.series_id = series_id;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public Long getModel_id() {
        return model_id;
    }

    public void setModel_id(Long model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public Boolean getOther_model() {
        return other_model;
    }

    public void setOther_model(Boolean other_model) {
        this.other_model = other_model;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public Long getColor_id() {
        return color_id;
    }

    public void setColor_id(Long color_id) {
        this.color_id = color_id;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public Date getFactory_date() {
        return factory_date;
    }

    public void setFactory_date(Date factory_date) {
        this.factory_date = factory_date;
    }

    public Date getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getEmission() {
        return emission;
    }

    public void setEmission(String emission) {
        this.emission = emission;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Boolean getTurbo() {
        return turbo;
    }

    public void setTurbo(Boolean turbo) {
        this.turbo = turbo;
    }

    public Double getNew_price() {
        return new_price;
    }

    public void setNew_price(Double new_price) {
        this.new_price = new_price;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(Long outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public Double getSell_price() {
        return sell_price;
    }

    public void setSell_price(Double sell_price) {
        this.sell_price = sell_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(Boolean is_edit) {
        this.is_edit = is_edit;
    }

    public static Vehicle searchVehicle(Boolean is_edit) {
        if (is_edit == null) is_edit = false;
        return LitePal.where("is_edit = ?", is_edit ? "1" : "0").findFirst(Vehicle.class);
    }

    public static Vehicle searchVehicle() {
        return searchVehicle(false);
    }
}