package com.example.lifehealthassistant.service;

import android.util.Log;

import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.bean.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInfoService {

    @GET("user/{id}")
    public Call<Re<User>> getById(@Path("id") int id);

    @GET("user")
    public Call<Re<List<User>>> getall();

    @POST("user")
    public Call<Re> save(@Body User user);

    @PUT("Ui-Gui/test/put/{name}")
    public Call<User> testput(@Path("name") String name);

    @DELETE("Ui-Gui/test/delete/{name}")
    public Call<User> testdelete(@Path("name") String name);

}
