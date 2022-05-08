package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lifehealthassistant.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    public void onUserInfo(View v){
        Intent intent = new Intent(UserActivity.this,EpidemicActivity.class);
        startActivity(intent);
    }

    public void onUpdateUInfo(View v){
        Intent intent = new Intent(UserActivity.this,DietActivity.class);
        startActivity(intent);
    }

}