package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lifehealthassistant.R;

public class DietActivity extends AppCompatActivity {

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        userid=getIntent().getStringExtra("userid");
    }

    public static void actionStart(Context context, String id){
        Intent intent=new Intent(context,DietActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onSaveDiet(View v){
        //Toast.makeText(DietActivity.this, "点击该按钮！", Toast.LENGTH_SHORT).show();
        SaveDietActivity.actionStart(DietActivity.this,userid);
    }

    public void onShowDiet(View v){
        ShowDietActivity.actionStart(DietActivity.this,userid);
    }

    public void onDietAll(View v){
        ShowDietAllActivity.actionStart(DietActivity.this,userid,null);
    }

    public void onFood(View v){
        Intent intent = new Intent(DietActivity.this,FoodActivity.class);
        startActivity(intent);
    }
}