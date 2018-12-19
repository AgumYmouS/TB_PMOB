package com.schwarzschild.absenonline;

import android.os.Parcel;
import android.os.Parcelable;

public class Matkul implements Parcelable {
    String shift;
    String matkul;

    public Matkul(){}

    protected Matkul(Parcel in) {
        this.shift = in.readString();
        this.matkul = in.readString();
    }

    public static final Creator<Matkul> CREATOR = new Creator<Matkul>() {
        @Override
        public Matkul createFromParcel(Parcel in) {
            return new Matkul(in);
        }

        @Override
        public Matkul[] newArray(int size) {
            return new Matkul[size];
        }
    };

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getMatkul() {
        return matkul;
    }

    public void setMatkul(String matkul) {
        this.matkul = matkul;
    }

    public Matkul(String shift, String matkul) {
        this.shift = shift;
        this.matkul = matkul;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shift);
        dest.writeString(this.matkul);
    }
}
