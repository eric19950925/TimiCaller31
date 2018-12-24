package com.eric.timicaller31.MyEvent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.timicaller31.MainActivity;
import com.eric.timicaller31.R;

import java.util.Calendar;


public class AlarmFragment extends Fragment {


    private RecyclerView recyclerView;
    private EventHelper helper;
    private EventAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm,container,false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddNewAlarm.class);
                startActivity(intent);//c
            }
        });
        FloatingActionButton qrfab = (FloatingActionButton) view.findViewById(R.id.qrfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scan qrcode
            }
        });
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        helper = new EventHelper(getContext());
        Cursor cursor = helper.getReadableDatabase()
                .query("EVENT",null,null,null,null,null,null);
        adapter = new EventAdapter(cursor);
        recyclerView.setAdapter(adapter);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = helper.getReadableDatabase()
                .query("EVENT",null,null,null,null,null,null);
        adapter = new EventAdapter(cursor);
        recyclerView.setAdapter(adapter);
    }

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder>implements View.OnClickListener {
        Cursor cursor;

        public EventAdapter(Cursor cursor) {
            this.cursor=cursor;
        }

        @NonNull
        @Override
        public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_alarm,parent,false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final EventHolder holder, final int position) {
            cursor.moveToPosition(position);
            int alarmon = cursor.getInt(cursor.getColumnIndex("COL_ACTIVE"));
            String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
            int hr = cursor.getInt(cursor.getColumnIndex("COL_HOUR"));
            int min = cursor.getInt(cursor.getColumnIndex("COL_MIN"));
            String phone = cursor.getString(cursor.getColumnIndex("COL_PHONE"));
            String hint = cursor.getString(cursor.getColumnIndex("COL_HINT"));
            byte[] imagearray = cursor.getBlob(cursor.getColumnIndex("COL_IMAGE"));
            Bitmap bmp = BitmapFactory.decodeByteArray(imagearray, 0, imagearray.length);
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            // 设置想要的大小
            int newWidth = 240;
            int newHeight =360;
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
            RoundedBitmapDrawable rb = RoundedBitmapDrawableFactory.create(getResources(),newbm);
            rb.setCircular(true);
            holder.nametext.setText(name);
            holder.hrtext.setText(String.valueOf(hr));
            holder.mintext.setText(String.valueOf(min));
            holder.imageView.setImageDrawable(rb);
//            Log.d(TAG, "onBindViewHolder: "+alarmon);
            if(alarmon==1){
                holder.aSwitch.setChecked(true);
                startalarm( hr,min,name,phone,hint,imagearray);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent todetails = new Intent(getContext(), Detail_MyAlarmActivity.class);
                    cursor.moveToPosition(position);

                    String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
                    int hour = cursor.getInt(cursor.getColumnIndex("COL_HOUR"));
                    int min = cursor.getInt(cursor.getColumnIndex("COL_MIN"));
                    String hint = cursor.getString(cursor.getColumnIndex("COL_HINT"));
                    String phone = cursor.getString(cursor.getColumnIndex("COL_PHONE"));
                    byte[] imagearray = cursor.getBlob(cursor.getColumnIndex("COL_IMAGE"));

                    todetails.putExtra("NAME", name);
                    todetails.putExtra("HOUR",hour);
                    todetails.putExtra("MIN", min);
                    todetails.putExtra("HINT", hint);
                    todetails.putExtra("PHONE", phone);
                    todetails.putExtra("IMAGE", imagearray);

                    startActivity(todetails);
                }
            });


            holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
                int hour = cursor.getInt(cursor.getColumnIndex("COL_HOUR"));
                int min = cursor.getInt(cursor.getColumnIndex("COL_MIN"));
                String phone = cursor.getString(cursor.getColumnIndex("COL_PHONE"));
                String hint = cursor.getString(cursor.getColumnIndex("COL_HINT"));
                byte[] imagearray = cursor.getBlob(cursor.getColumnIndex("COL_IMAGE"));
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        startalarm(hour,min,name,phone,hint,imagearray);
//                        Toast.makeText(buttonView.getContext(), hour*100+min+" ALARM ON", Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put("COL_ACTIVE", true);
                        helper.getWritableDatabase().update(
                                "EVENT", values, "COL_NAME =?",new String[]{name});
                    }
                    else {
                        cancelalarm( hour,min);
//                        Toast.makeText(buttonView.getContext(), hour*100+min+" ALARM OFF", Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put("COL_ACTIVE", false);
                        helper.getWritableDatabase().update(
                                "EVENT", values, "COL_NAME =?",new String[]{name});
                    }
                }
            });
            holder.aDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(position);//沒定址會刪到最下面的資料
                    int hour = cursor.getInt(cursor.getColumnIndex("COL_HOUR"));
                    int min = cursor.getInt(cursor.getColumnIndex("COL_MIN"));
                    String name = cursor.getString(cursor.getColumnIndex("COL_NAME"));
                    ContentValues values = new ContentValues();
                    values.put("COL_ACTIVE", false);
//                    helper.getWritableDatabase().update("EVENT", values, "COL_NAME =?",new String[]{name});
//                    cancelalarm( hour,min);
                    long check = helper.getWritableDatabase().delete(
                            "EVENT", "COL_NAME =?",new String[]{name});
                    //  重整畫面
                    startActivity(new Intent(getContext(), MainActivity.class));


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

        public class EventHolder extends RecyclerView.ViewHolder{
            TextView nametext;
            TextView hrtext;
            TextView mintext;
            Switch aSwitch;
            ImageView aDel;
            ImageView imageView;
            public EventHolder(View itemView) {
                super(itemView);
                nametext = itemView.findViewById(R.id.tv_name);
                hrtext = itemView.findViewById(R.id.tv_hour);
                mintext = itemView.findViewById(R.id.tv_min);
                aSwitch =itemView.findViewById(R.id.switch1);
                aDel = itemView.findViewById(R.id.del);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }
    }
    private void startalarm(int hour,int min,String name,String phone,String hint ,byte[] imagearray){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;
        long time;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
        intent.setAction("0925");
        intent.putExtra("NAME", name);
        intent.putExtra("PHONE", phone);
        intent.putExtra("IMAGE", imagearray);
        intent.putExtra("HINT", hint);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), hour*100+min, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
        if(System.currentTimeMillis()>time)
        {
            if (calendar.AM_PM == 0)
                time = time + (1000*60*60*12);
            else
                time = time + (1000*60*60*24);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, time+100, pendingIntent);
        Toast.makeText(getContext(), hour*100+min+"Alarm On", Toast.LENGTH_LONG).show();
    }
    private void cancelalarm(int hour,int min){
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
        intent.setAction("0925");
//                        intent.putExtra("NAME", name);
//                        intent.putExtra("PHONE", phone);
//                        intent.putExtra("IMAGE", imagearray);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), hour*100+min, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getContext(), hour*100+min+"Alarm Off", Toast.LENGTH_LONG).show();

    }
}
