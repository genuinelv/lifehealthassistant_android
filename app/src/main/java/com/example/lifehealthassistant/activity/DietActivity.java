package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lifehealthassistant.R;

public class DietActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
    }

    public void onSaveDiet(View v){
        //Toast.makeText(DietActivity.this, "点击该按钮！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DietActivity.this,SaveDietActivity.class);
        startActivity(intent);
    }

    public void onSearchDiet(View v){
        Intent intent = new Intent(DietActivity.this,DietActivity.class);
        startActivity(intent);
    }

    public void onFood(View v){
        Intent intent = new Intent(DietActivity.this,FoodActivity.class);
        startActivity(intent);
    }
}