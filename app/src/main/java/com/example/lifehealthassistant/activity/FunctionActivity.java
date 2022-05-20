package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lifehealthassistant.R;

public class FunctionActivity extends AppCompatActivity {

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        userid=getIntent().getStringExtra("userid");
    }

    public static void actionStart(Context context, String id){
        Intent intent=new Intent(context,FunctionActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }
    public void onSaveDiet_func(View v){
        SaveDietActivity.actionStart(FunctionActivity.this,userid);
    }
    public void onShowDietByDate_func(View v){
        ShowDietActivity.actionStart(FunctionActivity.this,userid);
    }
    public void onShowDietByFood_func(View v){
        ShowDietAllActivity.actionStart(FunctionActivity.this,userid,null,0);
    }
    public void onFood_func(View v){
        Intent intent = new Intent(FunctionActivity.this,FoodActivity.class);
        startActivity(intent);
    }
    public void onSaveDisease_func(View v){
        SaveDiseaseActivity.actionStart(FunctionActivity.this,userid);

    }
    public void onShowDisease_func(View v){
        ShowDiseaseActivity.actionStart(FunctionActivity.this,userid,null);
    }
    public void onBMI_func(View v){
        Intent intent = new Intent(FunctionActivity.this,BMIActivity.class);
        startActivity(intent);
    }
    public void onFat_func(View v){
        Intent intent = new Intent(FunctionActivity.this,FatActivity.class);
        startActivity(intent);
    }
    public void onMain2(View view){
        MainActivity.actionStart(FunctionActivity.this,userid);
        finish();
    }
    public void onFunction2(View view){
        FunctionActivity.actionStart(FunctionActivity.this,userid);
        finish();
    }
    public void onPerson2(View view){
        UserActivity.actionStart(FunctionActivity.this,userid);
        finish();
    }

}