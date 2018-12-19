package com.schwarzschild.absenonline;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("/api/mahasiswa/search")
    Call<List<Mahasiswa>> getMahasiswa(@Query("tgl") String tgl, @Query("mk") String mk);

    @GET("/api/mahasiswa/fav")
    Call<List<Mahasiswa>> getFavorit(@Query("fav") int fav);

    @FormUrlEncoded
    @POST("/api/mahasiswa/{id}")
    Call<Mahasiswa> updateMahasiswa(@Path("id") int id, @Field("fav") int fav);

    @Multipart
    @POST("/api/mahasiswa")
    Call<Mahasiswa> createMahasiswa(@Part MultipartBody.Part image, @PartMap Map<String,RequestBody> text);

}
