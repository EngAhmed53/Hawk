package com.shouman.apps.hawk.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.shouman.apps.hawk.BR;
import com.shouman.apps.hawk.common.Common;

import java.util.HashMap;

public class Company extends BaseObservable {

    private String comUserName;
    private String comEmail;
    private String comPhone;
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


    public Company(String id, String comUserName, String comName, String userEmail, String comUserPhone, String userPassword, HashMap<String, Boolean> branchesList) {
        this.comId = id;
        this.comUserName = comUserName;
        this.comUserName = userEmail;
        this.comPhone = comUserPhone;
        this.comPassword = userPassword;
        this.comName = comName;
        this.branchesList = branchesList;
        this.salesMenList = new HashMap<>();
        this.branchesList = new HashMap<>();
        branchesList.put("Main branch", true);
        this.isPaidVersion = false;
        this.comId = Common.generateRandomCompanyID();
    }


    public String getComUserName() {
        return comUserName;
    }

    public String getComEmail() {
        return comEmail;
    }

    public String getComPhone() {
        return comPhone;
    }

    public String getComPassword() {
        return comPassword;
    }

    public void setBaseUserInfo(BaseUser user) {
        this.comUserName = user.getUserName();
        this.comPhone = user.getUserPhone();
        this.comEmail = user.getUserEmail();
        this.comPassword = user.getUserPassword();
    }

    @Bindable
    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
        notifyPropertyChanged(BR.comId);
    }

    @Bindable
    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
        notifyPropertyChanged(BR.comName);
    }

    @Bindable
    public HashMap<String, Boolean> getSalesMenList() {
        return salesMenList;
    }

    public void setSalesMenList(HashMap<String, Boolean> salesMenList) {
        this.salesMenList = salesMenList;
        notifyPropertyChanged(BR.salesMenList);
    }

    @Bindable
    public boolean isPaidVersion() {
        return isPaidVersion;
    }

    public void setPaidVersion(boolean paidVersion) {
        isPaidVersion = paidVersion;
        notifyPropertyChanged(BR.paidVersion);
    }

    @Bindable
    public HashMap<String, Boolean> getBranchesList() {
        return branchesList;
    }

    public void setBranchesList(HashMap<String, Boolean> branchesList) {
        this.branchesList = branchesList;
        notifyPropertyChanged(BR.branchesList);
    }
}
