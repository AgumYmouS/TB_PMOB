package com.schwarzschild.absenonline;

import android.os.Parcel;
import android.os.Parcelable;

public class Mahasiswa implements Parcelable {
    int id;
    String nama;
    int nim;
    String tanggal;
    String matakuliah;
    String foto;
    int fav;
    String foto_link;

    public Mahasiswa(String nama, int nim, String tanggal, String matakuliah, String foto, int fav) {
        this.nama = nama;
        this.nim = nim;
        this.tanggal = tanggal;
        this.matakuliah = matakuliah;
        this.foto = foto;
        this.fav = fav;
    }

    public Mahasiswa(int id, String nama, int nim, String tanggal, String matakuliah, String foto,int fav, String foto_link) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
        this.tanggal = tanggal;
        this.matakuliah = matakuliah;
        this.foto = foto;
        this.fav = fav;
        this.foto_link = foto_link;
    }

    protected Mahasiswa(Parcel in) {
        this.id = in.readInt();
        this.nama = in.readString();
        this.nim = in.readInt();
        this.tanggal = in.readString();
        this.matakuliah = in.readString();
        this.foto = in.readString();
        this.fav = in.readInt();
        this.foto_link = in.readString();
    }

    public static final Parcelable.Creator<Mahasiswa> CREATOR = new Parcelable.Creator<Mahasiswa>() {
        @Override
        public Mahasiswa createFromParcel(Parcel source) {
            return new Mahasiswa(source);
        }

        @Override
        public Mahasiswa[] newArray(int size) {
            return new Mahasiswa[size];
        }
    };

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getFotoLink() {
        return foto_link;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNim(int nim) {
        this.nim = nim;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setMatakuliah(String matakuliah) {
        this.matakuliah = matakuliah;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getNim() {
        return nim;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getMatakuliah() {
        return matakuliah;
    }

    public String getFoto() {
        return foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nama);
        dest.writeInt(this.nim);
        dest.writeString(this.tanggal);
        dest.writeString(this.matakuliah);
        dest.writeString(this.foto);
        dest.writeInt(this.fav);
        dest.writeString(this.foto_link);
    }
}



