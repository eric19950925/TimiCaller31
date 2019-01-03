package com.eric.timicaller31;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.timicaller31.DailyEvents.EventHelper;
import com.eric.timicaller31.ObjectClass.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRoomActivity extends AppCompatActivity {
private static final int ACTIVITY_NUM = 2;
private Context mContext = FavoriteRoomActivity.this;
    String userid;
    RecyclerView recyclerView1,recyclerView2,recyclerView3;
    private FRoomAdapter adapter1;
    private EventHelper helper;
    private FirebaseRecyclerAdapter<Room, URoomHolder> adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setBoNaView();
        userid = this.getSharedPreferences("Timi", Context.MODE_PRIVATE)
                .getString("USERID", "");
//        Log.d(TAG, "onCreateView: "+userid);
        helper = new EventHelper(this);
        Cursor cursor = helper.getReadableDatabase()
                .query("FAVORITE_ROOM",null,null,null,null,null,null);
        adapter1 = new FRoomAdapter(cursor);
        recyclerView1 = findViewById(R.id.recycler1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.getItemAnimator().setRemoveDuration(1000);
        recyclerView1.setAdapter(adapter1);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                edrk = "DailyEvents";
                final List<String> inputway = new ArrayList<String>();
                inputway.add("Receipt");
                inputway.add("Google sheet");
                final android.app.AlertDialog.Builder dialog_list = new android.app.AlertDialog.Builder(FavoriteRoomActivity.this);
                dialog_list.setTitle("Build");
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                        FavoriteRoomActivity.this,android.R.layout.simple_list_item_1,inputway){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Cast list view each item as text view
                        TextView text_view = (TextView) super.getView(position,convertView,parent);

                        // Set item text font
                        //text_view.setTypeface(mTypeface);

                        // Set text size
                        text_view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);

                        // Get the list view odd and even position items
                        if(position == 0){ //  items1
                            // Set the list view one by one items (row) background color
                            text_view.setBackgroundColor(Color.parseColor("#ffff0000"));

                            // Set the list view one by one items text color
                            text_view.setTextColor(Color.parseColor("#FFFFFFFF"));
                        }else if(position==1){ //  items2
                            // Set the list view alternate items (row) background color
                            text_view.setBackgroundColor(Color.parseColor("#F8F8FF00"));

                            // Set the list view alternate items text color
                            text_view.setTextColor(Color.parseColor("#FF7F50"));
                        }

                        // Finally, return the modified items
                        return text_view;
                    }
                };

                // Set a single choice items list for alert dialog
                dialog_list.setSingleChoiceItems(
                        arrayAdapter, // Items list
                        -1, // Index of checked item (-1 = no selection)
                        new DialogInterface.OnClickListener() // Item click listener
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // Get the alert dialog selected item's text
                                String selectedItem = inputway.get(which);

                                // Display the selected item's text on toast
//                                Toast.makeText(getContext(),"Checked : " + selectedItem,Toast.LENGTH_LONG).show();
                                Toast.makeText(FavoriteRoomActivity.this, "你選的是" + selectedItem, Toast.LENGTH_SHORT).show();
                                switch(which) {
                                    case 0:
                                        final EditText ed_title = new EditText(FavoriteRoomActivity.this);
                                        new AlertDialog.Builder(FavoriteRoomActivity.this).setTitle("Add Receipt Room")
                                                .setView(ed_title)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        String rcroomTitle = ed_title.getText().toString();
                                                        DatabaseReference rcroomRef = FirebaseDatabase.getInstance().getReference("rcrooms").push();
                                                        Room room = new Room();
                                                        room.setTitle(rcroomTitle);
                                                        room.setId(userid);
                                                        room.setNum("");
                                                        rcroomRef.setValue(room);
                                                        String key = rcroomRef.getKey();
                                                        room.setKey(key);
                                                        rcroomRef.child("key").setValue(key);


                                                    }
                                                }).setNeutralButton("Cancel", null).show();

                                        break;
                                    case 1:
//                                        Intent new2 = new Intent(FavoriteRoomActivity.this, NewTimerActivity.class);
//                                        startActivity(new2);
                                        break;

                                }
                            }
                        });

                // Set the a;ert dialog positive button
                dialog_list.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Just dismiss the alert dialog after selection
                        // Or do something now
                    }
                });

                // Create the alert dialog
                android.app.AlertDialog dialog = dialog_list.create();

                // Finally, display the alert dialog
                dialog.show();
            }
        });




        //rc
        recyclerView2 = findViewById(R.id.recycler2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(FavoriteRoomActivity.this));
        recyclerView2.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("rcrooms").orderByKey();
        FirebaseRecyclerOptions<Room> options = new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(query, Room.class).build();
        adapter2 = new FirebaseRecyclerAdapter<Room, URoomHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull URoomHolder holder, int position, @NonNull final Room model) {
//                String title = model.getTitle();
//                Log.d(TAG, "onBindViewHolder: "+title);
                final String room_key = model.getKey();
                holder.title.setText(model.getTitle());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent into_uroom = new Intent(getApplicationContext(),URCRoomActivity.class);
                        into_uroom.putExtra("ROOM_KEY",room_key);
                        into_uroom.putExtra("ROOM_NUM",model.getNum());
                        startActivity(into_uroom);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(FavoriteRoomActivity.this);
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
            public FavoriteRoomActivity.URoomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.item_uroom,viewGroup,false);
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
    private void setBoNaView(){

        BottomNavigationViewEx b = (BottomNavigationViewEx)findViewById(R.id.bar);
        BoNaViewHelper.setBoNaView(b);
        BoNaViewHelper.enableNavigation(mContext,b);
        Menu menu = b.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
    public class FRoomAdapter extends RecyclerView.Adapter<FRoomAdapter.FRoomHolder>implements View.OnClickListener {
        Cursor cursor;

        public FRoomAdapter(Cursor cursor) {
            this.cursor=cursor;
        }

        @NonNull
        @Override
        public FRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_uroom,parent,false);
            return new FRoomHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final FRoomHolder holder, final int position) {
            cursor.moveToPosition(position);

            String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
            String roomkey = cursor.getString(cursor.getColumnIndex("COL_KEY"));
//            String build = cursor.getString(cursor.getColumnIndex("COL_BUILDER_NAME"));

            holder.nametext.setText(name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(position);

                    Intent vr = new Intent(FavoriteRoomActivity.this, VisitRoomActivity.class);
                    vr.putExtra("ROOM_KEY",cursor.getString(cursor.getColumnIndex("COL_KEY")));
                    startActivity(vr);
                }
            });
            holder.aDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(position);//沒定址會刪到最下面的資料
                    String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
                    helper.getWritableDatabase().delete(
                            "FAVORITE_ROOM", "COL_NAME =?",new String[]{name});
                    //  重整畫面
                    startActivity(new Intent(FavoriteRoomActivity.this, FavoriteRoomActivity.class));


                }
            });

        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        @Override
        public void onClick(View v) {

        }

        public class FRoomHolder extends RecyclerView.ViewHolder{
            TextView nametext;
            ImageView aDel;

            public FRoomHolder(View itemView) {
                super(itemView);
                nametext = itemView.findViewById(R.id.tv_name);
                aDel = itemView.findViewById(R.id.del);


            }
        }
    }
}
