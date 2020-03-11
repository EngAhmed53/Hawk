package com.shouman.apps.hawk.model;

import java.util.HashMap;

public class Company {

    private String userName;
    private String email;
    private String companyName;
    private HashMap<String, Boolean> branchesList;
    private String type;
    //private boolean isPaidVersion;

    public Company() {
        this.branchesList = new HashMap<>();
        this.type = "company_account";
    }


    public Company(String userName, String companyName, String email) {
        this.userName = userName;
        this.companyName = companyName;
        this.email = email;
        this.type = "company_account";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Boolean> getBranchesList() {
        return branchesList;
    }

    public void setBranchesList(HashMap<String, Boolean> branchesList) {
        this.branchesList = branchesList;
    }
}
