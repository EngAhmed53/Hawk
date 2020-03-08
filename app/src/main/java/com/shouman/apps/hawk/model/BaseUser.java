package com.shouman.apps.hawk.model;

public class BaseUser {

    private String userEmail;
    private String userPassword;
    private boolean isVerified;
    private String userType;

    public BaseUser() {
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(int type) {
        if (type == 0) {
            this.userType = "company";
        } else {
            this.userType = "sales_member";
        }
    }

    public BaseUser(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
