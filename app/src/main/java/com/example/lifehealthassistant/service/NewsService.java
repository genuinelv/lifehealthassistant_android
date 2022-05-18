package com.example.lifehealthassistant.service;

import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.News;
import com.example.lifehealthassistant.bean.Re;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {

    @GET("index?type=jiankang&key=b646373d5b1b0d7628c7ae1bfae514d0")
    public Call<News> getNews();
}
