package com.shouman.apps.hawk.data.model;

public class DailyLogEntry {
    //this is used for the each sales member log
    private String customerName;
    private String customerCompanyName;
    private boolean newCustomer;
    private long timeMillieSeconds;
    private String CUID;

    public DailyLogEntry() {
    }

    public DailyLogEntry(String customerName, String customerCompanyName, boolean newCustomer, long timeMillieSeconds, String cuid) {
        this.customerName = customerName;
        this.customerCompanyName = customerCompanyName;
        this.newCustomer = newCustomer;
        this.timeMillieSeconds = timeMillieSeconds;
        CUID = cuid;
    }

    public String getCUID() {
        return CUID;
    }

    public void setCUID(String CUID) {
        this.CUID = CUID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCompanyName() {
        return customerCompanyName;
    }

    public void setCustomerCompanyName(String customerCompanyName) {
        this.customerCompanyName = customerCompanyName;
    }

    public boolean isNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(boolean newCustomer) {
        this.newCustomer = newCustomer;
    }

    public long getTimeMillieSeconds() {
        return timeMillieSeconds;
    }

    public void setTimeMillieSeconds(long timeMillieSeconds) {
        this.timeMillieSeconds = timeMillieSeconds;
    }
}
