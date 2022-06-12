package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private EditText userIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        userIdText = (EditText) findViewById(R.id.userIdLoginText);
        EditText userPasswordText = (EditText) findViewById(R.id.userPasswordLoginText);
        TextView getPS_text=(TextView)findViewById(R.id.getPS_text);
        getPS_text.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        getPS_text.getPaint().setAntiAlias(true);//抗锯齿

        Log.d("lzn", "onCreate: "+ServerConfiguration.IP);
        boolean isRemember=sharedPreferences.getBoolean("remember_password",false);
        if(isRemember){
            String account=sharedPreferences.getString("account","");
            String passw=sharedPreferences.getString("password","");
            userIdText.setText(account);
            userPasswordText.setText(passw);
            rememberPass.setChecked(true);

        }

        //登录监听
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userIdInput = userIdText.getText().toString();
                final String userPasswordInput = userPasswordText.getText().toString();
                if(userIdInput.length()>=30||userPasswordInput.length()>=16){
                    Toast.makeText(LoginActivity.this, "用户名要在30位以内，密码要在16位以内", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("lzn",userIdInput+"++++"+userPasswordInput);

                //创建Retrofit对象
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build();
                //生成接口对象
                UserInfoService service = retrofit.create(UserInfoService.class);

                //调用接口方法返回Call对象
                final Call<Re<User>> call = service.getById(userIdInput);
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
                                if (pwdGet.equals(userPasswordInput)) {//密码正确
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    editor=sharedPreferences.edit();
                                    if(rememberPass.isChecked()){
                                        editor.putBoolean("remember_password",true);
                                        editor.putString("account",userIdInput);
                                        editor.putString("password",pwdGet);
                                    }
                                    else
                                        editor.clear();
                                    editor.commit();
                                    MainActivity.actionStart(LoginActivity.this,userIdInput);
                                    finish();

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

    public void onGetPS(View view){
        final String userIdInput = userIdText.getText().toString();
        if(userIdInput==null){
            Toast.makeText(LoginActivity.this, "没有输入用户名！", Toast.LENGTH_SHORT).show();
            return;
        }
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        //生成接口对象
        UserInfoService service = retrofit.create(UserInfoService.class);

        //调用接口方法返回Call对象
        final Call<Re<User>> call = service.getById(userIdInput);
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
                        //调用接口方法返回Call对象
                        final Call<Re<String>> call2 = service.getps(userIdInput);
                        //发布异步请求
                        call2.enqueue(new Callback<Re<String>>() {

                            @Override
                            public void onResponse(Call<Re<String>> call, retrofit2.Response<Re<String>> response) {
                                if(response.body()==null)
                                    Log.d("lzn","666666666");
                                Log.d("lzn",response.body().toString());
                                if(response.body().getMessage()!=null)
                                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<Re<String>> call, Throwable t) {}
                        });
                    }

                }else{
                    //查找失败，请重试
                    Toast.makeText(LoginActivity.this, "请重试！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Re<User>> call, Throwable t) {}
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}