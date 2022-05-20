package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.lifehealthassistant.R;

public class FatActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText fat_height_edit;
    private EditText fat_weight_eidt;
    private TextView fat_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fat);

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        fat_height_edit=(EditText) findViewById(R.id.fat_height_edit);
        fat_weight_eidt=(EditText) findViewById(R.id.fat_weight_edit);
        fat_text=(TextView) findViewById(R.id.fat_text);
    }

    public void onFatCompute(View v){
        String sex="男";
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rd = (RadioButton) radioGroup.getChildAt(i);
            if (rd.isChecked()) {
                sex=rd.getText().toString();
                break;
            }
        }
        String fat_height=fat_height_edit.getText().toString();
        String fat_weight=fat_weight_eidt.getText().toString();

        double fat_height_double=Double.parseDouble(fat_height);//腰围
        double fat_weight_double=Double.parseDouble(fat_weight);
        double fat;
        if(sex.equals("男")){
            double a=fat_height_double*0.74;
            double b=fat_weight_double*0.082+44.74;
            fat=(a-b)/fat_weight_double*100;
        }
        else{
            double a=fat_height_double*0.74;
            double b=fat_weight_double*0.082+34.89;
            fat=(a-b)/fat_weight_double*100;
        }

        fat_text.setText("您的体脂率为："+String.format("%.2f", fat)+"%。");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}