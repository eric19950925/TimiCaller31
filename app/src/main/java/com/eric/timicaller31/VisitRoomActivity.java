package com.eric.timicaller31;

import android.content.ContentValues;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.timicaller31.BuildMyRoom.Detail_UEventActivity;
import com.eric.timicaller31.DailyEvents.EventHelper;
import com.eric.timicaller31.ObjectClass.Event01;
import com.eric.timicaller31.ObjectClass.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

public class VisitRoomActivity extends AppCompatActivity implements ValueEventListener {
    private static final String TAG = VisitRoomActivity.class.getSimpleName();
    String edrk,edname;
    String name;
    Room room01;
    private RecyclerView recyclerView;
    private String userid;
//    private UEventAdapter adapter;

    private Bitmap bitmap;
    private FirebaseRecyclerAdapter<Event01, UEventHolder> adapter;
    private String rn;
    private String rk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vroom);
        Intent intent = getIntent();
        edrk = intent.getStringExtra("ROOM_KEY");
        edname = intent.getStringExtra("ROOM_NAME");
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText(edname));
//        userid = getSharedPreferences("Timi", MODE_PRIVATE)
//                .getString("USERID", null);
//        Log.d(TAG, "onCreate: " + userid);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VisitRoomActivity.this));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("rooms").child(edrk)
                .child("Events").orderByKey();
        FirebaseRecyclerOptions<Event01> options= new FirebaseRecyclerOptions.Builder<Event01>()
                .setQuery(query,Event01.class).build();
        adapter = new FirebaseRecyclerAdapter<Event01, UEventHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UEventHolder holder, int position, @NonNull final Event01 model) {
                holder.name.setText(model.getName());
                byte[] imagearray;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent todetails = new Intent(VisitRoomActivity.this,Detail_UEventActivity.class);
                        todetails.putExtra("NAME", model.getName());
                        todetails.putExtra("HOUR",model.getHour());
                        todetails.putExtra("MIN", model.getMin());
                        todetails.putExtra("HINT", "");
                        todetails.putExtra("PHONE", "");
                        todetails.putExtra("ROOM_KEY", edrk);
                        todetails.putExtra("VER_ID", "visiter");
                        todetails.putExtra("ROOM_NAME", edname);

//                        todetails.putExtra("IMAGE", imagearray);
                        startActivity(todetails);
                    }
                });
            }

            @NonNull
            @Override
            public UEventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.item_visit,viewGroup,false);
                return new UEventHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        room01 = dataSnapshot.getValue(Room.class);
        rn = room01.getTitle();
        rk = room01.getKey();
//        String rb = room01.getBuilder();
        EventHelper helper = new EventHelper(this);
        ContentValues values = new ContentValues();
        values.put("COL_NAME", rn);
        values.put("COL_KEY", rk);
//        values.put("COL_BUILDER_NAME", rb);

        helper.getWritableDatabase().insert("FAVORITE_ROOM", null, values);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public class UEventHolder extends RecyclerView.ViewHolder {
        TextView name;


        public UEventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);


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

        Intent b = new Intent(VisitRoomActivity.this, DailyEventsActivity.class);
        startActivity(b);
    }
    public void ff(View view){
        //將房間存入手機資料庫:名稱、代碼
        FirebaseDatabase.getInstance().getReference("rooms").child(edrk)
                .addValueEventListener(this);
//        EventHelper helper = new EventHelper(this);
//        ContentValues values = new ContentValues();
//        values.put("COL_NAME", rn);
//        values.put("COL_KEY", rk);
////        values.put("COL_BUILDER_NAME", rb);
//
//        helper.getWritableDatabase().insert("FAVORITE_ROOM", null, values);
    }

}
