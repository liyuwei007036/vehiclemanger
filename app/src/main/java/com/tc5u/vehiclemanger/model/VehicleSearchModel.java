package com.tc5u.vehiclemanger.model;

public class VehicleSearchModel {

    public static final String PUBLISH = "PUBLISH";

    public static final String IN_OUTLET = "IN_OUTLET";

    public static final String OWNER_TYPE = "OWNER_TYPE";

    private String name;

    private Object value;

    private String type;

    public VehicleSearchModel(String name, Object value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
