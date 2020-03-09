package com.shouman.apps.hawk.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;

@Entity(tableName = "clients_table")
public class SalesMan {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    private String userName;

    private String email;

    public HashMap<String, Boolean> getCustomersHashMap() {
        return customersHashMap;
    }

    public void setCustomersHashMap(HashMap<String, Boolean> customersHashMap) {
        this.customersHashMap = customersHashMap;
    }

    @Ignore
    private HashMap<String, Boolean> customersHashMap;

    @Ignore
    public SalesMan() {
        customersHashMap = new HashMap<>();
    }

    public SalesMan(String userName, String email) {
        this.userName = userName;
        this.email = email;
        customersHashMap = new HashMap<>();
    }

    @Ignore
    public SalesMan(long _id, String userName, String email) {
        this._id = _id;
        this.userName = userName;
        this.email = email;
        customersHashMap = new HashMap<>();
    }


    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }


}
