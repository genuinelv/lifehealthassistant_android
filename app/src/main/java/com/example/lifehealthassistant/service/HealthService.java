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
    public Call<Re<String>> saveHealth(@Body Health health, @Query("id") Integer id);


    @GET("health")
    public Call<Re<List<Health>>> getAll(@Query("id") Integer id);

    @HTTP(method = "PUT",path = "health",hasBody = true)
    public Call<Re<String>> updateHealth(@Body Health health, @Query("id") Integer id);

    @HTTP(method = "DELETE",path = "health",hasBody = true)
    public Call<Re<String>> deleteHealth(@Body Health health, @Query("id") Integer id);
}
