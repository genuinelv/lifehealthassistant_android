package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lifehealthassistant.R;

public class DiseaseActivity extends AppCompatActivity {

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);
        userid=getIntent().getStringExtra("userid");
    }

    public static void actionStart(Context context, String id){
        Intent intent=new Intent(context,DiseaseActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onSaveDisease(View view){
        SaveDiseaseActivity.actionStart(DiseaseActivity.this,userid);
    }

    public void onShowDisease(View view){
        ShowDiseaseActivity.actionStart(DiseaseActivity.this,userid,null);
    }
    public void onSaveRemind(View view){

    }
    public void onShowRemind(View view){

    }
    public void onMain2(View view){
        MainActivity.actionStart(DiseaseActivity.this,userid);
    }
    public void onMedical2(View view){
        DiseaseActivity.actionStart(DiseaseActivity.this,userid);
    }
    public void onPerson2(View view){
        UserActivity.actionStart(DiseaseActivity.this,userid);
    }



}