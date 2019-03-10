package com.eric.timicaller31.DailyEvents;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eric.timicaller31.BuildMyRoom.Detail_UEventActivity;
import com.eric.timicaller31.ObjectClass.Event01;
import com.eric.timicaller31.ObjectClass.Room;
import com.eric.timicaller31.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private String edrbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vroom);
        Intent intent = getIntent();
        edrk = intent.getStringExtra("ROOM_KEY");
        edname = intent.getStringExtra("ROOM_NAME");
        edrbid = intent.getStringExtra("ROOM_BUILDERID");
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText(edname));
        userid = getSharedPreferences("Timi", MODE_PRIVATE)
                .getString("USERID", null);
//        Log.d(TAG, "onCreate: " + userid);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VisitRoomActivity.this));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("users").child(edrbid).child("rooms").child(edrk)
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
                        todetails.putExtra("YEAR", model.getYear());
                        todetails.putExtra("MONTH", model.getMonth());
                        todetails.putExtra("DATE", model.getDate());
                        todetails.putExtra("HINT", model.getHint());
                        todetails.putExtra("PHONE", model.getPhone());
                        todetails.putExtra("ROOM_KEY", edrk);
                        todetails.putExtra("VER_ID", "visiter");
                        todetails.putExtra("ROOM_NAME", edname);
                        todetails.putExtra("ROOM_BUILDERID",edrbid);
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
        values.put("COL_BUILDER_NAME", edrbid);

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
        FirebaseDatabase.getInstance().getReference("users").child(edrbid).child("rooms").child(edrk)
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
