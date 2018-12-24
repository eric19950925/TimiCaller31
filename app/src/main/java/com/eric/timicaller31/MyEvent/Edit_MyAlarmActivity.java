package com.eric.timicaller31.MyEvent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.timicaller31.MainActivity;
import com.eric.timicaller31.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Edit_MyAlarmActivity extends AppCompatActivity {
    private static final int REQUEST_GALLARY = 6;
    private static final int REQUEST_CAMERA = 7;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    String name,hint,phone;
    int hour,min,id;
    private TextView etname;
    private EditText ethour;
    private EditText etmin;
    private EditText ethint;
    private EditText etphone;
    private ImageView imageView;
    private Button btn;
    byte[] bimage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__my_alarm);
        Intent intent = getIntent();
//
        name = intent.getStringExtra("NAME");
        hint = intent.getStringExtra("HINT");
        phone = intent.getStringExtra("PHONE");
        hour = intent.getIntExtra("HOUR",0);
        min = intent.getIntExtra("MIN",0);
        bimage = intent.getByteArrayExtra("IMAGE");

        ((TextView) findViewById(R.id.tv_name)).setText(name);
        ((EditText) findViewById(R.id.et_hour)).setText(String.valueOf(hour));
        ((EditText) findViewById(R.id.et_min)).setText(String.valueOf(min));
        ((EditText) findViewById(R.id.et_hint)).setText(hint);
        ((EditText) findViewById(R.id.et_phone)).setText(phone);
        etname = findViewById(R.id.tv_name);
        ethour = findViewById(R.id.et_hour);
        etmin = findViewById(R.id.et_min);
        ethint = findViewById(R.id.et_hint);
        etphone = findViewById(R.id.et_phone);
        Bitmap bmp = BitmapFactory.decodeByteArray(bimage, 0, bimage.length);
        imageView = (ImageView) findViewById(R.id.iv);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        // 设置想要的大小
        int newWidth = 320;
        int newHeight = 480;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        imageView.setImageBitmap(newbm);


        imageView = (ImageView) findViewById(R.id.iv);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        int premission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (premission == PackageManager.PERMISSION_GRANTED) {
            picCamera();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
    }

    private void picCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallary() {
        int premission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (premission == PackageManager.PERMISSION_GRANTED) {

            picGallery();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLARY);
        }
    }

    private void picGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_GALLARY){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                picGallery();
            }
        }
        if(requestCode==REQUEST_CAMERA){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                picCamera();
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    // 设置想要的大小
                    int newWidth = 320;
                    int newHeight = 480;
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    imageView.setImageBitmap(newbm);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Edit_MyAlarmActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            int width = thumbnail.getWidth();
            int height = thumbnail.getHeight();
            // 设置想要的大小
            int newWidth = 320;
            int newHeight = 480;
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            Bitmap newbm = Bitmap.createBitmap(thumbnail, 0, 0, width, height, matrix, true);
            imageView.setImageBitmap(newbm);

        }
    }

    public void update(View view) {

        String name = etname.getText().toString();
        int hour = Integer.parseInt(ethour.getText().toString());
        int min = Integer.parseInt(etmin.getText().toString());
        String hint = ethint.getText().toString();
        String phone = etphone.getText().toString();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        image.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] bArray = out.toByteArray();

        EventHelper helper = new EventHelper(this);
        ContentValues values = new ContentValues();
        values.put("COL_NAME", name);
        values.put("COL_ACTIVE", true);
        values.put("COL_HOUR", hour);
        values.put("COL_MIN", min);
        values.put("COL_HINT", hint);
        values.put("COL_PHONE", phone);
        values.put("COL_IMAGE",bArray);
        long check = helper.getWritableDatabase().update(
                "EVENT", values, "COL_NAME =?",new String[]{name});

        if (check > -1) {
            Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Update Failure", Toast.LENGTH_LONG).show();


        Intent tomain = new Intent(Edit_MyAlarmActivity.this, MainActivity.class);
        startActivity(tomain);

    }
}