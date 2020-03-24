package com.shouman.apps.hawk.model;

import java.util.Map;

public class Branch {
    //branch name
    private String n;

    private Map<String, String> SM;

    public Branch(String n, Map<String , String> SM) {
        this.n = n;
        this.SM = SM;
    }

    public Branch() {
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public Map<String, String> getSM() {
        return SM;
    }

    public void setSM(Map<String, String> SM) {
        this.SM = SM;
    }
}
