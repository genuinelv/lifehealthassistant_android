package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lifehealthassistant.R;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        WebView webView = (WebView) findViewById(R.id.exercise_web_view);
        webView.getSettings ( ).setJavaScriptEnabled(true) ;
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient ( new WebViewClient() );
        webView.loadUrl("https://www.hiyd.com/") ;

    }
    public void onBMI(View v){
        Intent intent = new Intent(ExerciseActivity.this,BMIActivity.class);
        startActivity(intent);
    }
    public void onFat(View v){
        Intent intent = new Intent(ExerciseActivity.this,FatActivity.class);
        startActivity(intent);

    }
}