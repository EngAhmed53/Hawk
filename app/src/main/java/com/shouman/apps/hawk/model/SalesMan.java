package com.shouman.apps.hawk.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "clients_table")
public class SalesMan {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    private String salesName;

    private String salesEmail;

    private String salesPassword;

    private int salesId;

    private String companyId;

    private String branchPin;

    @Ignore
    public SalesMan() {
    }

    public SalesMan(String salesName, String salesEmail, String salesPassword, int salesId, String companyId, String branchPin) {
        this.salesName = salesName;
        this.salesEmail = salesEmail;
        this.salesPassword = salesPassword;
        this.salesId = salesId;
        this.companyId = companyId;
        this.branchPin = branchPin;
    }

    @Ignore
    public SalesMan(long _id, String salesName, String salesEmail, String salesPassword, int salesId, String companyId, String branchPin) {
        this._id = _id;
        this.salesName = salesName;
        this.salesEmail = salesEmail;
        this.salesPassword = salesPassword;
        this.salesId = salesId;
        this.companyId = companyId;
        this.branchPin = branchPin;
    }


    public String getSalesName() {
        return salesName;
    }

    public String getSalesEmail() {
        return salesEmail;
    }


    public String getSalesPassword() {
        return salesPassword;
    }

    public void setUserBaseInfo(BaseUser user) {
        this.salesEmail = user.getUserEmail();
        this.salesPassword = user.getUserPassword();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }


    public int getSalesId() {
        return salesId;
    }

    public void setSalesId(int salesId) {
        this.salesId = salesId;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public String getBranchPin() {
        return branchPin;
    }

    public void setBranchPin(String branchPin) {
        this.branchPin = branchPin;
    }
}
