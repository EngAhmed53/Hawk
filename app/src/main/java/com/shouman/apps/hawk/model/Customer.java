package com.shouman.apps.hawk.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "customers_table")
public class Customer {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String customerName;

    private double lat;

    private double lang;

    private String companyName;

    private String phoneNumber;

    private long timeStamp;

    @Ignore
    public Customer() {
    }

    @Ignore
    public Customer(long id, String customerName, double lat, double lang, String companyName, String phoneNumber, long timeStamp) {
        this.id = id;
        this.customerName = customerName;
        this.lat = lat;
        this.lang = lang;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.timeStamp = timeStamp;
    }

    public Customer(String customerName, double lat, double lang, String companyName, String phoneNumber, long timeStamp) {
        this.customerName = customerName;
        this.lat = lat;
        this.lang = lang;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
