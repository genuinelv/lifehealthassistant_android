package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.lifehealthassistant.R;

public class MainActivity extends AppCompatActivity {

    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userid=getIntent().getIntExtra("userid",userid);
        WebView webView = (WebView) findViewById(R.id.news_web_view);
        webView.getSettings ( ).setJavaScriptEnabled(true) ;
        webView.setWebViewClient ( new WebViewClient() );
        webView.loadUrl("https://jiankang.163.com") ;

    }

    public static void actionStart(Context context,int id){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onEpidemic(View v){
        Intent intent = new Intent(MainActivity.this,EpidemicActivity.class);
        startActivity(intent);
    }

    public void onDiet(View v){
        DietActivity.actionStart(MainActivity.this,userid);
    }

    public void onExercise(View v){
        Intent intent = new Intent(MainActivity.this,ExerciseActivity.class);
        startActivity(intent);
    }
    public void onMain(View v){
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void onMedical(View v){
        DiseaseActivity.actionStart(MainActivity.this,userid);
    }

    public void onPerson(View v){
        Intent intent = new Intent(MainActivity.this,UserActivity.class);
        startActivity(intent);
    }

}