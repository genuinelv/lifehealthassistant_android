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
import android.widget.TextView;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.adapter.DietAdapter;
import com.example.lifehealthassistant.adapter.DiseaseAdapter;
import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DietService;
import com.example.lifehealthassistant.service.DiseaseService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowDietAllActivity extends AppCompatActivity {
    private String userid;
    private List<Diet> dietList=new ArrayList<>();
    private int count;
    private DietService service;
    private EditText select_dietall_edit;
    private TextView foodcount_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diet_all);

        userid=getIntent().getStringExtra("userid");
        String typename=getIntent().getStringExtra("typename");
        count=getIntent().getIntExtra("count",0);
        System.out.println(typename);
        foodcount_text=(TextView)findViewById(R.id.foodcount_text);

        select_dietall_edit=(EditText)findViewById(R.id.select_dietall_edit);
        select_dietall_edit.setText(typename);
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        service = retrofit.create(DietService.class);

        if(typename==null){
            initDiseaseList();
        }
        else{
            initDietListByFood(typename);

        }
    }
    public static void actionStart(Context context, String id, String typename,int count){
        Intent intent=new Intent(context,ShowDietAllActivity.class);
        intent.putExtra("userid",id);
        intent.putExtra("typename",typename);
        intent.putExtra("count",count);
        context.startActivity(intent);
    }
    private void initDietListByFood(String typename) {
        Log.d("lzn", typename);
        //调用接口方法返回Call对象
        final Call<Re<List<Diet>>> call2 = service.getDietByFood(userid,typename);
        call2.enqueue(new Callback<Re<List<Diet>>>() {
            @Override
            public void onResponse(Call<Re<List<Diet>>> call, retrofit2.Response<Re<List<Diet>>> response) {
                System.out.println(response.body());
                dietList=response.body().getData();
                System.out.println(dietList);
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.diet_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShowDietAllActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                DietAdapter dietAdapter =new DietAdapter(dietList,userid);
                recyclerView.setAdapter(dietAdapter);
                foodcount_text.setText("您记录的饮食中，总共"+count+"餐的情况下，有"+dietList.size()+"餐食用了"+typename+"。");
            }

            @Override
            public void onFailure(Call<Re<List<Diet>>> call, Throwable t) {

            }
        });
    }

    private void initDiseaseList() {

        //调用接口方法返回Call对象
        final Call<Re<List<Diet>>> call2 = service.getDietAll(userid);
        call2.enqueue(new Callback<Re<List<Diet>>>() {
            @Override
            public void onResponse(Call<Re<List<Diet>>> call, retrofit2.Response<Re<List<Diet>>> response) {
                System.out.println(response.body());
                dietList=response.body().getData();
                System.out.println(dietList);
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.diet_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShowDietAllActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                DietAdapter dietAdapter =new DietAdapter(dietList,userid);
                recyclerView.setAdapter(dietAdapter);
            }

            @Override
            public void onFailure(Call<Re<List<Diet>>> call, Throwable t) {

            }
        });
    }

    public void onSelectDietAll(View view){
        String selectitem=select_dietall_edit.getText().toString();
        System.out.println(selectitem);
        ShowDietAllActivity.actionStart(ShowDietAllActivity.this,userid,selectitem,dietList.size());
        finish();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}