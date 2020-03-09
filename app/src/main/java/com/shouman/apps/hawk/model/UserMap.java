package com.shouman.apps.hawk.model;

public class UserMap {
    private String userUID;
    private String path;
    private String branchUID;
    private boolean isVerified;

    public UserMap() {
    }

    public UserMap(String userUID, String path, String branchUID, boolean isVerified) {
        this.userUID = userUID;
        this.path = path;
        this.branchUID = branchUID; //only for sales_members
        this.isVerified = isVerified;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBranchUID() {
        return branchUID;
    }

    public void setBranchUID(String branchUID) {
        this.branchUID = branchUID;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

}
