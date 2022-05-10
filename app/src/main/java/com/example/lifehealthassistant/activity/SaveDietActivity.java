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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class SaveDietActivity extends AppCompatActivity {

    private Spinner spin_dietname;
    private TextView datetext;
    private TextView timetext;
    private TextView dietnametext;

    private int photoCount=0;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private ImageView picture1;
    private ImageView picture2;
    private ImageView picture3;
    private Uri imageUri;


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

    public void onCamera(View view) throws IOException {
        photoCount+=1;
        if(photoCount>3)
        {
            Toast.makeText(SaveDietActivity.this, "照片数量需要小于等于3！", Toast.LENGTH_SHORT).show();
            return;
        }
        //创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir( ) ,
                "output_image"+System.currentTimeMillis()+". jpg" ) ;
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
                        if(photoCount==1)
                            picture1.setImageBitmap(bitmap);
                        else if(photoCount==2)
                            picture2.setImageBitmap(bitmap);
                        else
                            picture3.setImageBitmap(bitmap);

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
            if(photoCount==1)
                picture1.setImageBitmap(bitmap);
            else if(photoCount==2)
                picture2.setImageBitmap(bitmap);
            else
                picture3.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}