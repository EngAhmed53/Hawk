package com.shouman.apps.hawk.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.shouman.apps.hawk.BR;

@Entity(tableName = "customers_table")
public class Customer extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String customerName;

    private String customerAddress;

    private double customerLat;

    private double customerLang;

    private String customerCompanyName;

    private String customerPhoneNumber;

    private long timeStamp;

    private String customerId;

    @Ignore
    public Customer() {
    }

    @Ignore
    public Customer(long id, String customerName, String customerAddress, double customerLat, double customerLang, String customerCompanyName, String customerPhoneNumber, long timeStamp) {
        this.id = id;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerLat = customerLat;
        this.customerLang = customerLang;
        this.customerCompanyName = customerCompanyName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.timeStamp = timeStamp;
    }

    public Customer(String customerName, String customerAddress, double customerLat, double customerLang, String customerCompanyName, String customerPhoneNumber, long timeStamp) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerLat = customerLat;
        this.customerLang = customerLang;
        this.customerCompanyName = customerCompanyName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.timeStamp = timeStamp;
    }

    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        notifyPropertyChanged(BR.customerName);
    }

    @Bindable
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
        notifyPropertyChanged(BR.customerAddress);
    }

    @Bindable
    public double getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(double customerLat) {
        this.customerLat = customerLat;
        notifyPropertyChanged(BR.customerLat);
    }

    @Bindable
    public double getCustomerLang() {
        return customerLang;
    }

    public void setCustomerLang(double customerLang) {
        this.customerLang = customerLang;
        notifyPropertyChanged(BR.customerLang);
    }

    @Bindable
    public String getCustomerCompanyName() {
        return customerCompanyName;
    }

    public void setCustomerCompanyName(String customerCompanyName) {
        this.customerCompanyName = customerCompanyName;
        notifyPropertyChanged(BR.customerCompanyName);
    }

    @Bindable
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
        notifyPropertyChanged(BR.customerPhoneNumber);
    }

    @Bindable
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        notifyPropertyChanged(BR.timeStamp);
    }

    @Bindable
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        notifyPropertyChanged(BR.customerId);
    }
}
