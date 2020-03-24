package com.shouman.apps.hawk.model;

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

    //company name
    private String cn;

    public User() {
    }

    public User(String un, String e, String buid, String cuid, String ut, String cn) {
        this.un = un;
        this.e = e;
        this.buid = buid;
        this.cuid = cuid;
        this.ut = ut;
        this.cn = cn;
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
}
