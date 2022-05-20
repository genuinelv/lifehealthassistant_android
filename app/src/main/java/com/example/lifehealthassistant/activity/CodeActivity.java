package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.adapter.HealthAdapter;
import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.bean.User;
import com.example.lifehealthassistant.bean.Useremail;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.HealthService;
import com.example.lifehealthassistant.service.UserInfoService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CodeActivity extends AppCompatActivity {

    private String userid;
    private int state;
    private String email;
    // 1
    // 2
    // 3,5
    // 4

    private EditText code_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        code_edittext=(EditText) findViewById(R.id.code_edittext);
        userid=getIntent().getStringExtra("userid");
        Log.d("lzn", "onCreate: "+userid);
        state=getIntent().getIntExtra("state",1);
        email=getIntent().getStringExtra("email");

        TextView code_text=(TextView) findViewById(R.id.code_text);
        Button code_button=(Button) findViewById(R.id.code_button);
        if(state==1){
            code_text.setText("邮箱：");
            //code_button.setText("获取验证码");
        }
        else if(state==2){
            code_text.setText("邮箱：");
            code_edittext.setText(email);
            code_edittext.setEnabled(false);
            //code_button.setText("获取验证码");
        }
        else if(state==3||state==5){
            code_text.setText("验证码：");
            //code_button.setText("验证");
        }
        else if(state==4){
            code_text.setText("密码：");
            //code_button.setText("修改");
        }


    }
    public static void actionStart(Context context, String id,  int state,String email){
        Intent intent=new Intent(context, CodeActivity.class);
        intent.putExtra("userid",id);
        intent.putExtra("state",state);
        intent.putExtra("email",email);
        context.startActivity(intent);
    }
    public void onCode(View view){Log.d("lzn", "onCode-1: "+userid);
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        UserInfoService service = retrofit.create(UserInfoService.class);
        if(state==1){
            String email=code_edittext.getText().toString();
            Log.d("lzn", "onCode0: "+userid);
            //调用接口方法返回Call对象
            final Call<Re> call2 = service.sendbindcode(userid,email);
            Log.d("lzn", "onCode: "+userid+email);//482087021@qq.com
            call2.enqueue(new Callback<Re>() {
                @Override
                public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                    System.out.println(response.body());
                    if(response.body().getMessage()!=null)
                        Toast.makeText(CodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    CodeActivity.actionStart(CodeActivity.this,userid,3,null);
                    finish();
                }

                @Override
                public void onFailure(Call<Re> call, Throwable t) {

                }
            });
        }
        else if(state==2){
            //调用接口方法返回Call对象
            final Call<Re> call2 = service.sendupdatecode(userid);
            call2.enqueue(new Callback<Re>() {
                @Override
                public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                    System.out.println(response.body());
                    if(response.body().getMessage()!=null)
                        Toast.makeText(CodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    CodeActivity.actionStart(CodeActivity.this,userid,5,null);
                    finish();
                }

                @Override
                public void onFailure(Call<Re> call, Throwable t) {

                }
            });
        }
        else if(state==3||state==5){
            String code=code_edittext.getText().toString();
            //调用接口方法返回Call对象
            final Call<Re> call2;
            if(state==3)
                call2= service.checkcode(userid,code,1);
            else //if(state==5)
                call2 = service.checkcode(userid,code,2);
            call2.enqueue(new Callback<Re>() {
                @Override
                public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                    System.out.println(response.body());
                    if(response.body().getMessage()!=null)
                        Toast.makeText(CodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if(state==3){
                        UserActivity.actionStart(CodeActivity.this,userid);
                        finish();
                    }
                    else {//5
                        CodeActivity.actionStart(CodeActivity.this,userid,4,null);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Re> call, Throwable t) {

                }
            });
        }
        else if(state==4){
            String ps=code_edittext.getText().toString();
            User user=new User();
            user.setId(userid);
            user.setPassword(ps);
            //调用接口方法返回Call对象
            final Call<Re> call2 = service.updatePs(user,userid);
            call2.enqueue(new Callback<Re>() {
                @Override
                public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                    System.out.println(response.body());

                    if(response.body().getMessage()!=null)
                        Toast.makeText(CodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    UserActivity.actionStart(CodeActivity.this,userid);
                    finish();
                }

                @Override
                public void onFailure(Call<Re> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
