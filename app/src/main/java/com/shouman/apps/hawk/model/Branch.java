package com.shouman.apps.hawk.model;

import java.util.Map;

public class Branch {
    private String name;
    private Map<String, String> sales_team;

    public Branch(String name, Map<String , String> sales_team) {
        this.name = name;
        this.sales_team = sales_team;
    }

    public Branch() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getSales_team() {
        return sales_team;
    }

    public void setSales_team(Map<String, String> sales_team) {
        this.sales_team = sales_team;
    }
}
