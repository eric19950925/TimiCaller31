package com.eric.timicaller31.UploadRoom;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.eric.timicaller31.Event01;
import com.eric.timicaller31.R;
import com.eric.timicaller31.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class URoomActivity extends AppCompatActivity {
    private static final String TAG = URoomActivity.class.getSimpleName();
    String edrk;
    String name;

//    private RecyclerView recyclerView;
    private String userid;
//    private UEventAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uroom);
        Intent intent = getIntent();
        edrk = intent.getStringExtra("ROOM_KEY");
        Log.d(TAG, "onCreate: " + edrk);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentu = new Intent(URoomActivity.this, AddNewUEvent.class);
//                startActivity(intentu);
//            }
//        });
//        userid = getSharedPreferences("Timi", MODE_PRIVATE)
//                .getString("USERID", null);
//        Log.d(TAG, "onCreate: " + userid);
//        Query query = FirebaseDatabase.getInstance().getReference("users")
//                .child(userid).child("rooms").child(edrk).child("events").orderByKey();
//        FirebaseRecyclerOptions<Event01> options= new FirebaseRecyclerOptions.Builder<Event01>()
//                .setQuery(query,Event01.class).build();
//        FirebaseRecyclerAdapter<Event01,URoomActivity.UEventHolder>adapter = new FirebaseRecyclerAdapter<Event01, URoomActivity.UEventHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull URoomActivity.UEventHolder holder, int position, @NonNull final Event01 model) {
//                holder.title.setText(model.getTitle());
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intod = new Intent(URoomActivity.this,Detail_UEventActivity.class);
//
//                        startActivity(intod);
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public URoomActivity.UEventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = getLayoutInflater().inflate(R.layout.item_alarm,viewGroup,false);
//                return new URoomActivity.UEventHolder(view);
//            }
//        };
//        recyclerView.setAdapter(adapter);
//
    }
//    public class UEventHolder extends RecyclerView.ViewHolder {
//        TextView name;
//
//
//        public UEventHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.tv_name);
//        }
//    }
}
