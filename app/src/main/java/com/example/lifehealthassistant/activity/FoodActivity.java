package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lifehealthassistant.R;

public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        WebView webView = (WebView) findViewById(R.id.food_web_view);
        webView.getSettings ( ).setJavaScriptEnabled(true) ;
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient ( new WebViewClient() );
        webView.loadUrl("https://www.meishij.net/shicai/") ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}