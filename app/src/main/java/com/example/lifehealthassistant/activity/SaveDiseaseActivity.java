package com.example.lifehealthassistant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.DietService;
import com.example.lifehealthassistant.service.DiseaseService;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SaveDiseaseActivity extends AppCompatActivity {

    private int userid;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private Uri imageUri;
    private int picflag=0;
    private int picSym=0;
    private int picMed=0;

    //视图中控件
    private TextView datestart_text;
    private TextView dateend_text;
    private EditText diseasename_edittext;
    private EditText symptom_edittext;
    private ImageView sym_pic;
    private EditText medicine_edittext;
    private ImageView med_pic;


    //封装成Disease
    private String datestart_this;
    private String dateend_this;
    private String diseasename_this;
    private String symptom_this;
    private String sympic_this;
    private String medicine_this;
    private String medpic_this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_disease);
        userid=getIntent().getIntExtra("userid",1);

        datestart_text=(TextView) findViewById(R.id.datestart_text);
        dateend_text=(TextView) findViewById(R.id.dateend_text);
        diseasename_edittext=(EditText) findViewById(R.id.diseasename_edittext);
        symptom_edittext=(EditText) findViewById(R.id.symptom_edittext);
        sym_pic=(ImageView) findViewById(R.id.sym_pic);
        medicine_edittext=(EditText) findViewById(R.id.medicine_edittext);
        med_pic=(ImageView) findViewById(R.id.med_pic);

    }

    public static void actionStart(Context context, int id){
        Intent intent=new Intent(context,SaveDiseaseActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onSelectDateStart(View v){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(SaveDiseaseActivity.this, null,
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
            datestart_text.setText(formatDate(year, monthOfYear, dayOfMonth));
            datestart_this=formatDate(year, monthOfYear, dayOfMonth);
            // 关闭dialog
            dialog.dismiss();
        });
    }
    public void onSelectDateEnd(View v){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(SaveDiseaseActivity.this, null,
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
            dateend_text.setText(formatDate(year, monthOfYear, dayOfMonth));
            dateend_this=formatDate(year, monthOfYear, dayOfMonth);
            // 关闭dialog
            dialog.dismiss();
        });
    }
    private String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return year + "-" + String.format(Locale.getDefault(), "%02d-%02d", monthOfYear, dayOfMonth);
    }

    public void onCameraForSym(View v){
        picSym+=1;
        if(picSym>1)
        {
            Toast.makeText(SaveDiseaseActivity.this, "症状照片数量限制一张，可再次点击按钮重新选择！", Toast.LENGTH_SHORT).show();
            picSym=0;
            return;
        }
        picflag=1;
        //创建File对象，用于存储拍照后的图片
        //Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM+File.separator+"Camera"+File.separator
        String reallypath=getExternalCacheDir()+"/output_image"+System.currentTimeMillis()+".jpg";
        File outputImage = new File(reallypath) ;

        sympic_this=reallypath;
        try{
            if (outputImage.exists()) {
                outputImage.delete( );
            }
            outputImage.createNewFile( );
        }catch ( IOException e) {
            e.printStackTrace( ) ;
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(SaveDiseaseActivity.this,
                    "com.example.lifehealthassistant.fileprovider", outputImage) ;
        }else {
            imageUri = Uri.fromFile(outputImage) ;
        }
        //启动相机程序
        Intent intent = new Intent ( "android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri) ;
        startActivityForResult(intent,TAKE_PHOTO) ;
    }

    public void onCameraForMed(View v){
        picMed+=1;
        if(picMed>1)
        {
            Toast.makeText(SaveDiseaseActivity.this, "药物照片数量限制一张，可再次点击按钮重新选择！", Toast.LENGTH_SHORT).show();
            picMed=0;
            return;
        }
        picflag=2;
        //创建File对象，用于存储拍照后的图片
        String reallypath=getExternalCacheDir()+"/output_image"+System.currentTimeMillis()+".jpg";
        File outputImage = new File(reallypath) ;

        medpic_this=reallypath;
        try{
            if (outputImage.exists()) {
                outputImage.delete( );
            }
            outputImage.createNewFile( );
        }catch ( IOException e) {
            e.printStackTrace( ) ;
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(SaveDiseaseActivity.this,
                    "com.example.lifehealthassistant.fileprovider", outputImage) ;
        }else {
            imageUri = Uri.fromFile(outputImage) ;
        }
        //启动相机程序
        Intent intent = new Intent ( "android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri) ;
        startActivityForResult(intent,TAKE_PHOTO) ;
    }
    public void onPhotoForSym(View v){
        picSym+=1;
        if(picSym>1)
        {
            Toast.makeText(SaveDiseaseActivity.this, "症状照片数量限制一张，可再次点击按钮重新选择！", Toast.LENGTH_SHORT).show();
            picSym=0;
            return;
        }
        picflag=1;
        if(ContextCompat.checkSelfPermission(SaveDiseaseActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SaveDiseaseActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else
            openAlbum();
    }
    public void onPhotoForMed(View v){
        picMed+=1;
        if(picMed>1)
        {
            Toast.makeText(SaveDiseaseActivity.this, "药物照片数量限制一张，可再次点击按钮重新选择！", Toast.LENGTH_SHORT).show();
            picMed=0;
            return;
        }
        picflag=2;
        if(ContextCompat.checkSelfPermission(SaveDiseaseActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SaveDiseaseActivity.this,new String[]{
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
            if(picflag==1){
                sympic_this=imagePath;
                sym_pic.setImageBitmap(bitmap);
            }
            else{
                medpic_this=imagePath;
                med_pic.setImageBitmap(bitmap);
            }

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
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
                        if(picflag==1)
                            sym_pic.setImageBitmap(bitmap);
                        else
                            med_pic.setImageBitmap(bitmap);

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
    public void onLoadDisease(View v){
        diseasename_this=diseasename_edittext.getText().toString();
        symptom_this=symptom_edittext.getText().toString();
        medicine_this=medicine_edittext.getText().toString();
        datestart_this+=" 00:00:00";
        dateend_this+=" 00:00:00";
        Disease adisease=new Disease(datestart_this,dateend_this,diseasename_this,symptom_this,sympic_this,medicine_this,medpic_this);
        Log.d("lzn", "onLoadDisease: "+adisease);

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        DiseaseService service = retrofit.create(DiseaseService.class);

        File file1 =new File(adisease.getSympic());
        File file2 =new File(adisease.getMedpic());
        RequestBody requestFile1=RequestBody.create(MediaType.parse("multipart/form-data"),file1);
        RequestBody requestFile2=RequestBody.create(MediaType.parse("multipart/form-data"),file2);
        MultipartBody.Part body1 =
                MultipartBody.Part.createFormData("file",file1.getName(),requestFile1);
        MultipartBody.Part body2 =
                MultipartBody.Part.createFormData("file",file2.getName(),requestFile2);
        List<MultipartBody.Part> parts=new ArrayList<>();
        parts.add(body1);
        parts.add(body2);

        //调用接口方法返回Call对象
        final Call<Re<Disease>> call = service.saveDiseasePic(parts);
        //发布异步请求
        call.enqueue(new Callback<Re<Disease>>() {

            @Override
            public void onResponse(Call<Re<Disease>> call, retrofit2.Response<Re<Disease>> response) {
                try {
                    Disease getDisease=response.body().getData();
                    System.out.println(response.body());

                    adisease.setSympic(getDisease.getSympic());
                    adisease.setMedpic(getDisease.getMedpic());

                    //调用接口方法返回Call对象
                    final Call<Re<String>> call2 = service.saveDisease(adisease,userid);
                    call2.enqueue(new Callback<Re<String>>() {
                        @Override
                        public void onResponse(Call<Re<String>> call, retrofit2.Response<Re<String>> response) {
                            System.out.println(response.body());
                        }

                        @Override
                        public void onFailure(Call<Re<String>> call, Throwable t) {

                        }
                    });

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Re<Disease>> call, Throwable t) {

            }


        });
        DiseaseActivity.actionStart(SaveDiseaseActivity.this,userid);
    }



}