package com.eric.timicaller31.BuildMyRoom;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.BuildMyRoomActivity;
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
    String edrk;
    String name;

    private RecyclerView recyclerView;
    private String userid;
//    private UEventAdapter adapter;

    private Bitmap bitmap;
    private FirebaseRecyclerAdapter<Event01, UEventHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uroom);
        Intent intent = getIntent();
        edrk = intent.getStringExtra("ROOM_KEY");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        qrcode();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show_dialog();
//            }

//            private void show_dialog() {
//                final List<String> inputway = new ArrayList<String>();
//                inputway.add("build alarm");
//                inputway.add("build cookbook");
//                inputway.add("equipment use");
//                inputway.add("design");
//                inputway.add("Google sheet");
//                final android.app.AlertDialog.Builder dialog_list = new android.app.AlertDialog.Builder(URoomActivity.this);
//                dialog_list.setTitle("Build");
//                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
//                        URoomActivity.this,android.R.layout.simple_list_item_1,inputway){
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent){
//                        // Cast list view each item as text view
//                        TextView text_view = (TextView) super.getView(position,convertView,parent);
//
//                        // Set item text font
//                        //text_view.setTypeface(mTypeface);
//
//                        // Set text size
//                        text_view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
//
//                        // Get the list view odd and even position items
//                        if(position == 0){ //  items1
//                            // Set the list view one by one items (row) background color
//                            text_view.setBackgroundColor(Color.parseColor("#ffff0000"));
//
//                            // Set the list view one by one items text color
//                            text_view.setTextColor(Color.parseColor("#FFFFFFFF"));
//                        }else if(position==1){ //  items2
//                            // Set the list view alternate items (row) background color
//                            text_view.setBackgroundColor(Color.parseColor("#F8F8FF00"));
//
//                            // Set the list view alternate items text color
//                            text_view.setTextColor(Color.parseColor("#FF7F50"));
//                        }
//                        else if(position==2){ // items3
//                            // Set the list view alternate items (row) background color
//                            text_view.setBackgroundColor(Color.parseColor("#ff00ff00"));
//
//                            // Set the list view alternate items text color
//                            text_view.setTextColor(Color.parseColor("#FFFFFFFF"));
//                        }
//                        else if(position==3){ // items4
//                            // Set the list view alternate items (row) background color
//                            text_view.setBackgroundColor(Color.parseColor("#FF1B9BFD"));
//
//                            // Set the list view alternate items text color
//                            text_view.setTextColor(Color.parseColor("#FFFFFFFF"));
//                        }
//                        else { // items5
//                            // Set the list view alternate items (row) background color
//                            text_view.setBackgroundColor(Color.parseColor("#9370DB"));
//
//                            // Set the list view alternate items text color
//                            text_view.setTextColor(Color.parseColor("#FFFFFFFF"));
//                        }
//
//
//                        // Finally, return the modified items
//                        return text_view;
//                    }
//                };
//
//                // Set a single choice items list for alert dialog
//                dialog_list.setSingleChoiceItems(
//                        arrayAdapter, // Items list
//                        -1, // Index of checked item (-1 = no selection)
//                        new DialogInterface.OnClickListener() // Item click listener
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int which) {
//                                // Get the alert dialog selected item's text
//                                String selectedItem = inputway.get(which);
//
//                                // Display the selected item's text on toast
////                                Toast.makeText(getContext(),"Checked : " + selectedItem,Toast.LENGTH_LONG).show();
//                                Toast.makeText(URoomActivity.this, "你選的是" + selectedItem, Toast.LENGTH_SHORT).show();
//                                switch(which) {
//                                    case 0:

                                        Intent new1 = new Intent(URoomActivity.this, NewAlarmActivity.class);
                                        new1.putExtra("ROOM_KEY",edrk);
                                        startActivity(new1);
//                                        break;
//                                    case 1:
//                                        Intent new2 = new Intent(URoomActivity.this, NewTimerActivity.class);
//                                        startActivity(new2);
//                                        break;
//                                    case 2:
//                                        Intent new3 = new Intent(URoomActivity.this, NewRCActivity.class);
//                                        startActivity(new3);
//                                        break;
//                                    case 3:
//                                        Intent new4 = new Intent(URoomActivity.this, NewDZActivity.class);
//                                        startActivity(new4);
//                                        break;
//                                    case 4:
//                                        Intent new5 = new Intent(URoomActivity.this, NewGXActivity.class);
//                                        startActivity(new5);
//                                        break;
//                                }
//                            }
//                        });
//
//                // Set the a;ert dialog positive button
//                dialog_list.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // Just dismiss the alert dialog after selection
//                        // Or do something now
//                    }
//                });
//
//                // Create the alert dialog
//                android.app.AlertDialog dialog = dialog_list.create();
//
//                // Finally, display the alert dialog
//                dialog.show();
            }
        });
//        userid = getSharedPreferences("Timi", MODE_PRIVATE)
//                .getString("USERID", null);
//        Log.d(TAG, "onCreate: " + userid);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(URoomActivity.this));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("rooms").child(edrk)
                .child("Events").orderByKey();
        FirebaseRecyclerOptions<Event01> options= new FirebaseRecyclerOptions.Builder<Event01>()
                .setQuery(query,Event01.class).build();
        adapter = new FirebaseRecyclerAdapter<Event01, UEventHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UEventHolder holder, int position, @NonNull final Event01 model) {
                holder.name.setText(model.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intod = new Intent(URoomActivity.this,Detail_UEventActivity.class);
                        intod.putExtra("ROOM_KEY",edrk);
                        intod.putExtra("EVENT_KEY",model.getKey());

                        intod.putExtra("HINT",model.getHint());
                        intod.putExtra("PHONE",model.getPhone());
                        intod.putExtra("HOUR",model.getHour());
                        intod.putExtra("MIN",model.getMin());
                        intod.putExtra("YEAR",model.getYear());
                        intod.putExtra("MNOTH",model.getMonth());
                        intod.putExtra("DATE",model.getDate());
                        intod.putExtra("NAME",model.getName());

                        intod.putExtra("VER_ID","BUILDER");
                        startActivity(intod);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                                DatabaseReference roomRef1 = FirebaseDatabase.getInstance().getReference("rooms");

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
        TextView name;
        ImageView delete;

        public UEventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
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
        String QRCodeContent = edrk;
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
//        toURoom.putExtra("ROOM_KEY",edrk);
        startActivity(toBRoom);
    }
}
