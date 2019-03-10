package com.eric.timicaller31.Receipt;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ReceiptActivity extends AppCompatActivity {
    private Context mContext = ReceiptActivity.this;
    private static final int ACTIVITY_NUM = 3;
    String userid;
    RecyclerView recyclerView2;
    private FirebaseRecyclerAdapter<Room, URoomHolder> adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        setBoNaView();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("發票存摺"));
        userid = this.getSharedPreferences("Timi", Context.MODE_PRIVATE)
                .getString("USERID", "");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ed_title = new EditText(ReceiptActivity.this);
                new AlertDialog.Builder(ReceiptActivity.this).setTitle("建立發票存摺:")
                        .setView(ed_title)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String rcroomTitle = ed_title.getText().toString();
                                DatabaseReference rcroomRef = FirebaseDatabase.getInstance().getReference("users").child(userid).child("rcrooms").push();
                                Room room = new Room();
                                room.setTitle(rcroomTitle);
                                room.setId(userid);
//                                                        room.setNum("");
                                rcroomRef.setValue(room);
                                String key = rcroomRef.getKey();
                                room.setKey(key);
                                rcroomRef.child("key").setValue(key);


                            }
                        }).setNeutralButton("取消", null).show();

            }
        });
        recyclerView2 = findViewById(R.id.recycler);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(ReceiptActivity.this));
        recyclerView2.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("users").child(userid).child("rcrooms").orderByKey();
        FirebaseRecyclerOptions<Room> options = new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(query, Room.class).build();
        adapter2 = new FirebaseRecyclerAdapter<Room, ReceiptActivity.URoomHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull URoomHolder holder, int position, @NonNull final Room model) {
//                String title = model.getTitle();
//                Log.d(TAG, "onBindViewHolder: "+title);
                final String room_key = model.getKey();
                holder.title.setText(model.getTitle());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent into_uroom = new Intent(getApplicationContext(), URCRoomActivity.class);
                        into_uroom.putExtra("ROOM_KEY", room_key);
                        into_uroom.putExtra("ROOM_NUM", model.getNum());
                        into_uroom.putExtra("ROOM_NAME", model.getTitle());
                        startActivity(into_uroom);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ReceiptActivity.this);
                        alertDialogBuilder.setTitle("Delete this room?");
                        alertDialogBuilder
                                .setMessage("Click yes to delete!")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                DatabaseReference roomRef1 = FirebaseDatabase.getInstance().getReference("rcrooms");

                                                roomRef1.child(model.getKey()).removeValue();
                                            }
                                        })

                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
            public ReceiptActivity.URoomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.item_buroom, viewGroup, false);
                return new URoomHolder(view);
            }
        };
                recyclerView2.setAdapter(adapter2);

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
            adapter2.startListening();
        }

        @Override
        public void onStop() {
            super.onStop();
            adapter2.stopListening();
        }
        private void setBoNaView () {

            BottomNavigationViewEx b = (BottomNavigationViewEx) findViewById(R.id.bar);
            BoNaViewHelper.setBoNaView(b);
            BoNaViewHelper.enableNavigation(mContext, b);
            Menu menu = b.getMenu();
            MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
            menuItem.setChecked(true);
        }

    }