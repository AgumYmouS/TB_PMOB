package com.schwarzschild.absenonline;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritActivity extends AppCompatActivity{
    private static final String TAG = "FavoritActivity";

    ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();
    RecyclerView rv_favorit;
    FavoritAdapter favoritAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_favorit);

        favoritAdapter = new FavoritAdapter();
        favoritAdapter.setListMahasiswa(listMahasiswa);
        favoritAdapter.notifyDataSetChanged();

        rv_favorit = findViewById(R.id.rv_favorit);
        rv_favorit.setAdapter(favoritAdapter);
        rv_favorit.setLayoutManager(new LinearLayoutManager(this));
        getFavorit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            getFavorit();
        }
    }

    private void getFavorit(){
        if(isConnected()){
            String API_BASE_URL = "https://api.tigalaskarbeton.com";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiClient client = retrofit.create(ApiClient.class);

            Call<List<Mahasiswa>> call = client.getFavorit(1);

            call.enqueue(new Callback<List<Mahasiswa>>() {
                @Override
                public void onResponse(Call<List<Mahasiswa>> call, Response<List<Mahasiswa>> response) {
                    Toast.makeText(FavoritActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();

                    List<Mahasiswa> listMahaItem = response.body();
                    favoritAdapter.setListMahasiswa(listMahaItem);
                }

                @Override
                public void onFailure(Call<List<Mahasiswa>> call, Throwable t) {
                    Toast.makeText(FavoritActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(FavoritActivity.this, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

