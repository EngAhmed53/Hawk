package com.shouman.apps.hawk.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.shouman.apps.hawk.BR;

@Entity(tableName = "clients_table")
public class SalesMan extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    private String salesName;

    private String salesEmail;

    private String salesPhone;

    private String salesPassword;

    private String salesId;

    private String companyId;

    private String branchPin;

    @Ignore
    public SalesMan() {
    }

    public SalesMan(String salesName, String salesEmail, String salesPhone, String salesPassword, String salesId, String companyId, String branchPin) {
        this.salesName = salesName;
        this.salesEmail = salesEmail;
        this.salesPhone = salesPhone;
        this.salesPassword = salesPassword;
        this.salesId = salesId;
        this.companyId = companyId;
        this.branchPin = branchPin;
        this.companyId = companyId;
        this.branchPin = branchPin;
    }

    @Ignore
    public SalesMan(long _id, String salesName, String salesEmail, String salesPhone, String salesPassword, String salesId) {
        this._id = _id;
        this.salesName = salesName;
        this.salesEmail = salesEmail;
        this.salesPhone = salesPhone;
        this.salesPassword = salesPassword;
        this.salesId = salesId;
    }


    public String getSalesName() {
        return salesName;
    }

    public String getSalesEmail() {
        return salesEmail;
    }

    public String getSalesPhone() {
        return salesPhone;
    }

    public String getSalesPassword() {
        return salesPassword;
    }

    public void setUserBaseInfo(BaseUser user) {
        this.salesName = user.getUserName();
        this.salesEmail = user.getUserEmail();
        this.salesPhone = user.getUserPhone();
        this.salesPassword = user.getUserPassword();
    }

    @Bindable
    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
        notifyPropertyChanged(BR._id);
    }

    @Bindable
    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
        notifyPropertyChanged(BR.salesId);
    }

    @Bindable
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
        notifyPropertyChanged(BR.companyId);
    }

    @Bindable
    public String getBranchPin() {
        return branchPin;
    }

    public void setBranchPin(String branchPin) {
        this.branchPin = branchPin;
        notifyPropertyChanged(BR.branchPin);
    }
}
