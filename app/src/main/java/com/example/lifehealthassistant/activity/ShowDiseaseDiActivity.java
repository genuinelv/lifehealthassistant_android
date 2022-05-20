package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DiseaseService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowDiseaseDiActivity extends AppCompatActivity {


    private String userid;
    private Disease disease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_disease_detail);
        userid=getIntent().getStringExtra("userid");
        disease=(Disease) getIntent().getSerializableExtra("disease");
        TextView datestart_text2=(TextView) findViewById(R.id.datestart_text2);
        TextView dateend_text2=(TextView) findViewById(R.id.dateend_text2);
        TextView diseasename_text2=(TextView) findViewById(R.id.diseasename_text2);
        TextView symptom_text2=(TextView) findViewById(R.id.symptom_text2);
        TextView medicine_text2=(TextView) findViewById(R.id.medicine_text2);

        datestart_text2.setText(disease.getDatestart());
        dateend_text2.setText(disease.getDateend());
        diseasename_text2.setText(disease.getDiseasename());
        symptom_text2.setText(disease.getSymptom());
        medicine_text2.setText(disease.getMedicine());
        ImageView sym_pic2=(ImageView) findViewById(R.id.sym_pic2);
        ImageView med_pic2=(ImageView) findViewById(R.id.med_pic2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                for(int i=0;i<2;i++){
                    try {
                        String reallyname;
                        if(i==0){
                            reallyname=disease.getSympic().split("\\\\")[2];
                        }
                        else{
                            reallyname=disease.getMedpic().split("\\\\")[2];
                        }
                        System.out.println(reallyname);

                        url = new URL(ServerConfiguration.IP+"pic_disease/"+reallyname);
                        InputStream inputStream = url.openStream();
                        //　文件保存目录路径
                        String dirName = getExternalFilesDir(null).getPath() + "/MyDownLoad/";
                        // 文件目录
                        File fileDir = new File(dirName);
                        //目录不存在创建
                        if (!fileDir.exists()) {
                            boolean is = fileDir.mkdirs();
                            Log.d("lzn", "创建目录是否成功:" + is);
                        }
                        // 文件路径  dataFileName是后端放回的文件名
                        String fileName = dirName + reallyname;
                        System.out.println(fileName);
                        File file = new File(fileName);
                        if(file.exists()){
                            file.delete();
                        }
                        // 创建文件
                        boolean is = file.createNewFile();
                        Log.d("lzn", "创建文件是否成功:" + is);
                        // 写文件数据
                        //创建字节流
                        byte[] bs = new byte[1024];
                        int len;
                        OutputStream os = new FileOutputStream(fileName);
                        //写数据
                        while ((len = inputStream.read(bs)) != -1) {
                            os.write(bs, 0, len);
                        }
                        Bitmap bitmap = BitmapFactory.decodeFile(fileName);

                        if(i==0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sym_pic2.setImageBitmap(bitmap);
                                }
                            });
                        }
                        else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    med_pic2.setImageBitmap(bitmap);
                                }
                            });
                        }
                        //完成后关闭流
                        os.close();
                        inputStream.close();
                    } catch ( IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    public static void actionStart(Context context, String id, Disease disease){
        Intent intent=new Intent(context,ShowDiseaseDiActivity.class);
        intent.putExtra("userid",id);
        intent.putExtra("disease",disease);
        context.startActivity(intent);
    }

    public void onDelete(View v){
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        DiseaseService service = retrofit.create(DiseaseService.class);
        //调用接口方法返回Call对象
        final Call<Re> call2 = service.delete(disease,userid);
        call2.enqueue(new Callback<Re>() {
            @Override
            public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                System.out.println(response.body());
                if(response.body().getMessage()!=null)
                    Toast.makeText(ShowDiseaseDiActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Re> call, Throwable t) {

            }
        });
        ShowDiseaseActivity.actionStart(ShowDiseaseDiActivity.this,userid,null);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}