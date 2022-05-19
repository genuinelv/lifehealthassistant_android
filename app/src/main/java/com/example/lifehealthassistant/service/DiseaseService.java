package com.example.lifehealthassistant.service;

import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Re;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DiseaseService {
    @Multipart
    @POST("disease/save_disease_pic")
    public Call<Re<Disease>> saveDiseasePic(@Part List<MultipartBody.Part> parts);

    @POST("disease/save_disease")
    public Call<Re> saveDisease(@Body Disease disease, @Query("id") String id);

    @GET("disease/get_diseasebyname")
    public Call<Re<List<Disease>>> getByName(@Query("id") String id, @Query("diseasename") String name);

    @GET("disease/get_diseaseall")
    public Call<Re<List<Disease>>> getAll(@Query("id") String id);


    @HTTP(method = "DELETE",path = "disease/delete_disease",hasBody = true)
    public Call<Re> delete(@Body Disease disease, @Query("id") String id);

}
