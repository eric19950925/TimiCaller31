package com.eric.timicaller31.UploadRoom;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.timicaller31.DownloadActivity;
import com.eric.timicaller31.MainActivity;
import com.eric.timicaller31.R;
import com.eric.timicaller31.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static android.content.Context.MODE_PRIVATE;

public class MyRoomFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private String userid;
    private FirebaseRecyclerAdapter<Room, URoomHolder> adapter;
    private String TAG = MyRoomFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_myroom, container, false);
        userid = this.getActivity().getSharedPreferences("Timi", Context.MODE_PRIVATE)
                .getString("USERID", "");
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ed_title = new EditText(getContext());
                new AlertDialog.Builder(getContext()).setTitle("Room title")
                        .setView(ed_title)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String roomTitle = ed_title.getText().toString();
                                DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("users")
                                        .child(userid).child("rooms").push();
                                Room room = new Room();
                                room.setTitle(roomTitle);
                                roomRef.setValue(room);
                                String key = roomRef.getKey();
                                roomRef.child("key").setValue(key);

                            }
                        }).setNeutralButton("Cancel", null).show();
            }
        });
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .child(userid).child("rooms").orderByKey();
        FirebaseRecyclerOptions<Room> options = new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(query, Room.class).build();
        adapter = new FirebaseRecyclerAdapter<Room, URoomHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull URoomHolder holder, int position, @NonNull final Room model) {
                String title = model.getTitle();
                Log.d(TAG, "onBindViewHolder: "+title);
                holder.title.setText(model.getTitle());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent into_uroom = new Intent(getContext(),URoomActivity.class);
                        into_uroom.putExtra("ROOM_KEY",model.getId());
                        startActivity(into_uroom);
                    }
                });
            }

            @NonNull
            @Override
            public URoomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.item_uroom,viewGroup,false);
                return new URoomHolder(view);

            }
        };
        recyclerView.setAdapter(adapter);
        return view;
    }

    public class URoomHolder extends RecyclerView.ViewHolder {
        TextView title;

        public URoomHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_name);
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
}
