package com.example.lifehealthassistant.service;

import android.util.Log;

import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.bean.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInfoService {

    @GET("user/{id}")
    public Call<Re<User>> getById(@Path("id") int id);

    @POST("user")
    public Call<Re<User>> save(@Body User user);

    @Multipart
    @PUT("user")
    public Call<Re<User>> update(@Part MultipartBody.Part part);
    @PUT("user/{id}")
    public Call<Re<String>> update(@Body User user,@Path("id") int id);

    @DELETE("user/{id}")
    public Call<Re<String>> deleteById(@Path("id") int id);



}
