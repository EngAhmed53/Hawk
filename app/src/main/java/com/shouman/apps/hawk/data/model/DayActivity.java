package com.shouman.apps.hawk.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DayActivity implements Parcelable {
    private String activityType;
    private String salesName;
    private String customerUID;
    private String customerName;
    private long activityTime;

    public DayActivity(String activityType, String salesName, String customerUID, String customerName, long activityTime) {
        this.activityType = activityType;
        this.salesName = salesName;
        this.customerUID = customerUID;
        this.customerName = customerName;
        this.activityTime = activityTime;
    }

    public DayActivity() {
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getCustomerUID() {
        return customerUID;
    }

    public void setCustomerUID(String customerUID) {
        this.customerUID = customerUID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(long activityTime) {
        this.activityTime = activityTime;
    }

    protected DayActivity(Parcel in) {
        activityType = in.readString();
        salesName = in.readString();
        customerUID = in.readString();
        customerName = in.readString();
        activityTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activityType);
        dest.writeString(salesName);
        dest.writeString(customerUID);
        dest.writeString(customerName);
        dest.writeLong(activityTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DayActivity> CREATOR = new Parcelable.Creator<DayActivity>() {
        @Override
        public DayActivity createFromParcel(Parcel in) {
            return new DayActivity(in);
        }

        @Override
        public DayActivity[] newArray(int size) {
            return new DayActivity[size];
        }
    };
}
