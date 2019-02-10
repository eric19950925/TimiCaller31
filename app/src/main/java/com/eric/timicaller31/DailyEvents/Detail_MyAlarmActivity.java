package com.eric.timicaller31.DailyEvents;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.R;

import java.io.ByteArrayOutputStream;

public class Detail_MyAlarmActivity extends AppCompatActivity {
    String edname,edhint,edphone;
    int edhour,edmin;
    byte[] bimage;
    ImageView imageView;
    private EventHelper eventHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__my_alarm);


        Intent intent = getIntent();
        edname = intent.getStringExtra("NAME");
        edhint = intent.getStringExtra("HINT");
        edphone = intent.getStringExtra("PHONE");
        edhour = intent.getIntExtra("HOUR",0);
        edmin = intent.getIntExtra("MIN",0);
        if (intent.hasExtra("IMAGE")) {
            bimage = intent.getByteArrayExtra("IMAGE");
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
        }
        else {
            Resources res=getResources();

            Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.jungle);
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

        }

        ((TextView) findViewById(R.id.name)).setText(edname);
        ((TextView) findViewById(R.id.hint)).setText(edhint);
        ((TextView) findViewById(R.id.phone)).setText(edphone);
        ((TextView)findViewById(R.id.hour)).setText(String.valueOf(edhour));
        ((TextView)findViewById(R.id.min)).setText(String.valueOf(edmin));
    }


    public void Edit(View view){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        image.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] bArray = out.toByteArray();
        Intent toedit = new Intent(Detail_MyAlarmActivity.this, Edit_MyAlarmActivity.class);

        toedit.putExtra("NAME", edname);
        toedit.putExtra("HOUR",edhour);
        toedit.putExtra("MIN", edmin);
        toedit.putExtra("HINT", edhint);
        toedit.putExtra("PHONE", edphone);
        toedit.putExtra("IMAGE", bArray);

        startActivity(toedit);
    }
}

