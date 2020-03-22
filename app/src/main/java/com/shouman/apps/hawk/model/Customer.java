package com.shouman.apps.hawk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

    //customer name
    private String n;

    //customer latitude;
    private double lt;

    //customer longitude
    private double ln;

    //company name
    private String cn;

    //customer phone number
    private String p;

    //customer email
    //can be empty or null
    private String e;

    //customer extra information;
    //can be extra or null
    private String ei;

    public Customer() {
    }

    public Customer(String n, double lt, double ln, String cn, String p, String e, String ei) {
        this.n = n;
        this.lt = lt;
        this.ln = ln;
        this.cn = cn;
        this.p = p;
        this.e = e;
        this.ei = ei;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public double getLt() {
        return lt;
    }

    public void setLt(double lt) {
        this.lt = lt;
    }

    public double getLn() {
        return ln;
    }

    public void setLn(double ln) {
        this.ln = ln;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getEi() {
        return ei;
    }

    public void setEi(String ei) {
        this.ei = ei;
    }

    protected Customer(Parcel in) {
        n = in.readString();
        lt = in.readDouble();
        ln = in.readDouble();
        cn = in.readString();
        p = in.readString();
        ei = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(n);
        dest.writeDouble(lt);
        dest.writeDouble(ln);
        dest.writeString(cn);
        dest.writeString(p);
        dest.writeString(e);
        dest.writeString(ei);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
