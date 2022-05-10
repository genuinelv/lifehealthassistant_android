package com.example.lifehealthassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.lifehealthassistant.R;

import java.util.Calendar;
import java.util.Locale;

public class SaveDietActivity extends AppCompatActivity {

    private Spinner spin_dietname;
    private TextView datetext;
    private TextView timetext;
    private TextView dietnametext;
    private int Add_count=0;

    private final LinearLayout linearLayout=findViewById(R.id.layoutButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_diet);
        datetext=(TextView) findViewById(R.id.date_text);
        timetext=(TextView) findViewById(R.id.time_text);
        dietnametext=(TextView) findViewById(R.id.text_dietname);

        spin_dietname=(Spinner) findViewById(R.id.spinner_dietname);
        String[]names=getResources().getStringArray(R.array.dietname);//建立数据源
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);//建立Adapter并且绑定数据源
//第一个参数表示在哪个Activity上显示，第二个参数是系统下拉框的样式，第三个参数是数组。
        spin_dietname.setAdapter(adapter);//绑定Adapter到控件
        spin_dietname.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String strSpinner = names[arg2];
                dietnametext.setText(strSpinner);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void onSelectDate(View v){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(SaveDietActivity.this, null,
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
            datetext.setText(formatDate(year, monthOfYear, dayOfMonth));

            // 关闭dialog
            dialog.dismiss();
        });

    }
    private String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return year + "-" + String.format(Locale.getDefault(), "%02d-%02d", monthOfYear, dayOfMonth);
    }
    public void onSelectTime(View v){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog=new TimePickerDialog(SaveDietActivity.this,
                (timePicker, hourOfDay, minute)->timetext.setText(formatTime(hourOfDay,minute)),//确认按钮
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dialog.show();


    }
    private String formatTime(int hourOfDay, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d:00", hourOfDay, minute);
    }

    public void onAddFood(View v){

        Add_count+=1;

        LinearLayout linearLayout_addFood;
        Button button_addFood;
        EditText editText_addFood;
        LinearLayout.LayoutParams buttonParams1;
        LinearLayout.LayoutParams buttonParams2;
        LinearLayout.LayoutParams buttonParams3;

        linearLayout_addFood=new LinearLayout(this);
        linearLayout_addFood.setOrientation(LinearLayout.HORIZONTAL);
        button_addFood=new Button(this);
        button_addFood.setText("删除");
        editText_addFood=new EditText(this);
        linearLayout_addFood.addView(button_addFood);
        linearLayout_addFood.addView(editText_addFood);
        linearLayout.addView(button_addFood);

        buttonParams1=(LinearLayout.LayoutParams)linearLayout_addFood.getLayoutParams();
        buttonParams1.width=LinearLayout.LayoutParams.MATCH_PARENT;
        buttonParams1.height=LinearLayout.LayoutParams.WRAP_CONTENT;
        linearLayout_addFood.setLayoutParams(buttonParams1);

        buttonParams2=(LinearLayout.LayoutParams)button_addFood.getLayoutParams();
        buttonParams2.width=300;
        buttonParams2.height=LinearLayout.LayoutParams.WRAP_CONTENT;
        button_addFood.setLayoutParams(buttonParams2);

        buttonParams3=(LinearLayout.LayoutParams)editText_addFood.getLayoutParams();
        buttonParams3.width=500;
        buttonParams3.height=LinearLayout.LayoutParams.WRAP_CONTENT;
        editText_addFood.setLayoutParams(buttonParams3);

//        定义一个参数count，每次点击本方法，值+1
//        将该值添加到button及edittext的名字用以区分并可循环得到所有的editext
//        但是我又不知道将button、edittext这些的定义放在方法里面对不对，也觉得这种
//        修改界面布局的方法应该不会成功，一种解决方法是进入本activity时需要添加一个参数指示食物框数
//        即每次点击本按钮重开一遍本activity，又考虑从数据拿出来时候在界面中展示的麻烦，最终不搞了这个很亮眼的功能
    }
}