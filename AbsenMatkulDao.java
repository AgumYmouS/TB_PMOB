package com.schwarzschild.absenonline;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
interface AbsenMatkulDao {

    @Query("SELECT * FROM mahasiswas")
    List<AbsenMatkul> getAllAbsenMatkul();

    @Query("DELETE FROM mahasiswas WHERE tanggal = :tanggal AND matakuliah = :matakuliah")
    void delAbsenMatkul(String tanggal, String matakuliah);

    @Query("SELECT * FROM mahasiswas WHERE tanggal = :tanggal AND matakuliah = :matakuliah")
    List<AbsenMatkul> getAbsenMatkul(String tanggal, String matakuliah);

    @Query("UPDATE mahasiswas SET fav = :fav WHERE id = :id")
    void updateMahasiswa(int fav, int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMahasiswa(AbsenMatkul absenMatkul);
}
