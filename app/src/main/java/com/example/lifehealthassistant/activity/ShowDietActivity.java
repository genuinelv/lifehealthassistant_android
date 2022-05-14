package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DietService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowDietActivity extends AppCompatActivity {

    private int userid;

    private String date_all;
    private String dietname_all;

    private Spinner spin_dietname;
    private TextView timetext;
    private TextView foodtext;
    private ImageView picture1;
    private ImageView picture2;
    private ImageView picture3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diet);
        userid=getIntent().getIntExtra("userid",1);
        timetext=(TextView)findViewById(R.id.time_text2);
        foodtext=(TextView)findViewById(R.id.food_text);
        picture1=(ImageView) findViewById(R.id.picture1_2);
        picture2=(ImageView) findViewById(R.id.picture2_2);
        picture3=(ImageView) findViewById(R.id.picture3_2);

        spin_dietname=(Spinner) findViewById(R.id.spinner_dietname2);
        String[]names=getResources().getStringArray(R.array.dietname);//建立数据源
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        //建立Adapter并且绑定数据源
        //第一个参数表示在哪个Activity上显示，第二个参数是系统下拉框的样式，第三个参数是数组。
        spin_dietname.setAdapter(adapter);//绑定Adapter到控件
        spin_dietname.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String strSpinner = names[arg2];
                //dietnametext.setText(strSpinner);
                dietname_all=strSpinner;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static void actionStart(Context context, int id){
        Intent intent=new Intent(context,ShowDietActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onSelectDate(View v){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(ShowDietActivity.this, null,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

        //确认按钮
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
            // 确认年月日
            int year = dialog.getDatePicker().getYear();
            int monthOfYear = dialog.getDatePicker().getMonth() + 1;
            int dayOfMonth = dialog.getDatePicker().getDayOfMonth();
            //datetext.setText(formatDate(year, monthOfYear, dayOfMonth));
            date_all=formatDate(year, monthOfYear, dayOfMonth);
            // 关闭dialog
            dialog.dismiss();
        });
    }
    private String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return year + "-" + String.format(Locale.getDefault(), "%02d-%02d", monthOfYear, dayOfMonth);
    }

    public void onSelectDiet(View v){
        Diet diet =new Diet(date_all+" 00:00:00",null,dietname_all);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        DietService service = retrofit.create(DietService.class);
        //调用接口方法返回Call对象
        final Call<Re<Diet>> call2 = service.getDiet(diet,userid);
        call2.enqueue(new Callback<Re<Diet>>() {
            @Override
            public void onResponse(Call<Re<Diet>> call, retrofit2.Response<Re<Diet>> response) {
                System.out.println(response.body());
                Diet getDiet=response.body().getData();
                timetext.setText(getDiet.getDatetime().split(" ")[1]);
                foodtext.setText(getDiet.getFood());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL url = null;
                        for(int i=0;i<3;i++){
                            try {
                                String reallyname;
                                if(i==0){
                                    reallyname=getDiet.getPicture1().split("\\\\")[2];
                                }
                                else if(i==1) {
                                    reallyname=getDiet.getPicture2().split("\\\\")[2];
                                }
                                else{
                                    reallyname=getDiet.getPicture3().split("\\\\")[2];
                                }
                                System.out.println(reallyname);

                                url = new URL(ServerConfiguration.IP+"pic_diet/"+reallyname);
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
                                            picture1.setImageBitmap(bitmap);
                                        }
                                    });
                                }
                                else if(i==1) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            picture2.setImageBitmap(bitmap);
                                        }
                                    });
                                }
                                else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            picture3.setImageBitmap(bitmap);
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

            @Override
            public void onFailure(Call<Re<Diet>> call, Throwable t) {

            }
        });
    }
}