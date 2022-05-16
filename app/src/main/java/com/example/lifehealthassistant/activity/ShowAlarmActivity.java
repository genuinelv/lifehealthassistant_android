package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;

import com.example.lifehealthassistant.R;

public class ShowAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        startActivity(intent);
    }
}