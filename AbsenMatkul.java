package com.schwarzschild.absenonline;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "mahasiswas")
public class AbsenMatkul {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "nama")
    public String nama;

    @ColumnInfo(name = "nim")
    public int nim;

    @ColumnInfo(name = "tanggal")
    public String tanggal;

    @ColumnInfo(name = "matakuliah")
    public String matkul;

    @ColumnInfo(name = "foto")
    public String foto;

    @ColumnInfo(name = "fav")
    public int fav;

    @ColumnInfo(name = "foto_link")
    public String foto_link;
}
