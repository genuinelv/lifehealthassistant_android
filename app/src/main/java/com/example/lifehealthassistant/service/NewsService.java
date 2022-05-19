package com.example.lifehealthassistant.service;

import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.News;
import com.example.lifehealthassistant.bean.Re;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    //key=3dc86b09a2ee2477a5baa80ee70fcdf5
    //key=22a108244dbb8d1f49967cd74a0c144d
    //key=2aba5ed71d6d00fe39c7eb5012b128ed
    //key=b646373d5b1b0d7628c7ae1bfae514d0
    //key=136c500303493492d6f855c6a62f48ee
    //key=5bc999ffcd1dda45b35f6b00fc40fea1
    //key=304e0e410d0de9779780d834f6a01956//10
    @GET("index?type=jiankang&key=5bc999ffcd1dda45b35f6b00fc40fea1")
    public Call<News> getNews();
}
