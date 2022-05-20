package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.adapter.DiseaseAdapter;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DiseaseService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowDiseaseActivity extends AppCompatActivity {

    private String userid;
    private List<Disease> diseaseList=new ArrayList<>();
    private DiseaseService service;
    private EditText select_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_disease);
        userid=getIntent().getStringExtra("userid");
        String typename=getIntent().getStringExtra("typename");
        System.out.println(typename);
        select_edit=(EditText)findViewById(R.id.select_edit);

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        service = retrofit.create(DiseaseService.class);

        if(typename==null){
            initDiseaseList();
        }
        else{
            initDiseaseListByName(typename);
        }


    }
    public static void actionStart(Context context, String id,String typename){
        Intent intent=new Intent(context,ShowDiseaseActivity.class);
        intent.putExtra("userid",id);
        intent.putExtra("typename",typename);
        context.startActivity(intent);
    }
    private void initDiseaseListByName(String typename) {
        Log.d("lzn", typename);
        //调用接口方法返回Call对象
        final Call<Re<List<Disease>>> call2 = service.getByName(userid,typename);
        call2.enqueue(new Callback<Re<List<Disease>>>() {
            @Override
            public void onResponse(Call<Re<List<Disease>>> call, retrofit2.Response<Re<List<Disease>>> response) {
                System.out.println(response.body());
                diseaseList=response.body().getData();
                System.out.println(diseaseList);
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.disease_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShowDiseaseActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                DiseaseAdapter diseaseAdapter =new DiseaseAdapter(diseaseList,userid);
                recyclerView.setAdapter(diseaseAdapter);
            }

            @Override
            public void onFailure(Call<Re<List<Disease>>> call, Throwable t) {

            }
        });
    }

    private void initDiseaseList() {

        //调用接口方法返回Call对象
        final Call<Re<List<Disease>>> call2 = service.getAll(userid);
        call2.enqueue(new Callback<Re<List<Disease>>>() {
            @Override
            public void onResponse(Call<Re<List<Disease>>> call, retrofit2.Response<Re<List<Disease>>> response) {
                System.out.println(response.body());
                diseaseList=response.body().getData();
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.disease_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShowDiseaseActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                DiseaseAdapter diseaseAdapter =new DiseaseAdapter(diseaseList,userid);
                recyclerView.setAdapter(diseaseAdapter);
            }

            @Override
            public void onFailure(Call<Re<List<Disease>>> call, Throwable t) {

            }
        });
    }

    public void onSelect(View view){
        String selectitem=select_edit.getText().toString();
        System.out.println(selectitem);
        ShowDiseaseActivity.actionStart(ShowDiseaseActivity.this,userid,selectitem);
        finish();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}