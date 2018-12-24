package com.eric.timicaller31.DownloadRoom;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.DownloadActivity;
import com.eric.timicaller31.UploadRoom.URoomHelper;
import com.eric.timicaller31.R;
import com.eric.timicaller31.UploadRoom.AddNewRoomActivity;

public class IntoRoomFragment extends Fragment {
    private RecyclerView recyclerView;
    private DRoomAdapter adapter;
    private DRoomHelper helper;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intoroom,container,false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),IntoNewRoomActivity.class);
                startActivity(intent);//c
            }
        });
        helper = new DRoomHelper(getContext());
        Cursor cursor = helper.getReadableDatabase()
                .query("DROOM",null,null,null,null,null,null);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        adapter = new IntoRoomFragment.DRoomAdapter(cursor);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public class DRoomAdapter extends RecyclerView.Adapter<IntoRoomFragment.DRoomAdapter.DRoomHolder>{
        private final Cursor cursor;

        public DRoomAdapter(Cursor cursor) {
            this.cursor = cursor;
        }

        @NonNull
        @Override
        public IntoRoomFragment.DRoomAdapter.DRoomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_room,viewGroup,false);
            return new IntoRoomFragment.DRoomAdapter.DRoomHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DRoomHolder droomHolder, final int i) {
            cursor.moveToPosition(i);

            String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
//            String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
            droomHolder.nameText.setText(name);
            droomHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent todroom = new Intent(getContext(), DRoomActivity.class);
                    cursor.moveToPosition(i);

                    String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
                    String uri = cursor.getString(cursor.getColumnIndex("COL_URI"));


                    todroom.putExtra("NAME", name);
                    todroom.putExtra("URI", uri);

                    startActivity(todroom);
                }
            });
            droomHolder.aDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(i);//沒定址會刪到最下面的資料
                    String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
                    long check = helper.getWritableDatabase().delete(
                            "DROOM", "COL_NAME =?",new String[]{name});
                    //  重整畫面
                    startActivity(new Intent(getContext(),DownloadActivity.class));


                }
            });
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public class DRoomHolder extends RecyclerView.ViewHolder{
            TextView nameText;
            ImageView aDel;
            public DRoomHolder(@NonNull View itemView) {
                super(itemView);
                nameText = itemView.findViewById(R.id.tv_name);
                aDel = itemView.findViewById(R.id.del);
            }
        }
    }
}
