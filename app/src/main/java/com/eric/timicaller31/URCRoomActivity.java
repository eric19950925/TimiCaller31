package com.eric.timicaller31;

import android.annotation.SuppressLint;
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
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.timicaller31.BuildMyRoom.Detail_UEventActivity;
import com.eric.timicaller31.BuildMyRoom.URoomActivity;
import com.eric.timicaller31.ObjectClass.Event01;
import com.eric.timicaller31.ObjectClass.Receipt;
import com.eric.timicaller31.ObjectClass.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class URCRoomActivity extends AppCompatActivity implements ValueEventListener {
    private static final String TAG = URCRoomActivity.class.getSimpleName();
    String edrk,edrn,edname;
    String name;

    private RecyclerView recyclerView;
    private String userid;
//    private UEventAdapter adapter;

    private Bitmap bitmap;
    private FirebaseRecyclerAdapter<Receipt, UEventHolder> adapter;
    private String rcroomTitle1;
    private Receipt rcroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urcroom);
        Intent intent = getIntent();
        edrk = intent.getStringExtra("ROOM_KEY");
        edrn = intent.getStringExtra("ROOM_NUM");
        edname = intent.getStringExtra("ROOM_NAME");
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText(edname));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ed_title = new EditText(URCRoomActivity.this);
                new android.app.AlertDialog.Builder(URCRoomActivity.this).setTitle("Add Receipt")
                        .setView(ed_title)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String rcroomTitle = ed_title.getText().toString();
                                DatabaseReference rcRef = FirebaseDatabase.getInstance().getReference("rcrooms").child(edrk)
                                        .child("Receipts").push();

                                Receipt receipt = new Receipt();
                                receipt.setNum(rcroomTitle);
                                receipt.setStar(false);
                                rcRef.setValue(receipt);
                                String key = rcRef.getKey();
                                receipt.setKey(key);
                                rcRef.child("key").setValue(key);

                                Intent toURoom = new Intent(URCRoomActivity.this, URCRoomActivity.class);
                                toURoom.putExtra("ROOM_KEY",edrk);
                                toURoom.putExtra("ROOM_NUM",edrn);
                                startActivity(toURoom);


                            }
                        }).setNeutralButton("Cancel", null).show();
            }
        });
        FloatingActionButton qrfab = (FloatingActionButton) findViewById(R.id.qrfab);
        qrfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gq = new Intent(URCRoomActivity.this,QRS_RCActivity.class);
                gq.putExtra("ROOM_KEY",edrk);
                gq.putExtra("ROOM_NUM",edrn);
                startActivity(gq);

            }});
//        userid = getSharedPreferences("Timi", MODE_PRIVATE)
//                .getString("USERID", null);
//        Log.d(TAG, "onCreate: " + userid);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(URCRoomActivity.this));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("rcrooms").child(edrk)
                .child("Receipts").orderByKey();
        FirebaseRecyclerOptions<Receipt> options= new FirebaseRecyclerOptions.Builder<Receipt>()
                .setQuery(query,Receipt.class).build();
        adapter = new FirebaseRecyclerAdapter<Receipt, UEventHolder>(options) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull UEventHolder holder, int position, @NonNull final Receipt model) {
                holder.name.setText(model.getNum());
                if(model.getNum().equals(edrn)){
                    holder.star.setColorFilter(R.color.yellow);
                    Log.d(TAG, "onBindViewHolder: "+edrn);
                }
                else {
                    holder.star.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onBindViewHolder: !="+edrn);
                }
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                                DatabaseReference roomRef1 = FirebaseDatabase.getInstance().getReference("rcrooms");

                                                roomRef1.child(edrk).child("Receipts").child(model.getKey()).removeValue();
                                            }



                });
            }

            @NonNull
            @Override
            public UEventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.item_urcroom,viewGroup,false);
                return new UEventHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public class UEventHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView delete;
        ImageView star;
        public UEventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            delete = itemView.findViewById(R.id.del);
            star = itemView.findViewById(R.id.star);
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

    public void back(View view){

        Intent toBRoom = new Intent(this, ReceiptActivity.class);
//        toURoom.putExtra("ROOM_KEY",edrk);
        startActivity(toBRoom);
    }
    public void check(View view){
        final EditText ed_title = new EditText(URCRoomActivity.this);
        new android.app.AlertDialog.Builder(URCRoomActivity.this).setTitle("Add Check Number ")
                .setView(ed_title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        rcroomTitle1 = ed_title.getText().toString();

                        FirebaseDatabase.getInstance().getReference("rcrooms")
                                .child(edrk).child("num").setValue(rcroomTitle1);
                        Log.d(TAG, "onDataChange: " + rcroomTitle1);

                        Intent toURoom = new Intent(URCRoomActivity.this, URCRoomActivity.class);
                        toURoom.putExtra("ROOM_KEY",edrk);
                        toURoom.putExtra("ROOM_NUM",rcroomTitle1);
                        startActivity(toURoom);


                    }
                }).setNeutralButton("Cancel", null).show();

    }



}
