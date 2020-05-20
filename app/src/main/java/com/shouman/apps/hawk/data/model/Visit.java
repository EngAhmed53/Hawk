package com.shouman.apps.hawk.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Visit implements Parcelable {
    private String visitNote;
    private long visitTime;
    private int visitCache;

    public Visit(String visitNote, long visitTime, int visitCache) {
        this.visitNote = visitNote;
        this.visitTime = visitTime;
        this.visitCache = visitCache;
    }

    public Visit() {
    }

    public String getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(String visitNote) {
        this.visitNote = visitNote;
    }

    public long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(long visitTime) {
        this.visitTime = visitTime;
    }

    protected Visit(Parcel in) {
        visitNote = in.readString();
        visitTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(visitNote);
        dest.writeLong(visitTime);
        dest.writeInt(visitCache);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Visit> CREATOR = new Parcelable.Creator<Visit>() {
        @Override
        public Visit createFromParcel(Parcel in) {
            return new Visit(in);
        }

        @Override
        public Visit[] newArray(int size) {
            return new Visit[size];
        }
    };

    public int getVisitCache() {
        return visitCache;
    }

    public void setVisitCache(int visitCache) {
        this.visitCache = visitCache;
    }
}
