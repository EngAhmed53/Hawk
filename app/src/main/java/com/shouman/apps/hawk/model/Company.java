package com.shouman.apps.hawk.model;

import java.util.HashMap;

public class Company {

    private String userName;
    private String email;
    private String companyName;
    private HashMap<String, Boolean> branchesList;
    //private boolean isPaidVersion;

    public Company() {
        this.branchesList = new HashMap<>();
        branchesList.put("Main branch", true);
    }


    public Company(String userName, String companyName, String email) {
        this.userName = userName;
        this.companyName = companyName;
        this.email = email;
        branchesList.put("Main branch", true);
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


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public HashMap<String, Boolean> getBranchesList() {
        return branchesList;
    }

    public void setBranchesList(HashMap<String, Boolean> branchesList) {
        this.branchesList = branchesList;
    }
}
