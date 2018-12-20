package com.schwarzschild.absenonline;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements ShareActionProvider.OnShareTargetSelectedListener {

    private static final String TAG = "DetailActivity";

    private ShareActionProvider mShare;

    TextView dNama;
    TextView dBp;
    ImageView dFoto;
    Mahasiswa dMahasiswa;
    ToggleButton dtoggleButton;
    ApiClient client;
    int fav;
    AppDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_mahasiswa);


        dNama = findViewById(R.id.h_nama);
        dBp = findViewById(R.id.h_bp);
        dFoto = findViewById(R.id.h_foto);
        dtoggleButton = findViewById(R.id.toggleButton);

        mDb = Room.databaseBuilder(this, AppDatabase.class, "maha.db")
                .allowMainThreadQueries()
                .build();

        Intent detailIntent = getIntent();
        if(null != detailIntent) {
            dMahasiswa = detailIntent.getParcelableExtra("key_maha_parcelable");
        }

        if(dMahasiswa != null){
            getSupportActionBar().setTitle(dMahasiswa.getNama().toUpperCase());
            dNama.setText(dMahasiswa.getNama());
            dBp.setText(String.valueOf(dMahasiswa.getNim()));
            fav = dMahasiswa.getFav();
            if(fav == 0){
                dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_gray));
                dtoggleButton.setChecked(false);
            }else if(fav == 1){
                dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_yellow));
                dtoggleButton.setChecked(true);
            }

            if(isConnected()){
                String url = dMahasiswa.getFotoLink();
                Glide.with(this)
                        .load(url)
                        .into(dFoto);
            }else{
                Toast.makeText(DetailActivity.this, "Failed To Load Photo!", Toast.LENGTH_SHORT).show();
            }
        }

        dtoggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                if(isConnected()) {
                    String API_BASE_URL = "https://api.tigalaskarbeton.com";

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    client = retrofit.create(ApiClient.class);
                    int id = dMahasiswa.getId();
                    if (checked) {
                        Call<Mahasiswa> call = client.updateMahasiswa(id, 1);
                        Log.d(TAG, "onCheckedChanged: ok");
                        call.enqueue(new Callback<Mahasiswa>() {
                            @Override
                            public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                                Toast.makeText(getApplicationContext(), "Ditambahkan Ke Favorit", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Mahasiswa> call, Throwable t) {
                                Log.d(TAG, "onFailure: " + t);
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                        mDb.absenMatkulDao().updateMahasiswa(1, id);
                        dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_yellow));
                    } else {
                        Call<Mahasiswa> call = client.updateMahasiswa(id, 0);
                        call.enqueue(new Callback<Mahasiswa>() {
                            @Override
                            public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                                Toast.makeText(getApplicationContext(), "DiHapus Dari Favorit", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Mahasiswa> call, Throwable t) {
                                Log.d(TAG, "onFailure: " + t);
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                        mDb.absenMatkulDao().updateMahasiswa(0, id);
                        dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_gray));
                    }
                }else{
                    Toast.makeText(DetailActivity.this, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public void onBackPressed() {
        Intent detailIntent =new Intent(this, AbsenActivity.class);
        detailIntent.putExtra("key_maha_parcelable", dMahasiswa);
        detailIntent.putExtra("matkul", dMahasiswa.matakuliah);
        startActivityForResult(detailIntent, 1);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        int id = item.getItemId();
        mShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if(mShare != null){
            Uri uri = Uri.parse(dMahasiswa.getFotoLink());
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, dNama.getText());
            shareIntent.putExtra(Intent.EXTRA_TEXT, dBp.getText());
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mShare.setShareIntent(shareIntent);
        }
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        Toast.makeText(this, intent.getComponent().toString(), Toast.LENGTH_LONG).show();
        return (false);
    }
}
