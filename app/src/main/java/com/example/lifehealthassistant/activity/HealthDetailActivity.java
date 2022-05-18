package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DiseaseService;
import com.example.lifehealthassistant.service.HealthService;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthDetailActivity extends AppCompatActivity {

    private int userid;
    private Health health;
    private String state;//show+delete,insert+load,update+load

    private Button datetime_health_button;
    private TextView datetime_health_text;
    private EditText age_edit;
    private EditText height_edit;
    private EditText weight_edit;
    private EditText bloodpressurehigh_edit;
    private EditText bloodpressurelow_edit;
    private EditText bloodsugar_edit;

    private Button different_button;
    private Button different_button2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_detail);
        userid=getIntent().getIntExtra("userid",userid);
        health=(Health) getIntent().getSerializableExtra("health");
        if(health==null)
            health=new Health();
        state=getIntent().getStringExtra("state");

        datetime_health_button=(Button)findViewById(R.id.datetime_health_button);
        datetime_health_text=(TextView) findViewById(R.id.datetime_health_text);
        age_edit=(EditText) findViewById(R.id.age_edit);
        height_edit=(EditText) findViewById(R.id.height_edit);
        weight_edit=(EditText) findViewById(R.id.weight_edit);
        bloodpressurehigh_edit=(EditText) findViewById(R.id.bloodpressurehigh_edit);
        bloodpressurelow_edit=(EditText) findViewById(R.id.bloodpressurelow_edit);
        bloodsugar_edit=(EditText) findViewById(R.id.bloodsugar_edit);
        different_button=(Button) findViewById(R.id.different_button);
        different_button2=(Button) findViewById(R.id.different_button2);
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        HealthService service = retrofit.create(HealthService.class);
        if(!state.equals("insert")){
            datetime_health_text.setText(health.getDatetimehealth());
            age_edit.setText(String.valueOf(health.getAge()));
            height_edit.setText(String.format("%.2f",health.getHeight()));
            weight_edit.setText(String.format("%.2f",health.getWeight()));
            bloodpressurehigh_edit.setText(String.valueOf(health.getBloodpressurehigh()));
            bloodpressurelow_edit.setText(String.valueOf(health.getBloodpressurelow()));
            bloodsugar_edit.setText(String.format("%.2f",health.getBloodsugar()));
        }


        if(state.equals("show")){
            age_edit.setEnabled(false);
            height_edit.setEnabled(false);
            weight_edit.setEnabled(false);
            bloodpressurehigh_edit.setEnabled(false);
            bloodpressurelow_edit.setEnabled(false);
            bloodsugar_edit.setEnabled(false);
            different_button.setText("删除");
            different_button2.setText("修改");
        }
        else if(state.equals("update")){
            different_button.setText("取消");
            different_button2.setText("保存修改");
        }
        else if(state.equals("insert")){
            different_button.setText("取消");
            different_button2.setText("保存");
        }
        datetime_health_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("show")){
                    Toast.makeText(HealthDetailActivity.this, "无效", Toast.LENGTH_SHORT).show();
                }
                else if(state.equals("update")){
                    Toast.makeText(HealthDetailActivity.this, "不可修改该属性", Toast.LENGTH_SHORT).show();
                }
                else if(state.equals("insert")){
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog=new DatePickerDialog(HealthDetailActivity.this, null,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();

                    // 确认按钮
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
                        // 确认年月日
                        int year = dialog.getDatePicker().getYear();
                        int monthOfYear = dialog.getDatePicker().getMonth() + 1;
                        int dayOfMonth = dialog.getDatePicker().getDayOfMonth();
                        datetime_health_text.setText(formatDate(year, monthOfYear, dayOfMonth));
                        health.setDatetimehealth(formatDate(year, monthOfYear, dayOfMonth)+" 00:00:00");
                        // 关闭dialog
                        dialog.dismiss();
                    });
                }
            }
        });
        different_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("show")){//删除

                    //调用接口方法返回Call对象
                    final Call<Re<String>> call2 = service.deleteHealth(health,userid);
                    call2.enqueue(new Callback<Re<String>>() {
                        @Override
                        public void onResponse(Call<Re<String>> call, retrofit2.Response<Re<String>> response) {
                            System.out.println(response.body());
                        }

                        @Override
                        public void onFailure(Call<Re<String>> call, Throwable t) {

                        }
                    });
                    ShowHealthActivity.actionStart(HealthDetailActivity.this,userid);
                    finish();
                }
                else if(state.equals("update")){//取消
                    finish();
                }
                else if(state.equals("insert")){//取消
                    finish();
                }
            }
        });
        different_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("show")){//修改
                    HealthDetailActivity.actionStart(HealthDetailActivity.this,userid,health,"update");
                }
                else if(state.equals("update")){//保存修改
                    health.setAge(Integer.parseInt(age_edit.getText().toString()));
                    health.setHeight(Double.parseDouble(height_edit.getText().toString()));
                    health.setWeight(Double.parseDouble(weight_edit.getText().toString()));
                    health.setBloodpressurehigh(Integer.parseInt(bloodpressurehigh_edit.getText().toString()));
                    health.setBloodpressurelow(Integer.parseInt(bloodpressurelow_edit.getText().toString()));
                    health.setBloodsugar(Double.parseDouble(bloodsugar_edit.getText().toString()));

                    //调用接口方法返回Call对象
                    final Call<Re<String>> call2 = service.updateHealth(health,userid);
                    call2.enqueue(new Callback<Re<String>>() {
                        @Override
                        public void onResponse(Call<Re<String>> call, retrofit2.Response<Re<String>> response) {
                            System.out.println(response.body());
                        }

                        @Override
                        public void onFailure(Call<Re<String>> call, Throwable t) {

                        }
                    });
                    ShowHealthActivity.actionStart(HealthDetailActivity.this,userid);
                    finish();
                }
                else if(state.equals("insert")){//保存
                    health.setAge(Integer.parseInt(age_edit.getText().toString()));
                    health.setHeight(Double.parseDouble(height_edit.getText().toString()));
                    health.setWeight(Double.parseDouble(weight_edit.getText().toString()));
                    health.setBloodpressurehigh(Integer.parseInt(bloodpressurehigh_edit.getText().toString()));
                    health.setBloodpressurelow(Integer.parseInt(bloodpressurelow_edit.getText().toString()));
                    health.setBloodsugar(Double.parseDouble(bloodsugar_edit.getText().toString()));

                    //调用接口方法返回Call对象
                    final Call<Re<String>> call2 = service.saveHealth(health,userid);
                    call2.enqueue(new Callback<Re<String>>() {
                        @Override
                        public void onResponse(Call<Re<String>> call, retrofit2.Response<Re<String>> response) {
                            System.out.println(response.body());
                        }

                        @Override
                        public void onFailure(Call<Re<String>> call, Throwable t) {

                        }
                    });
                    ShowHealthActivity.actionStart(HealthDetailActivity.this,userid);
                    finish();
                }
            }
        });


    }

    public static void actionStart(Context context, int id, Health health,String state){
        Intent intent=new Intent(context, HealthDetailActivity.class);
        intent.putExtra("userid",id);
        intent.putExtra("health",health);
        intent.putExtra("state",state);
        context.startActivity(intent);
    }
    private String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return year + "-" + String.format(Locale.getDefault(), "%02d-%02d", monthOfYear, dayOfMonth);
    }
}