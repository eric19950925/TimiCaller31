package com.eric.timicaller31.BuildMyRoom;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.NewAlarmActivity;
import com.eric.timicaller31.ObjectClass.Event01;
import com.eric.timicaller31.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

public class URoomActivity extends AppCompatActivity {
    private static final String TAG = URoomActivity.class.getSimpleName();
    String edrk,edname;
    String name;

    private RecyclerView recyclerView;
    private String userid;
//    private UEventAdapter adapter;

    private Bitmap bitmap;
    private FirebaseRecyclerAdapter<Event01, UEventHolder> adapter;
    private String edrbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uroom);
        Intent intent = getIntent();
        edrk = intent.getStringExtra("ROOM_KEY");
        edname = intent.getStringExtra("ROOM_NAME");
        edrbid = intent.getStringExtra("ROOM_BUILDERID");


        userid = getSharedPreferences("Timi", MODE_PRIVATE)
                .getString("USERID", "");
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText(edname));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        qrcode();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new1 = new Intent(URoomActivity.this, NewAlarmActivity.class);
                new1.putExtra("ROOM_KEY",edrk);
                new1.putExtra("ROOM_NAME",edname);
                new1.putExtra("ROOM_BUILDERID",edrbid);
                startActivity(new1);
            }
        });

//        Log.d(TAG, "onCreate: " + userid);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(URoomActivity.this));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("users").child(userid).child("rooms").child(edrk)
                .child("Events").orderByKey();
        FirebaseRecyclerOptions<Event01> options= new FirebaseRecyclerOptions.Builder<Event01>()
                .setQuery(query,Event01.class).build();
        adapter = new FirebaseRecyclerAdapter<Event01, UEventHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UEventHolder holder, int position, @NonNull final Event01 model) {
                holder.name.setText(model.getName());
                holder.hour.setText(String.valueOf(model.getHour()));
                holder.min.setText(String.valueOf(model.getMin()));
                holder.year.setText(String.valueOf(model.getYear()));
                holder.month.setText(String.valueOf(model.getMonth()));
                holder.date.setText(String.valueOf(model.getDate()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intod = new Intent(URoomActivity.this,Detail_UEventActivity.class);
                        intod.putExtra("ROOM_KEY",edrk);
                        intod.putExtra("ROOM_NAME",edname);
                        intod.putExtra("ROOM_BUILDERID", edrbid);
                        intod.putExtra("EVENT_KEY",model.getKey());
                        intod.putExtra("HINT",model.getHint());
                        intod.putExtra("PHONE",model.getPhone());
                        intod.putExtra("HOUR",model.getHour());
                        intod.putExtra("MIN",model.getMin());
                        intod.putExtra("YEAR",model.getYear());
                        intod.putExtra("MONTH",model.getMonth());
                        intod.putExtra("DATE",model.getDate());
                        intod.putExtra("NAME",model.getName());

                        intod.putExtra("VER_ID","BUILDER");
                        startActivity(intod);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference roomRef1 = FirebaseDatabase.getInstance().getReference("users").child(userid).child("rooms");
                        roomRef1.child(edrk).child("Events").child(model.getKey()).removeValue();
                                            }



                });
            }

            @NonNull
            @Override
            public UEventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.item_uroom,viewGroup,false);
                return new UEventHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    public class UEventHolder extends RecyclerView.ViewHolder {
        TextView name,hour,min,year,month,date;
        ImageView delete;


        public UEventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            hour = itemView.findViewById(R.id.tv_hour);
            min = itemView.findViewById(R.id.tv_min);
            year = itemView.findViewById(R.id.tv_yy);
            month = itemView.findViewById(R.id.tv_mm);
            date = itemView.findViewById(R.id.tv_dd);
            delete = itemView.findViewById(R.id.del);

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    private void qrcode() {
        Log.d(TAG, "onCreate: " + edrk);
        // QR code 的內容
        String QR =edrk+":"+userid;
        String QRCodeContent = QR;
        // QR code 寬度
        int QRCodeWidth = 800;
        // QR code 高度
        int QRCodeHeight = 800;
        // QR code 內容編碼
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        MultiFormatWriter writer = new MultiFormatWriter();

        // 容錯率姑且可以將它想像成解析度，分為 4 級：L(7%)，M(15%)，Q(25%)，H(30%)
        // 設定 QR code 容錯率為 H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // 建立 QR code 的資料矩陣
        BitMatrix result = null;
        try {
            result = writer.encode(QRCodeContent, BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        // ZXing 還可以生成其他形式條碼，如：BarcodeFormat.CODE_39、BarcodeFormat.CODE_93、BarcodeFormat.CODE_128、BarcodeFormat.EAN_8、BarcodeFormat.EAN_13...

        //建立點陣圖
        bitmap = Bitmap.createBitmap(QRCodeWidth, QRCodeHeight, Bitmap.Config.ARGB_8888);
        // 將 QR code 資料矩陣繪製到點陣圖上
        for (int y = 0; y<QRCodeHeight; y++)
        {
            for (int x = 0;x<QRCodeWidth; x++)
            {
                bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        FloatingActionButton qrfab = (FloatingActionButton) findViewById(R.id.qrfab);
        qrfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_qrcode();
            }});
//            ImageView imgView = (ImageView) findViewById(R.id.imageView);
//            // 設定為 QR code 影像
//            imgView.setImageBitmap(bitmap);
    }
    public void make_qrcode(){
        AlertDialog.Builder ImageDialog = new AlertDialog.Builder(URoomActivity.this);
        ImageDialog.setTitle("Title");
        ImageView showImage = new ImageView(URoomActivity.this);

        ImageDialog.setView(showImage);
        showImage.setImageBitmap(bitmap);
        ImageDialog.setNegativeButton("ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
            }
        });
        ImageDialog.show();
    }
    public void back(View view){

        Intent toBRoom = new Intent(this, BuildMyRoomActivity.class);
//        toBRoom.putExtra("ROOM_KEY",edname);
        startActivity(toBRoom);
    }
}
