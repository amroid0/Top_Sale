package com.aelzohry.topsaleqatar.model;

public class AdsItem {

    private String title;
    private String value;

    public AdsItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

}
