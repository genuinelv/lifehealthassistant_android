package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.bean.User;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DietService;
import com.example.lifehealthassistant.service.UserInfoService;
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

public class UserActivity extends AppCompatActivity {

    private String userid;
    private TextView username_show_text;
    private TextView usersex_show_text;
    private ImageView userpic_show_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userid=getIntent().getStringExtra("userid");
        username_show_text=(TextView) findViewById(R.id.username_show_text);
        usersex_show_text=(TextView) findViewById(R.id.usersex_show_text);
        userpic_show_image=(ImageView) findViewById(R.id.userpic_show_image);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        UserInfoService service = retrofit.create(UserInfoService.class);
        //调用接口方法返回Call对象
        final Call<Re<User>> call2 = service.getById(userid);
        System.out.println(userid);
        call2.enqueue(new Callback<Re<User>>() {
            @Override
            public void onResponse(Call<Re<User>> call, retrofit2.Response<Re<User>> response) {
                System.out.println(response.body());
                User getUser=response.body().getData();
                username_show_text.setText(getUser.getName());
                usersex_show_text.setText(getUser.getSex());
                if(getUser.getPhoto()!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            URL url = null;
                            try {
                                String reallyname;
                                reallyname=getUser.getPhoto().split("\\\\")[2];
                                System.out.println(reallyname);

                                url = new URL(ServerConfiguration.IP+"pic_user/"+reallyname);

                                InputStream inputStream = url.openStream();
                                //　文件保存目录路径
                                String dirName = getExternalFilesDir(null).getPath() + "/MyDownLoad/";
                                // 文件目录
                                File fileDir = new File(dirName);System.out.println("到这了");
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

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        userpic_show_image.setImageBitmap(bitmap);
                                    }
                                });

                                //完成后关闭流
                                os.close();
                                inputStream.close();
                            } catch ( IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }

            }

            @Override
            public void onFailure(Call<Re<User>> call, Throwable t) {

            }
        });

    }

    public static void actionStart(Context context, String id){
        Intent intent=new Intent(context, UserActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }
    public void onDeleteUser(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(UserActivity.this);
        builder.setTitle("是否要注销该账号");
        builder.setMessage("注销将会删除该账号的所有信息");
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                        .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
                //生成接口对象
                UserInfoService service = retrofit.create(UserInfoService.class);
                //调用接口方法返回Call对象
                final Call<Re> call2 = service.deleteById(userid);
                call2.enqueue(new Callback<Re>() {
                    @Override
                    public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                        System.out.println(response.body());
                        if(response.body().getMessage()!=null)
                            Toast.makeText(UserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Re> call, Throwable t) {

                    }
                });
                Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();



    }

    public void onUpdateUInfo(View v){
        UpdateUserActivity.actionStart(UserActivity.this,userid);
    }
    public void onShowHealth(View v){
        ShowHealthActivity.actionStart(UserActivity.this,userid);
    }
    public void onExit(View v){
        Intent intent = new Intent(UserActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void onMain3(View view){
        MainActivity.actionStart(UserActivity.this,userid);
    }
    public void onMedical3(View view){
        DiseaseActivity.actionStart(UserActivity.this,userid);
    }
    public void onPerson3(View view){
        UserActivity.actionStart(UserActivity.this,userid);
    }

    public void onBindEmail(View view){
        Log.d("lzn", "onBindEmail: "+userid);
        CodeActivity.actionStart(UserActivity.this,userid,1,null);
    }
    public void onUpdatePs(View view){
        Log.d("lzn", "onUpdatePs: 修改密码操作");
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        //生成接口对象
        UserInfoService service = retrofit.create(UserInfoService.class);
        //调用接口方法返回Call对象
        final Call<Re<String>> call2 = service.getps(userid);
        //发布异步请求
        call2.enqueue(new Callback<Re<String>>() {

            @Override
            public void onResponse(Call<Re<String>> call, retrofit2.Response<Re<String>> response) {
                if(response.body()==null)
                    Log.d("lzn","666666666");
                Log.d("lzn",response.body().toString());
                String email=response.body().getData();
                if(email==null){
                    CodeActivity.actionStart(UserActivity.this,userid,4,null);
                }
                else{
                    CodeActivity.actionStart(UserActivity.this,userid,2,email);
                }

            }

            @Override
            public void onFailure(Call<Re<String>> call, Throwable t) {}
        });


    }

}