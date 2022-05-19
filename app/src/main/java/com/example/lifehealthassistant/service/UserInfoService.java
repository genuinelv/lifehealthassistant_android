package com.example.lifehealthassistant.service;

import android.util.Log;

import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.bean.User;
import com.example.lifehealthassistant.bean.Useremail;

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
    public Call<Re<User>> getById(@Path("id") String id);

    @POST("user")
    public Call<Re<User>> save(@Body User user);

    @Multipart
    @PUT("user")
    public Call<Re<User>> update(@Part MultipartBody.Part part);
    @PUT("user/{id}")
    public Call<Re> update(@Body User user,@Path("id") String id);
    @PUT("user/ps/{id}")
    public Call<Re> updatePs(@Body User user,@Path("id") String id);

    @DELETE("user/{id}")
    public Call<Re> deleteById(@Path("id") String id);


    @GET("email/{id}")
    public Call<Re<Useremail>> getEmailById(@Path("id") String id);
    @PUT("email/sendbindcode")
    public Call<Re> sendbindcode(@Query("id") String id,@Query("email") String email);
    @PUT("email/sendupdatecode")
    public Call<Re> sendupdatecode(@Query("id") String id);
    @PUT("email/getps")
    public Call<Re<String>> getps(@Query("id") String id);
    @PUT("email/checkcode")
    public Call<Re> checkcode(@Query("id") String id,@Query("code") String code,@Query("state") Integer i);



}
