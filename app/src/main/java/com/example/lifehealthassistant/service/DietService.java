package com.example.lifehealthassistant.service;

import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Re;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DietService {

    @Multipart
    @POST("diet/save_diet_pic")
    public Call<Re<Diet>> saveDietPic(@Part List<MultipartBody.Part> parts);

    @POST("diet/save_diet")
    public Call<Re<String>> saveDiet(@Body Diet diet, @Query("id") Integer id);

    @POST("diet/get_diet")
    public Call<Re<Diet>> getDiet(@Body Diet diet, @Query("id") Integer id);
}