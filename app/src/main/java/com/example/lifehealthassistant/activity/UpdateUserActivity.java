package com.example.lifehealthassistant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Re;
import com.example.lifehealthassistant.bean.User;
import com.example.lifehealthassistant.config.ServerConfiguration;
import com.example.lifehealthassistant.service.UserInfoService;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateUserActivity extends AppCompatActivity {

    private String userid;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private Uri imageUri;

    private RadioGroup radioGroup;
    private EditText username_update_edittext;
    //private EditText password_update_edittext;
    private ImageView userpic_update_image;

    private String userphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        userid=getIntent().getStringExtra("userid");
        username_update_edittext=(EditText) findViewById(R.id.username_update_edittext);
        //password_update_edittext=(EditText) findViewById(R.id.password_update_edittext);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup2);
        userpic_update_image=(ImageView) findViewById(R.id.userpic_update_image);
    }

    public static void actionStart(Context context, String id){
        Intent intent=new Intent(context, UpdateUserActivity.class);
        intent.putExtra("userid",id);
        context.startActivity(intent);
    }

    public void onCameraForUser(View view){
        //创建File对象，用于存储拍照后的图片
        String reallypath=getExternalCacheDir()+"/output_image"+System.currentTimeMillis()+".jpg";
        File outputImage = new File(reallypath) ;

        userphoto=reallypath;
        try{
            if (outputImage.exists()) {
                outputImage.delete( );
            }
            outputImage.createNewFile( );
        }catch ( IOException e) {
            e.printStackTrace( ) ;
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(UpdateUserActivity.this,
                    "com.example.lifehealthassistant.fileprovider", outputImage) ;
        }else {
            imageUri = Uri.fromFile(outputImage) ;
        }
        //启动相机程序
        Intent intent = new Intent ( "android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri) ;
        startActivityForResult(intent,TAKE_PHOTO) ;
    }
    public void onPhotoForUser(View view){
        if(ContextCompat.checkSelfPermission(UpdateUserActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UpdateUserActivity.this,new String[]{
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
            userphoto=imagePath;
            userpic_update_image.setImageBitmap(bitmap);
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
                        userpic_update_image.setImageBitmap(bitmap);

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
    public void onLoadUser(View view){
        String sex="男";
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rd = (RadioButton) radioGroup.getChildAt(i);
            if (rd.isChecked()) {
                sex=rd.getText().toString();
                break;
            }
        }
        String aaa=username_update_edittext.getText().toString();
        //String bbb=password_update_edittext.getText().toString();

        User user=new User();
        user.setId(userid);
        user.setName(aaa);
        user.setSex(sex);
        //user.setPassword(bbb);
        user.setPhoto(userphoto);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfiguration.IP)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //生成接口对象
        UserInfoService service = retrofit.create(UserInfoService.class);

        File file =new File(user.getPhoto());
        RequestBody requestFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file",file.getName(),requestFile);
        //调用接口方法返回Call对象
        final Call<Re<User>> call = service.update(body);
        //发布异步请求
        call.enqueue(new Callback<Re<User>>() {

            @Override
            public void onResponse(Call<Re<User>> call, retrofit2.Response<Re<User>> response) {
                try {
                    User getUser=response.body().getData();
                    System.out.println(response.body());

                    user.setPhoto(getUser.getPhoto());

                    //调用接口方法返回Call对象
                    final Call<Re> call2 = service.update(user,userid);
                    call2.enqueue(new Callback<Re>() {
                        @Override
                        public void onResponse(Call<Re> call, retrofit2.Response<Re> response) {
                            System.out.println(response.body());
                            UserActivity.actionStart(UpdateUserActivity.this,userid);
                            //放在retrofit外。比如282行，跳回useractivity会无法显示修改的内容，大概网络是多线程的
                            //执行跳转功能的线程在上传修改信息的线程前完成，故无法显示最新的
                            if(response.body().getMessage()!=null)
                                Toast.makeText(UpdateUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Re<User>> call, Throwable t) {
            }


        });

    }
}