package com.shouman.apps.hawk.model;

import com.shouman.apps.hawk.common.Common;

import java.util.HashMap;

public class Company {

    private String comUserName;
    private String comEmail;
    private String comPassword;
    private String comName;
    private String comId;
    private HashMap<String, Boolean> salesMenList;
    private HashMap<String, Boolean> branchesList;
    private boolean isPaidVersion;

    public Company() {
        this.salesMenList = new HashMap<>();
        this.branchesList = new HashMap<>();
        branchesList.put("Main branch", true);
        this.isPaidVersion = false;
        this.comId = Common.generateRandomCompanyID();
    }


    public Company(String id, String comUserName, String comName, String userEmail, String userPassword) {
        this.comId = id;
        this.comUserName = comUserName;
        this.comUserName = userEmail;
        this.comPassword = userPassword;
        this.comName = comName;
        this.salesMenList = new HashMap<>();
        this.branchesList = new HashMap<>();
        branchesList.put("Main branch", true);
        this.isPaidVersion = false;
        this.comId = Common.generateRandomCompanyID();
    }

    public void setComUserName(String comUserName) {
        this.comUserName = comUserName;
    }

    public String getComUserName() {
        return comUserName;
    }

    public String getComEmail() {
        return comEmail;
    }

    public String getComPassword() {
        return comPassword;
    }

    public void setBaseUserInfo(BaseUser user) {
        this.comEmail = user.getUserEmail();
        this.comPassword = user.getUserPassword();
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public HashMap<String, Boolean> getSalesMenList() {
        return salesMenList;
    }

    public void setSalesMenList(HashMap<String, Boolean> salesMenList) {
        this.salesMenList = salesMenList;
    }

    public boolean isPaidVersion() {
        return isPaidVersion;
    }

    public void setPaidVersion(boolean paidVersion) {
        isPaidVersion = paidVersion;
    }

    public HashMap<String, Boolean> getBranchesList() {
        return branchesList;
    }

    public void setBranchesList(HashMap<String, Boolean> branchesList) {
        this.branchesList = branchesList;
    }
}
