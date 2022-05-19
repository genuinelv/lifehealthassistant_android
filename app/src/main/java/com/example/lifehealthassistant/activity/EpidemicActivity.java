package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lifehealthassistant.R;

public class EpidemicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epidemic);

        WebView webView = (WebView) findViewById(R.id.epidemic_web_view);
        webView.getSettings ( ).setJavaScriptEnabled(true) ;
        webView.setWebViewClient ( new WebViewClient() );
        webView.setBackgroundColor(0);
        webView.loadUrl("https://news.sina.cn/zt_d/yiqing0121") ;
        //webView.loadUrl("https://dfzximg02.dftoutiao.com/news/20220518/20220518175905_f5e59737e37885a1e903c0d29f1bb93e_1_mwpm_03201609.jpeg") ;
    }
}