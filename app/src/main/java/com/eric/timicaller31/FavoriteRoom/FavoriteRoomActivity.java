package com.eric.timicaller31.FavoriteRoom;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.BoNaViewHelper;
import com.eric.timicaller31.DailyEvents.EventHelper;
import com.eric.timicaller31.R;
import com.eric.timicaller31.DailyEvents.VisitRoomActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class FavoriteRoomActivity extends AppCompatActivity {
private static final int ACTIVITY_NUM = 1;
private Context mContext = FavoriteRoomActivity.this;
    String userid,rbid;
    RecyclerView recyclerView1,recyclerView2,recyclerView3;
    private FRoomAdapter adapter1;
    private EventHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setBoNaView();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("收藏的公布欄"));
        userid = this.getSharedPreferences("Timi", Context.MODE_PRIVATE)
                .getString("USERID", "");
//        Log.d(TAG, "onCreateView: "+userid);

        helper = new EventHelper(this);
        Cursor cursor = helper.getReadableDatabase()
                .query("FAVORITE_ROOM",null,null,null,null,null,null);
        adapter1 = new FRoomAdapter(cursor);
        recyclerView1 = findViewById(R.id.recycler);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.getItemAnimator().setRemoveDuration(1000);
        recyclerView1.setAdapter(adapter1);

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
            View view = getLayoutInflater().inflate(R.layout.item_buroom,parent,false);
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
                    vr.putExtra("ROOM_NAME",cursor.getString(cursor.getColumnIndex("COL_NAME")));
                    vr.putExtra("ROOM_BUILDERID",cursor.getString(cursor.getColumnIndex("COL_BUILDER_NAME")));
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
