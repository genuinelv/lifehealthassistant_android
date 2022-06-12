package com.example.lifehealthassistant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DietService;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SaveDietActivity extends AppCompatActivity {

    private String userid;
    private Spinner spin_dietname;
    private TextView datetext;
    private TextView timetext;
    private TextView dietnametext;
    private EditText editText_food;

    private int photoCount=0;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private ImageView picture1;
    private ImageView picture2;
    private ImageView picture3;
    private Uri imageUri;



    //封装成Diet
    private String date_all;
    private String time_all;
    private String dietname_all;
    private String food_all;
    private String picture_uri_1;
    private String picture_uri_2;
    private String picture_uri_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_diet);
        datetext=(TextView) findViewById(R.id.date_text);
        timetext=(TextView) findViewById(R.id.time_text);
        dietnametext=(TextView) findViewById(R.id.text_dietname);
        picture1=(ImageView) findViewById(R.id.picture1);
        picture2=(ImageView) findViewById(R.id.picture2);
        picture3=(ImageView) findViewById(R.id.picture3);
        editText_food=(EditText)findViewById(R.id.food_edittext);
        userid=getIntent().getStringExtra("userid");


        spin_dietname=(Spinner) findViewById(R.id.spinner_dietname);
        String[]names=getResources().getStringArray(R.array.dietname);//建立数据源
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        //建立Adapter并且绑定数据源
        //第一个参数表示在哪个Activity上显示，第二个参数是系统下拉框的样式，第三个参数是数组。
        spin_dietname.setAdapter(adapter);//绑定Adapter到控件
        spin_dietname.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String strSpinner = names[arg2];
                dietnametext.setText(strSpinner);
                dietname_all=strSpinner;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static void actionStart(Context context, String id){
        Intent intent=new Intent(context,SaveDietActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
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
            date_all=formatDate(year, monthOfYear, dayOfMonth);
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
                (timePicker, hourOfDay, minute)->set_time_all(formatTime(hourOfDay,minute)),//确认按钮
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dialog.show();
    }
    private void set_time_all(String timeget){
        time_all=timeget;
        timetext.setText(timeget);
    }
    private String formatTime(int hourOfDay, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d:00", hourOfDay, minute);
    }

    public void onCamera(View view) throws IOException {
        photoCount+=1;
        if(photoCount>3)
        {
            Toast.makeText(SaveDietActivity.this, "照片数量需要小于等于3！", Toast.LENGTH_SHORT).show();
            return;
        }
        //创建File对象，用于存储拍照后的图片
        //Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM+File.separator+"Camera"+File.separator
        String reallypath=getExternalCacheDir()+"/output_image"+System.currentTimeMillis()+".jpg";
        File outputImage = new File(reallypath) ;


        if(photoCount==1){
            picture_uri_1=reallypath;
        }
        else if(photoCount==2){
            picture_uri_2=reallypath;
        }
        else{
            picture_uri_3=reallypath;
        }
        try{
            if (outputImage.exists()) {
                outputImage.delete( );
            }
            outputImage.createNewFile( );
        }catch ( IOException e) {
            e.printStackTrace( ) ;
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(SaveDietActivity.this,
                    "com.example.lifehealthassistant.fileprovider", outputImage) ;
        }else {
            imageUri = Uri.fromFile(outputImage) ;
        }
        //启动相机程序
        Intent intent = new Intent ( "android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri) ;
        startActivityForResult(intent,TAKE_PHOTO) ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(imageUri));
                        if(photoCount==1){
                            picture1.setImageBitmap(bitmap);
                            //picture_uri_1=imageUri.getPath();
                        }
                        else if(photoCount==2){
                            picture2.setImageBitmap(bitmap);
                            //picture_uri_2=imageUri.getPath();
                        }
                        else{
                            picture3.setImageBitmap(bitmap);
                            //picture_uri_3=imageUri.getPath();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {

                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }
                    else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }

    }
    public void onPhoto(View view){
        photoCount+=1;
        if(photoCount>3)
        {
            Toast.makeText(SaveDietActivity.this, "照片数量需要小于等于3！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ContextCompat.checkSelfPermission(SaveDietActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SaveDietActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else
            openAlbum();
    }
    private void openAlbum(){
        Intent intent = new Intent ( "android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO); //打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    openAlbum();
                else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
            else if("content".equalsIgnoreCase(uri.getScheme())){

                imagePath=getImagePath(uri,null);
            }
            else if("file".equalsIgnoreCase(uri.getScheme())){

                imagePath= uri.getPath();
            }
            displayImage(imagePath);
        }
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor = getContentResolver( ).query(uri, null, selection,null,null);
        if ( cursor != null){
            if ( cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if(photoCount==1){
                picture1.setImageBitmap(bitmap);
                picture_uri_1=imagePath;
            }
            else if(photoCount==2){
                picture2.setImageBitmap(bitmap);
                picture_uri_2=imagePath;
            }
            else{
                picture3.setImageBitmap(bitmap);
                picture_uri_3=imagePath;
            }
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAllLoad(View view){
        food_all=editText_food.getText().toString();
        String datetime_all=date_all+" "+time_all;
        if(picture_uri_1==null||picture_uri_2==null||picture_uri_3==null){
            Toast.makeText(this, "需要插入三张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        Diet adiet=new Diet(datetime_all,food_all,dietname_all,picture_uri_1,picture_uri_2,picture_uri_3);
        Log.d("lzn", "onAllLoad: "+adiet);

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        DietService service = retrofit.create(DietService.class);

        File file1 =new File(adiet.getPicture1());
        File file2 =new File(adiet.getPicture2());
        File file3 =new File(adiet.getPicture3());
        RequestBody requestFile1=RequestBody.create(MediaType.parse("multipart/form-data"),file1);
        RequestBody requestFile2=RequestBody.create(MediaType.parse("multipart/form-data"),file2);
        RequestBody requestFile3=RequestBody.create(MediaType.parse("multipart/form-data"),file3);
        MultipartBody.Part body1 =
                MultipartBody.Part.createFormData("file",file1.getName(),requestFile1);
        MultipartBody.Part body2 =
                MultipartBody.Part.createFormData("file",file2.getName(),requestFile2);
        MultipartBody.Part body3 =
                MultipartBody.Part.createFormData("file",file3.getName(),requestFile3);
        List<MultipartBody.Part> parts=new ArrayList<>();
        parts.add(body1);
        parts.add(body2);
        parts.add(body3);

        //调用接口方法返回Call对象
        final Call<Re<Diet>> call = service.saveDietPic(parts);
        //发布异步请求
        call.enqueue(new Callback<Re<Diet>>() {

            @Override
            public void onResponse(Call<Re<Diet>> call, retrofit2.Response<Re<Diet>> response) {
                try {
                    Diet getDiet=response.body().getData();
                    System.out.println(response.body());

                    adiet.setPicture1(getDiet.getPicture1());
                    adiet.setPicture2(getDiet.getPicture2());
                    adiet.setPicture3(getDiet.getPicture3());
                    //调用接口方法返回Call对象
                    final Call<Re> call2 = service.saveDiet(adiet,userid);
                    call2.enqueue(new Callback<Re>() {
                        @Override
                        public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                            System.out.println(response.body());
                            if(response.body().getMessage()!=null)
                                Toast.makeText(SaveDietActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            FunctionActivity.actionStart(SaveDietActivity.this,userid);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Re> call, Throwable t) {

                        }
                    });

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Re<Diet>> call, Throwable t) {

            }


        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}