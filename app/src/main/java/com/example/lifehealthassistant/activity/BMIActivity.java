package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lifehealthassistant.R;

public class BMIActivity extends AppCompatActivity {

    private EditText height_edit;
    private EditText weight_eidt;
    private TextView bmi_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        height_edit=(EditText) findViewById(R.id.height_edit);
        weight_eidt=(EditText) findViewById(R.id.weight_edit);
        bmi_text=(TextView) findViewById(R.id.bmi_text);


    }
    public void onBMICompute(View v){
        String height=height_edit.getText().toString();
        String weight=weight_eidt.getText().toString();

        double height_double=Double.parseDouble(height)/100;
        double weight_double=Double.parseDouble(weight);
        double BMI=weight_double/(height_double*height_double);
        String rec="评价为：";
        if(BMI<18.5)
        {
            rec+="偏瘦";
        }
        else if(BMI>=18.5&&BMI<24){
            rec+="正常";
        }
        else if(BMI>=24&&BMI<28){
            rec+="超重";
        }
        else if(BMI>=28&&BMI<30){
            rec+="肥胖";
        }
        else{
            rec+="高度肥胖";
        }
        bmi_text.setText("您的健康指数为："+String.format("%.2f", BMI)+"。"+rec);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}