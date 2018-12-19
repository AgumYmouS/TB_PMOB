package com.schwarzschild.absenonline;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public TextView tgl;
    ArrayList<Matkul> listMatkul = new ArrayList<Matkul>();
    RecyclerView rv_matkul;
    RecyclerView.Adapter matkulAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");
        getDataMatkul();
        matkulAdapter = new MatkulAdapter(listMatkul);

        rv_matkul = findViewById(R.id.rv_matkul);
        rv_matkul.setAdapter(matkulAdapter);
        rv_matkul.setHasFixedSize(true);
        rv_matkul.setLayoutManager(new LinearLayoutManager(this));

        String tanggal_sekarang = getCurrentDate();
        tgl = findViewById(R.id.isi);
        tgl.setText(tanggal_sekarang);

        Button fButton = (Button) findViewById(R.id.fav_button);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FavoritActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public ArrayList<Matkul> getDataMatkul(){

        listMatkul.add(new Matkul("SHIFT 1", "Pemrograman Mobile A"));
        listMatkul.add(new Matkul("SHIFT 2", "Pemrograman Mobile B"));
        listMatkul.add(new Matkul("SHIFT 3", "Pemrograman Mobile C"));
        listMatkul.add(new Matkul("SHIFT 4", "Pemrograman Mobile D"));

        return listMatkul;
    }

    public String getCurrentDate() {
        Date anotherCurDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd-MMMM-yyyy");
        String formattedDateString = formatter.format(anotherCurDate);
        return formattedDateString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            getDataMatkul();
        }
    }
}
