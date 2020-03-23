package com.shouman.apps.hawk.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

@Entity(tableName = "clients_table")
public class SalesMan {

    @PrimaryKey(autoGenerate = true)
    @Exclude
    private long _id;

    //user name
    private String un;

    //email
    private String e;

    //user_type
    private String t;

    @Ignore
    private HashMap<String, Boolean> customersHashMap;

    @Ignore
    public SalesMan() {
        customersHashMap = new HashMap<>();
        this.t = "sales_member_account";
    }

    public SalesMan(String un, String e) {
        this.un = un;
        this.e = e;
        customersHashMap = new HashMap<>();
        this.t = "sales_member_account";
    }

    @Ignore
    public SalesMan(long _id, String un, String e) {
        this._id = _id;
        this.un = un;
        this.e = e;
        customersHashMap = new HashMap<>();
    }


    public String getUn() {
        return un;
    }

    public String getE() {
        return e;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public void setE(String e) {
        this.e = e;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public HashMap<String, Boolean> getCustomersHashMap() {
        return customersHashMap;
    }

    public void setCustomersHashMap(HashMap<String, Boolean> customersHashMap) {
        this.customersHashMap = customersHashMap;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
