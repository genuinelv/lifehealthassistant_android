package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.adapter.DiseaseAdapter;
import com.example.lifehealthassistant.adapter.HealthAdapter;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DiseaseService;
import com.example.lifehealthassistant.service.HealthService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowHealthActivity extends AppCompatActivity {

    private int userid;
    private List<Health> healthList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_health);
        userid=getIntent().getIntExtra("userid",1);

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        HealthService service = retrofit.create(HealthService.class);

        //调用接口方法返回Call对象
        final Call<Re<List<Health>>> call2 = service.getAll(userid);
        call2.enqueue(new Callback<Re<List<Health>>>() {
            @Override
            public void onResponse(Call<Re<List<Health>>> call, retrofit2.Response<Re<List<Health>>> response) {
                System.out.println(response.body());
                healthList=response.body().getData();
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.health_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShowHealthActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                HealthAdapter healthAdapter =new HealthAdapter(healthList,userid);
                recyclerView.setAdapter(healthAdapter);
            }

            @Override
            public void onFailure(Call<Re<List<Health>>> call, Throwable t) {

            }
        });

    }

    public static void actionStart(Context context, int id){
        Intent intent=new Intent(context, ShowHealthActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onSaveHealth(View v){
        HealthDetailActivity.actionStart(ShowHealthActivity.this,userid,null,"insert");
    }
}