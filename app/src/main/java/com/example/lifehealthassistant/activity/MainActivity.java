package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.adapter.DataDTOAdapter;
import com.example.lifehealthassistant.adapter.DiseaseAdapter;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.News;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DiseaseService;
import com.example.lifehealthassistant.service.NewsService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String userid;
    private List<News.ResultDTO.DataDTO> dataDTOList=new ArrayList<>();
    private NewsService newsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userid=getIntent().getStringExtra("userid");

        SwipeRefreshLayout swip_refresh_layout=findViewById(R.id.swipeRefresh);
        swip_refresh_layout.setColorSchemeResources(R.color.teal_700);
        swip_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivity.actionStart(MainActivity.this,userid);
            }
        });



        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://v.juhe.cn/toutiao/")
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        newsService = retrofit.create(NewsService.class);
        initDatadtoList();

    }

    private void initDatadtoList() {
        //调用接口方法返回Call对象
        final Call<News> call2 = newsService.getNews();
        call2.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, retrofit2.Response<News> response) {
                System.out.println(response.body());
                dataDTOList=response.body().getResult().getData();
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.datadto_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                DataDTOAdapter dataDTOAdapter =new DataDTOAdapter(dataDTOList);
                recyclerView.setAdapter(dataDTOAdapter);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }

    public static void actionStart(Context context,String id){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onEpidemic(View v){
        Intent intent = new Intent(MainActivity.this,EpidemicActivity.class);
        startActivity(intent);
    }



    public void onExercise(View v){
        Intent intent = new Intent(MainActivity.this,ExerciseActivity.class);
        startActivity(intent);
    }

    public void onMain1(View v){
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onFunction1(View v){
        FunctionActivity.actionStart(MainActivity.this,userid);
        finish();
    }

    public void onPerson1(View v){
        UserActivity.actionStart(MainActivity.this,userid);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}