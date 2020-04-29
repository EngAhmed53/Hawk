package com.shouman.apps.hawk.data.model;

public class SalesListItem {
    private String name;
    private boolean status;

    public SalesListItem() {
    }

    public SalesListItem(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
