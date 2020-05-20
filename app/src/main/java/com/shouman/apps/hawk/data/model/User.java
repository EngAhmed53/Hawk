package com.shouman.apps.hawk.data.model;

public class User {
    //userName
    private String un;
    //email
    private String e;
    //branch uid
    private String buid;
    //company uid
    private String cuid;
    //user type
    private String ut;
    //companyName
    private String cn;

    //branchName
    private String bn;

    private boolean status;

    public User() {
        status = true;
    }

    public User(String un, String e, String buid, String cuid, String ut, String cn, String bn) {
        this.un = un;
        this.e = e;
        this.buid = buid;
        this.cuid = cuid;
        this.ut = ut;
        this.cn = cn;
        this.bn = bn;
        status = true;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getBuid() {
        return buid;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
