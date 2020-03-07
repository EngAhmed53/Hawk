package com.shouman.apps.hawk.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.shouman.apps.hawk.BR;

public class BaseUser extends BaseObservable {

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userPassword;

    public BaseUser() {
    }

    public BaseUser(String userName, String userEmail, String userPhone, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
        notifyPropertyChanged(BR.userPhone);
    }

    @Bindable
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        notifyPropertyChanged(BR.userPassword);
    }
}
