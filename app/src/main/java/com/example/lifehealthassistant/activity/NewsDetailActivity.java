package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lifehealthassistant.R;

public class NewsDetailActivity extends AppCompatActivity {

    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        url=getIntent().getStringExtra("url");
        WebView webView = (WebView) findViewById(R.id.news_web_view);
        webView.getSettings ( ).setJavaScriptEnabled(true) ;
        webView.setWebViewClient ( new WebViewClient() );
        webView.loadUrl(url) ;
    }
    public static void actionStart(Context context, String url){
        Intent intent=new Intent(context,NewsDetailActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
}