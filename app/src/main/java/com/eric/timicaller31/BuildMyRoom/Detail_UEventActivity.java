package com.eric.timicaller31.BuildMyRoom;


import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.DailyEvents.Edit_MyAlarmActivity;
import com.eric.timicaller31.DailyEvents.EventHelper;
import com.eric.timicaller31.R;
import com.eric.timicaller31.DailyEvents.VisitRoomActivity;

import java.io.ByteArrayOutputStream;

public class Detail_UEventActivity extends AppCompatActivity {
    String edname,edhint,edphone,edrk,edek,edverid;
    int edhour,edmin,edyear,edmonth,eddate;
    byte[] bimage;
    ImageView imageView;
    private EventHelper eventHelper;
    private Button btn ,btnb;
    private String edrname;
    private String edrbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__my_uevent);


        Intent intent = getIntent();
        edname = intent.getStringExtra("NAME");
        edhint = intent.getStringExtra("HINT");
        edphone = intent.getStringExtra("PHONE");
        edhour = intent.getIntExtra("HOUR",0);
        edmin = intent.getIntExtra("MIN",0);
        edyear = intent.getIntExtra("YEAR",0);
        edmonth = intent.getIntExtra("MONTH",0);
        eddate = intent.getIntExtra("DATE",0);
        edrk = intent.getStringExtra("ROOM_KEY");
        edrname = intent.getStringExtra("ROOM_NAME");
        edrbid = intent.getStringExtra("ROOM_BUILDERID");
        edek = intent.getStringExtra("EVENT_KEY");
        edverid = intent.getStringExtra("VER_ID");
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
        ((TextView)findViewById(R.id.year)).setText(String.valueOf(edyear));
        ((TextView)findViewById(R.id.month)).setText(String.valueOf(edmonth));
        ((TextView)findViewById(R.id.date)).setText(String.valueOf(eddate));
        btn = (Button)findViewById(R.id.button4);
        btnb = (Button)findViewById(R.id.button8);
        if(edverid.equals("BUILDER")){
//            btn.setText("edit");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    image.compress(Bitmap.CompressFormat.PNG, 100, out);
                    byte[] bArray = out.toByteArray();
                    Intent toedit = new Intent(Detail_UEventActivity.this, Edit_MyAlarmActivity.class);
                    toedit.putExtra("TYPE", "myuevent");
                    toedit.putExtra("NAME", edname);
                    toedit.putExtra("HOUR",edhour);
                    toedit.putExtra("MIN", edmin);
                    toedit.putExtra("YEAR", edyear);
                    toedit.putExtra("MONTH", edmonth);
                    toedit.putExtra("DATE", eddate);
                    toedit.putExtra("HINT", edhint);
                    toedit.putExtra("PHONE", edphone);
                    toedit.putExtra("PHONE", edphone);
                    toedit.putExtra("PHONE", edphone);
                    toedit.putExtra("ROOM_KEY",edrk);
                    toedit.putExtra("ROOM_NAME",edrname);
                    toedit.putExtra("ROOM_BUILDERID",edrbid);
                    toedit.putExtra("EVENT_KEY",edek);
                    toedit.putExtra("IMAGE", bArray);

                    startActivity(toedit);
                }
            });
            btnb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent b = new Intent(Detail_UEventActivity.this, URoomActivity.class);
                    b.putExtra("ROOM_KEY", edrk);
                    b.putExtra("ROOM_NAME", edrname);
                    b.putExtra("ROOM_BUILDERID", edrbid);
                    startActivity(b);
                }
            });
        }
        else {
            btn.setText("加到個人管理");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    image.compress(Bitmap.CompressFormat.PNG, 100, out);
                    byte[] bArray = out.toByteArray();
                    EventHelper helper = new EventHelper(getApplication());
                    ContentValues values = new ContentValues();
                    values.put("COL_NAME", edname);
                    values.put("COL_ACTIVE", true);
                    values.put("COL_HOUR", edhour);
                    values.put("COL_MIN", edmin);
                    values.put("COL_YEAR", edyear);
                    values.put("COL_MONTH", edmonth);
                    values.put("COL_DATE", eddate);
                    values.put("COL_HINT", edhint);
                    values.put("COL_PHONE", edphone);
                    values.put("COL_IMAGE",bArray);
                    helper.getWritableDatabase().insert("EVENT", null, values);

                }
            });
            btnb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent b = new Intent(Detail_UEventActivity.this, VisitRoomActivity.class);
                    b.putExtra("ROOM_KEY", edrk);
                    b.putExtra("ROOM_NAME", edrname);
                    b.putExtra("ROOM_BUILDERID", edrbid);
                    startActivity(b);
                }
            });
        }

    }



}

