package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.UserInfoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //登录监听
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userIdText = (EditText) findViewById(R.id.userIdLoginText);
                EditText userPasswordText = (EditText) findViewById(R.id.userPasswordLoginText);
                final String userIdInput = userIdText.getText().toString();
                int idInteger=Integer.parseInt(userIdInput);
                final String userPasswordInput = userPasswordText.getText().toString();
                Log.d("lzn",userIdInput+"++++"+userPasswordInput);

                //创建Retrofit对象
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build();
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
                                Toast.makeText(LoginActivity.this, "没有该用户！", Toast.LENGTH_SHORT).show();
                            }
                            else{//有人

                                User u= response.body().getData();
                                String pwdGet = u.getPassword();
                                Log.d("lzn",pwdGet+"++++"+userPasswordInput);
                                if (pwdGet.equals(userPasswordInput)) {//密码正确
                                    //MainActivity.actionStart(LoginActivity.this,userNameInput,null);//将登录使用的用户名传给下一个活动MainActivity
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    MainActivity.actionStart(LoginActivity.this,idInteger);

                                } else {//密码错误
                                    Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else{
                            //查找失败，请重试
                            Toast.makeText(LoginActivity.this, "查找失败，请重试！", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Re<User>> call, Throwable t) {}
                });

            }
        });



        //注册监听
        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}