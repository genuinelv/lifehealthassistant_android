package com.example.lifehealthassistant.service;

import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Re;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DietService {

    @Multipart
    @POST("diet/save_diet_pic")
    public Call<Re<Diet>> saveDietPic(@Part List<MultipartBody.Part> parts);

    @POST("diet/save_diet")
    public Call<Re> saveDiet(@Body Diet diet, @Query("id") String id);

    @POST("diet/get_diet")
    public Call<Re<Diet>> getDiet(@Body Diet diet, @Query("id") String id);

    @GET("diet/get_diet_all")
    public Call<Re<List<Diet>>> getDietAll(@Query("id") String id);

    @GET("diet/get_diet_content")
    public Call<Re<List<Diet>>> getDietByFood(@Query("id") String id,@Query("food") String food);

    @HTTP(method = "DELETE",path = "diet/delete_diet",hasBody = true)
    public Call<Re> delete(@Body Diet diet, @Query("id") String id);
}
