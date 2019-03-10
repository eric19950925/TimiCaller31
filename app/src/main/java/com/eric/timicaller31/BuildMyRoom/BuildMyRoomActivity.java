package com.eric.timicaller31.BuildMyRoom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.BoNaViewHelper;
import com.eric.timicaller31.ObjectClass.Room;
import com.eric.timicaller31.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BuildMyRoomActivity extends AppCompatActivity {
    private static final String TAG = BuildMyRoomActivity.class.getSimpleName();
    private Context mContext = BuildMyRoomActivity.this;
    private static final int ACTIVITY_NUM = 2;
    private RecyclerView recyclerView;
    private String userid;
    private FirebaseUser user;
    private FirebaseRecyclerAdapter<Room, URoomHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_my_room);
        setBoNaView();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("公佈欄管理"));
        userid = this.getSharedPreferences("Timi", Context.MODE_PRIVATE)
                .getString("USERID", "");
        Log.d(TAG, "onCreateView: "+userid);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ed_title = new EditText(BuildMyRoomActivity.this);
                new AlertDialog.Builder(BuildMyRoomActivity.this).setTitle("建立公佈欄:")
                        .setView(ed_title)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String roomTitle = ed_title.getText().toString();
                                DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("users").child(userid).child("rooms").push();
                                Room room = new Room();
                                room.setTitle(roomTitle);
                                room.setId(userid);
                                roomRef.setValue(room);
                                String key = roomRef.getKey();
                                room.setKey(key);
                                roomRef.child("key").setValue(key);


                            }
                        }).setNeutralButton("取消", null).show();
            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BuildMyRoomActivity.this));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("users").child(userid).child("rooms").orderByKey();
        FirebaseRecyclerOptions<Room> options = new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(query, Room.class).build();
        adapter = new FirebaseRecyclerAdapter<Room, URoomHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull URoomHolder holder, int position, @NonNull final Room model) {
//                String title = model.getTitle();
//                Log.d(TAG, "onBindViewHolder: "+title);
                final String room_key = model.getKey();
                holder.title.setText(model.getTitle());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent into_uroom = new Intent(getApplicationContext(),URoomActivity.class);
                        into_uroom.putExtra("ROOM_KEY",room_key);
                        into_uroom.putExtra("ROOM_NAME",model.getTitle());
                        into_uroom.putExtra("ROOM_BUILDERID",model.getTitle());
                        startActivity(into_uroom);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(BuildMyRoomActivity.this);
                        alertDialogBuilder.setTitle("確認刪除此公佈欄?");
                        alertDialogBuilder
                                .setMessage("按下確認以刪除!")
                                .setCancelable(false)
                                .setPositiveButton("確認",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                DatabaseReference roomRef1 = FirebaseDatabase.getInstance().getReference("rooms");

                                                roomRef1.child(model.getKey()).removeValue();
                                            }
                                        })

                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                });

                        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
            }

            @NonNull
            @Override
            public URoomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.item_buroom,viewGroup,false);
                return new URoomHolder(view);

            }
        };
        recyclerView.setAdapter(adapter);


    }
    public class URoomHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView delete;
        public URoomHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_name);
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
    private void setBoNaView(){

        BottomNavigationViewEx b = (BottomNavigationViewEx)findViewById(R.id.bar);
        BoNaViewHelper.setBoNaView(b);
        BoNaViewHelper.enableNavigation(mContext,b);
        Menu menu = b.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
