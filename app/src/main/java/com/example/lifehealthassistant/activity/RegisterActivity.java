package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.bean.User;
import com.example.lifehealthassistant.service.UserInfoService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //创建Retrofit对象
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.51.71/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();


        //注册监听
        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* 查询表中有无该username；
                if(没有){
                    判断两次输入的密码是否相同
                    if(相同){
                        将用户名、密码、默认初始余额加入user数据库表
                    }
                    else
                        提示输入的密码不一致
                }
                else
                    提示用户已存在，注册失败；
*/
                //从用户表中查找出输入的用户名对应的元组
                EditText userIdText = (EditText) findViewById(R.id.userIdRegisterText);
                EditText userPasswordText = (EditText) findViewById(R.id.userPasswordRegisterText);
                EditText userPasswordAgainText = (EditText) findViewById(R.id.userPasswordRegisterAgainText);
                final String userIdInput = userIdText.getText().toString();
                int idInteger = Integer.parseInt(userIdInput);
                final String userPasswordInput = userPasswordText.getText().toString();
                final String userPasswordAgainInput = userPasswordAgainText.getText().toString();

                //生成接口对象
                UserInfoService service = retrofit.create(UserInfoService.class);

                //调用接口方法返回Call对象
                final Call<Re<User>> call = service.getById(idInteger);
                //发布异步请求
                call.enqueue(new Callback<Re<User>>() {

                    @Override
                    public void onResponse(Call<Re<User>> call, retrofit2.Response<Re<User>> response) {
                        if(response.body()==null)
                            Log.d("lzn","666666666");
                        Log.d("lzn",response.body().toString());
                        if(response.body().getFlag()){
                            if(response.body().getData()==null){
                                //无人
                                Toast.makeText(RegisterActivity.this, "没有该用户,可创建！", Toast.LENGTH_SHORT).show();
                                registerTodb(idInteger,userPasswordInput,userPasswordAgainInput);
                            }
                            else{//有人
                                Toast.makeText(RegisterActivity.this, "该用户已存在！", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            //查找失败，请重试
                            Toast.makeText(RegisterActivity.this, "请重试！", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Re<User>> call, Throwable t) {}
                });

            }
        });

        //取消注册
        Button cancelRegister = (Button) findViewById(R.id.cancelRegister_button);
        cancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerTodb(int idInteger,String userPasswordInput,String userPasswordAgainInput){
        if (userPasswordInput.equals(userPasswordAgainInput)) {//两次密码一致
            User user=new User(idInteger,"系统用户",userPasswordInput);

            //生成接口对象
            UserInfoService service = retrofit.create(UserInfoService.class);
            //调用接口方法返回Call对象
            final Call<Re<User>> call = service.save(user);
            //发布异步请求
            call.enqueue(new Callback<Re<User>>() {

                @Override
                public void onResponse(Call<Re<User>> call, retrofit2.Response<Re<User>> response) {
                    if(response.body()==null)
                        Log.d("lzn","666666666");
                    Log.d("lzn",response.body().toString());
                    if(response.body().getFlag()){//成功创建

                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();

                    }else{//失败，请重试

                        Toast.makeText(RegisterActivity.this, "创建失败，请重试！", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Re<User>> call, Throwable t) {}
            });

        }else//密码不一致
            Toast.makeText(RegisterActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();

    }
}