package com.shouman.apps.hawk.model;

import java.util.HashMap;

public class Company {

    //user name
    private String u;

    //user email
    private String e;

    //user company name
    private String c;

    //branches map
    private HashMap<String, Boolean> b;

    //user type
    private String t;
    //private boolean isPaidVersion;

    public Company() {
        this.b = new HashMap<>();
        this.t = "company_account";
    }


    public Company(String u, String c, String e) {
        this.u = u;
        this.c = c;
        this.e = e;
        this.t = "company_account";
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getU() {
        return u;
    }

    public String getE() {
        return e;
    }

    public String getT() {
        return t;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public void setE(String e) {
        this.e = e;
    }

    public HashMap<String, Boolean> getB() {
        return b;
    }

    public void setB(HashMap<String, Boolean> b) {
        this.b = b;
    }
}
