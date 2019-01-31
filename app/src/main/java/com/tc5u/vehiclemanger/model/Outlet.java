package com.tc5u.vehiclemanger.model;

public class Outlet {
    private String letter;

    private Boolean is_letter;

    private Long id;

    private String name;

    private Boolean is_select;

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

    public Boolean getIs_select() {
        return is_select;
    }

    public void setIs_select(Boolean is_select) {
        this.is_select = is_select;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Boolean getIs_letter() {
        return is_letter;
    }

    public void setIs_letter(Boolean is_letter) {
        this.is_letter = is_letter;
    }
}
