package com.schwarzschild.absenonline;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AbsenActivity extends AppCompatActivity implements MahasiswaAdapter.OnItemClick{

    private static final String TAG = "AbsenActivity";

    ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();
    RecyclerView rv_mahasiswa;
    ProgressBar progressBar;
    MahasiswaAdapter mahasiswaAdapter;
    String matkulName;
    String tanggal;

    AppDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_absen);
        Log.d(TAG, String.valueOf(listMahasiswa.size()));

        getMatkulIntent();

        tanggal = getCurrentDate();

        mahasiswaAdapter = new MahasiswaAdapter();
        mahasiswaAdapter.setListMahasiswa(listMahasiswa);
        mahasiswaAdapter.setClickHandler(this);
        mahasiswaAdapter.notifyDataSetChanged();

        rv_mahasiswa = findViewById(R.id.rv_mahasiswa);
        rv_mahasiswa.setAdapter(mahasiswaAdapter);
        rv_mahasiswa.setLayoutManager(new LinearLayoutManager(this));

        mDb = Room.databaseBuilder(this, AppDatabase.class, "maha.db")
                .allowMainThreadQueries()
                .build();

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int mode = preferences.getInt("display_status_key", 1);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        rv_mahasiswa.setVisibility(View.VISIBLE);

        if(mode == 1) {
            getAbsenMahasiswa();
        }

        Button mButton = (Button) findViewById(R.id.absen);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InputActivity.class);
                intent.putExtra("matkul", matkulName);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            getMatkulIntent();
            refreshData();
        }
    }

    private void refreshData() {
        mahasiswaAdapter.notifyDataSetChanged();
    }

    private void getMatkulIntent() {
        Log.d(TAG, "getMatkulIntent: checking.");
        if (getIntent().hasExtra("matkul")) {
            matkulName = getIntent().getStringExtra("matkul");
        }

        TextView text = findViewById(R.id.pilmatkul);
        text.setText(matkulName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.refresh){
            Toast.makeText(this, "Refreshing Data...", Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
            int mode = preferences.getInt("display_status_key", 1);

            if(isConnected()){
            if(mode == 1) {
                getAbsenMahasiswa();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
            }else{
                Toast.makeText(this, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAbsenMahasiswa(){

        progressBar.setVisibility(View.VISIBLE);
        rv_mahasiswa.setVisibility(View.INVISIBLE);

        if(isConnected()){
            String API_BASE_URL = "https://api.tigalaskarbeton.com";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiClient client = retrofit.create(ApiClient.class);

            Call<List<Mahasiswa>> call = client.getMahasiswa(tanggal, matkulName);

            Log.d(TAG, "getAbsenMahasiswa: ok.");

            call.enqueue(new Callback<List<Mahasiswa>>() {
                @Override
                public void onResponse(Call<List<Mahasiswa>> call, Response<List<Mahasiswa>> response) {
                    Log.d(TAG, "onResponse: getdata.");
                    Toast.makeText(AbsenActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();

                    List<Mahasiswa> listMahaItem = response.body();

                    mDb.absenMatkulDao().delAbsenMatkul(tanggal, matkulName);
                    saveMahasiswaToDb(listMahaItem);

                    mahasiswaAdapter.setListMahasiswa(listMahaItem);

                    progressBar.setVisibility(View.INVISIBLE);
                    rv_mahasiswa.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<List<Mahasiswa>> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+ t.toString());
                    Toast.makeText(AbsenActivity.this, "Gagal", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.INVISIBLE);
                    rv_mahasiswa.setVisibility(View.VISIBLE);
                }
            });
        }else{
            List<AbsenMatkul> listNow = mDb.absenMatkulDao().getAbsenMatkul(tanggal, matkulName);
            List<Mahasiswa> mahasiswaList = new ArrayList<>();
            for(AbsenMatkul absmtk: listNow) {
                Mahasiswa m = new Mahasiswa(
                        absmtk.id,
                        absmtk.nama,
                        absmtk.nim,
                        absmtk.tanggal,
                        absmtk.matkul,
                        absmtk.foto,
                        absmtk.fav,
                        absmtk.foto_link
                );
                mahasiswaList.add(m);
            }
            mahasiswaAdapter.setListMahasiswa(mahasiswaList);
            progressBar.setVisibility(View.INVISIBLE);
            rv_mahasiswa.setVisibility(View.VISIBLE);
        }
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("display_status_key", 1);
        editor.commit();
    }

    public void saveMahasiswaToDb(List<Mahasiswa> mahasiswaList){

        for(Mahasiswa m : mahasiswaList){
            AbsenMatkul absmtk = new AbsenMatkul();
            absmtk.id = m.getId();
            absmtk.nama = m.getNama();
            absmtk.nim = m.getNim();
            absmtk.tanggal = m.getTanggal();
            absmtk.matkul = m.getMatakuliah();
            absmtk.foto = m.getFoto();
            absmtk.fav = m.getFav();

            mDb.absenMatkulDao().insertAllMahasiswa(absmtk);

        }
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public void mahasiswaClick(Mahasiswa mahasiswa) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra("key_maha_parcelable", mahasiswa);
        startActivity(detailIntent);
    }

    public String getCurrentDate() {
        Date anotherCurDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateString = formatter.format(anotherCurDate);
        return formattedDateString;
    }

    @Override
    public void onBackPressed() {
        Intent detailIntent = new Intent(this, MainActivity.class);
        startActivityForResult(detailIntent,1);
        super.onBackPressed();
    }
}
