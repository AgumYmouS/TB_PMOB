package com.schwarzschild.absenonline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InputActivity extends AppCompatActivity {

    private static final String TAG = "InputActivity";

    static final int REQUEST_KAMERA = 1;
    public ImageView images;
    public String iTanggal;
    public String iMatkul;
    EditText eNama;
    EditText eBp;
    Bitmap imageBitmap;
    ApiClient client;
    Button aButton, sButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_absen);

        eNama = findViewById(R.id.i_nama);
        eBp = findViewById(R.id.i_bp);
        images = findViewById(R.id.i_foto);
        iTanggal = getCurrentDate();

        if (getIntent().hasExtra("matkul")) {
            iMatkul = getIntent().getStringExtra("matkul");
        }

        aButton = (Button) findViewById(R.id.selfie);
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_KAMERA);
            }
        });

        sButton = (Button) findViewById(R.id.done);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(eNama.getText())){
                    eNama.setError("Nama is required!");
                }else if(TextUtils.isEmpty(eBp.getText())){
                    eBp.setError("BP is required!");
                }else {
                    upload(imageBitmap);
                    Intent intent = new Intent(v.getContext(), AbsenActivity.class);
                    intent.putExtra("matkul", iMatkul);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_KAMERA && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            images.setImageBitmap(imageBitmap);
            images.setVisibility(View.VISIBLE);
            sButton.setEnabled(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upload(Bitmap bitmap){

        String iNama = eNama.getText().toString();
        String iBp = eBp.getText().toString();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("nama", createPartFromString(iNama));
        map.put("nim", createPartFromString(iBp));
        map.put("tanggal", createPartFromString(iTanggal));
        map.put("matakuliah", createPartFromString(iMatkul));
        map.put("fav", createPartFromString("0"));


        File file = createTempFile(bitmap);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);

        if(isConnected()){
            String API_BASE_URL = "https://api.tigalaskarbeton.com";

            Gson gson = new GsonBuilder().setLenient().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            client = retrofit.create(ApiClient.class);

            Call<Mahasiswa> call = client.createMahasiswa(body, map);

            call.enqueue(new Callback<Mahasiswa>() {
                @Override
                public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Mahasiswa> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(InputActivity.this, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.webp");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.WEBP,0, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public String getCurrentDate() {
        Date anotherCurDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateString = formatter.format(anotherCurDate);
        return formattedDateString;
    }

    @Override
    public void onBackPressed() {
        Intent detailIntent = new Intent(this, AbsenActivity.class);
        detailIntent.putExtra("matkul", iMatkul);
        startActivityForResult(detailIntent, 1);
        super.onBackPressed();
    }
}
