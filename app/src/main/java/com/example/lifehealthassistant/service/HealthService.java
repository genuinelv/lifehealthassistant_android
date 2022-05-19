package com.example.lifehealthassistant.service;

import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.Re;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HealthService {

    @POST("health")
    public Call<Re> saveHealth(@Body Health health, @Query("id") String id);


    @GET("health")
    public Call<Re<List<Health>>> getAll(@Query("id") String id);

    @HTTP(method = "PUT",path = "health",hasBody = true)
    public Call<Re> updateHealth(@Body Health health, @Query("id") String id);

    @HTTP(method = "DELETE",path = "health",hasBody = true)
    public Call<Re> deleteHealth(@Body Health health, @Query("id") String id);
}
