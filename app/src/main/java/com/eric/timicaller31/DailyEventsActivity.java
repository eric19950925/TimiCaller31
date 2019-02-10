package com.eric.timicaller31;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.timicaller31.DailyEvents.AlarmReceiver;
import com.eric.timicaller31.DailyEvents.Detail_MyAlarmActivity;
import com.eric.timicaller31.DailyEvents.EventHelper;
import com.eric.timicaller31.ObjectClass.Member;
import com.eric.timicaller31.ObjectClass.Room;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static java.lang.String.valueOf;

public class DailyEventsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, ValueEventListener {
    private static final String TAG = DailyEventsActivity.class.getSimpleName();
    private static final int REQUEST_ACCESS_FINE_LOCATION = 8;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 11;
    private Context mContext = DailyEventsActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private static final int REQUEST_LOGIN = 100;
    private static final int RC_SIGN_IN = 101;
    boolean logon = false;
    String userid;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Member member;
    private String edrk;
    private RecyclerView recyclerView;
    private EventHelper helper;
    private EventAdapter adapter;
    private Calendar cal;
    private TextView t;
    private boolean getService = false;        //是否已開啟定位服務
    private MyLocationListener myLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        setBoNaView();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabloca = (FloatingActionButton) findViewById(R.id.qrloca);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("個人管理"));
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        helper = new EventHelper(this);
        Cursor cursor = helper.getReadableDatabase()
                .query("EVENT", null, null, null, null, null, null);
        adapter = new EventAdapter(cursor);
        recyclerView.setAdapter(adapter);
        FloatingActionButton qrfab = (FloatingActionButton) findViewById(R.id.qrfab2);
        qrfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gq = new Intent(DailyEventsActivity.this, QRSActivity.class);
                startActivity(gq);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edrk = "DailyEvents";
                Intent new1 = new Intent(DailyEventsActivity.this, NewAlarmActivity.class);
                new1.putExtra("ROOM_KEY", edrk);
                startActivity(new1);
            }
        });
        fabloca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get location
                LocationManager status = (LocationManager) (mContext.getSystemService(Context.LOCATION_SERVICE));
                if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
                    getService = true;
                    locationServiceInitial();
                } else {
                    Toast.makeText(mContext, "請開啟定位服務", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    //開啟設定頁面
                }

            }

        });
    }

            private void SafetyCheck(final String longitude, final String latitude) {
                final List<String> inputway = new ArrayList<String>();
                inputway.add("1、全隊平安" );
                inputway.add("2、有人受傷--腳-無法移動，請求救援！");
                inputway.add("3、有人受傷--脊椎--無法移動，請求救援！");
                inputway.add("4、遭綁架脅迫--請報警！");
                final AlertDialog.Builder dialog_list = new AlertDialog.Builder(DailyEventsActivity.this);



                dialog_list.setTitle("回報訊息");
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                        DailyEventsActivity.this,android.R.layout.simple_list_item_1,inputway){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Cast list view each item as text view
                        TextView text_view = (TextView) super.getView(position,convertView,parent);

                        // Set item text font
                        //text_view.setTypeface(mTypeface);

                        // Set text size
                        text_view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);

                        // Get the list view odd and even position items
                        if(position == 0){ //  items1
                            // Set the list view one by one items (row) background color
                            text_view.setBackgroundColor(Color.parseColor("#FFFFFF"));

                            // Set the list view one by one items text color
                            text_view.setTextColor(Color.parseColor("#7B68EE"));
                        }else if(position==1){ //  items2
                            // Set the list view alternate items (row) background color
                            text_view.setBackgroundColor(Color.parseColor("#D3D3D3"));

                            // Set the list view alternate items text color
                            text_view.setTextColor(Color.parseColor("#7B68EE"));
                        }else if(position==2){ //  items2
                            // Set the list view alternate items (row) background color
                            text_view.setBackgroundColor(Color.parseColor("#FFFFFF"));

                            // Set the list view alternate items text color
                            text_view.setTextColor(Color.parseColor("#7B68EE"));
                        }else if(position==3){ //  items2
                            // Set the list view alternate items (row) background color
                            text_view.setBackgroundColor(Color.parseColor("#D3D3D3"));

                            // Set the list view alternate items text color
                            text_view.setTextColor(Color.parseColor("#7B68EE"));
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

                            private String massage;

                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // Get the alert dialog selected item's text
                                String selectedItem = inputway.get(which);

                                // Display the selected item's text on toast
//                                Toast.makeText(getContext(),"Checked : " + selectedItem,Toast.LENGTH_LONG).show();
                                Toast.makeText(DailyEventsActivity.this, "你選的是" + selectedItem, Toast.LENGTH_SHORT).show();
                                switch(which) {
                                    case 0:
                                        massage = "1";
                                        upload(String.valueOf(longitude),String.valueOf(latitude), massage);
                                        lms.removeUpdates(myLocationListener);

                                        break;
                                    case 1:
                                        massage = "2";
                                        upload(String.valueOf(longitude),String.valueOf(latitude),massage);
                                        lms.removeUpdates(myLocationListener);

                                        break;
                                    case 2:
                                        massage = "3";
                                        upload(String.valueOf(longitude),String.valueOf(latitude),massage);
                                        lms.removeUpdates(myLocationListener);

                                        break;
                                    case 3:
                                        massage = "4";
                                        upload(String.valueOf(longitude),String.valueOf(latitude),massage);
                                        lms.removeUpdates(myLocationListener);

                                        break;


                                }
                            }
                        });

                // Set the a;ert dialog positive button
                dialog_list.setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Just dismiss the alert dialog after selection
                        // Or do something now
                        lms.removeUpdates(myLocationListener);
                    }
                });
                // Create the alert dialog
                AlertDialog dialog = dialog_list.create();

                // Finally, display the alert dialog
                dialog.show();
            }
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;	//最佳資訊提供者
    private void locationServiceInitial() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Toast.makeText(this, "進入if迴圈", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(DailyEventsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(DailyEventsActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
            return;
        }
        else{
            Toast.makeText(this, "資源讀取中...", Toast.LENGTH_LONG).show();
            lms = (LocationManager) getSystemService(LOCATION_SERVICE);	//取得系統定位服務
            Criteria criteria = new Criteria();	//資訊提供者選取標準
            bestProvider = lms.getBestProvider(criteria, true);	//選擇精準度最高的提供者
            //Toast.makeText(this, "最佳定位方式:"+bestProvider, Toast.LENGTH_LONG).show();
            myLocationListener = new MyLocationListener();
            lms.requestLocationUpdates("network", 0, 0, myLocationListener);
            //以位置管理員每(五小時:18000000)呼叫位置監聽服務

        }
    }
    private class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            final double latitude = location.getLatitude();//获取纬度，平行
            final double longitude = location.getLongitude();//获取经度，垂直
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(DailyEventsActivity.this, "經度:"+String.valueOf(longitude)+"，緯度:"+String.valueOf(latitude), Toast.LENGTH_LONG).show();
//
//                }
//
//            });
           //Toast.makeText(DailyEventsActivity.this, "準備顯示選單", Toast.LENGTH_LONG).show();
            SafetyCheck(String.valueOf(longitude),String.valueOf(latitude));
//            upload(String.valueOf(longitude),String.valueOf(latitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    private void upload(String lo, String la,String massage) {
        String type = "location";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, "eric02", "0910", lo,
                la, String.valueOf(hour),
                String.valueOf(min), String.valueOf(year), String.valueOf(month + 1), String.valueOf(date),massage);
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
            int year = cursor.getInt(cursor.getColumnIndex("COL_YEAR"));
            int month = cursor.getInt(cursor.getColumnIndex("COL_MONTH"));
            int date = cursor.getInt(cursor.getColumnIndex("COL_DATE"));
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
            holder.hrtext.setText(valueOf(hr));
            holder.mintext.setText(valueOf(min));
            holder.yeartext.setText(valueOf(year));
            holder.monthtext.setText(valueOf(month));
            holder.datetext.setText(valueOf(date));
            holder.imageView.setImageDrawable(rb);
//
//            if(alarmon==1){
//                holder.aSwitch.setChecked(true);
//                startalarm( year,month,date,hr,min,name,phone,hint,imagearray);
//            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent todetails = new Intent(DailyEventsActivity.this, Detail_MyAlarmActivity.class);
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
                int year = cursor.getInt(cursor.getColumnIndex("COL_YEAR"));
                int month = cursor.getInt(cursor.getColumnIndex("COL_MONTH"));
                int date = cursor.getInt(cursor.getColumnIndex("COL_DATE"));
                String phone = cursor.getString(cursor.getColumnIndex("COL_PHONE"));
                String hint = cursor.getString(cursor.getColumnIndex("COL_HINT"));
                byte[] imagearray = cursor.getBlob(cursor.getColumnIndex("COL_IMAGE"));
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        startalarm(hour,min,year,month,date,name,phone,hint,imagearray);
//                        Toast.makeText(buttonView.getContext(), hour*100+min+" ALARM ON", Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put("COL_ACTIVE", true);
                        helper.getWritableDatabase().update(
                                "EVENT", values, "COL_NAME =?",new String[]{name});
                    }
                    else {
                        cancelalarm();
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
                    startActivity(new Intent(DailyEventsActivity.this, DailyEventsActivity.class));


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
            TextView yeartext;
            TextView monthtext;
            TextView datetext;
            Switch aSwitch;
            ImageView aDel;
            ImageView imageView;
            public EventHolder(View itemView) {
                super(itemView);
                nametext = itemView.findViewById(R.id.tv_name);
                hrtext = itemView.findViewById(R.id.tv_hour);
                mintext = itemView.findViewById(R.id.tv_min);
                yeartext = itemView.findViewById(R.id.tv_yy);
                monthtext = itemView.findViewById(R.id.tv_mm);
                datetext = itemView.findViewById(R.id.tv_dd);
                aSwitch =itemView.findViewById(R.id.switch1);
                aDel = itemView.findViewById(R.id.del);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }
    }
    private void startalarm(int hour,int min,int year,int month,int date,String name,String phone,String hint ,byte[] imagearray){
        cal = Calendar.getInstance();
        cal.set(year, month-1, date, hour, min, 00);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;
        long time;

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, min);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction("0925");
        intent.putExtra("NAME", name);
        intent.putExtra("PHONE", phone);
        intent.putExtra("IMAGE", imagearray);
        intent.putExtra("HINT", hint);
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
//        if(System.currentTimeMillis()>time)
//        {
//            if (calendar.AM_PM == 0)
//                time = time + (1000*60*60*12);
//            else
//                time = time + (1000*60*60*24);
//        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, cal.getTime()+"Alarm On", Toast.LENGTH_LONG).show();
    }
    private void cancelalarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction("0925");
//                        intent.putExtra("NAME", name);
//                        intent.putExtra("PHONE", phone);
//                        intent.putExtra("IMAGE", imagearray);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, cal.getTime()+"Alarm Off", Toast.LENGTH_LONG).show();

    }
    private void setBoNaView(){

        BottomNavigationViewEx b = (BottomNavigationViewEx)findViewById(R.id.bar);
        BoNaViewHelper.setBoNaView(b);
        BoNaViewHelper.enableNavigation(mContext,b);
        Menu menu = b.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        if(user !=null){
            FirebaseDatabase.getInstance().getReference("users")
                    .child(user.getUid())
                    .child("uid")
                    .setValue(user.getUid());
            FirebaseDatabase.getInstance().getReference("users")
                    .child(user.getUid())
                    .addValueEventListener(this);
            userid = user.getUid();
            getSharedPreferences("Timi",MODE_PRIVATE)
                    .edit()
                    .putString("USERID", userid)
                    .apply();

        }
        else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(Arrays.asList(
                    //new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            )).setIsSmartLockEnabled(false).build(),RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
//        auth.signOut();
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        member = dataSnapshot.getValue(Member.class);
//        if(member.getNickname()==null){
//
//        }
//        else {
//            FirebaseDatabase.getInstance().getReference("users")
//                    .child(user.getUid())
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            member = dataSnapshot.getValue(Member.class);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    //RequestHandler如何使用?
//    class RegisterUser extends AsyncTask<Void, Void, String> {
////
////        private ProgressBar progressBar;
////
////        @Override
////        protected String doInBackground(Void... voids) {
////            //creating request handler object
////            RequestHandler requestHandler = new RequestHandler() {
////            //creating request parameters
////            HashMap<String, String> params = new HashMap<>();
////            params.put("username", username);
////            params.put("email", email);
////            params.put("password", password);
////            params.put("gender", gender);
////
////            //returing the response
////            return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
////        }
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////            //displaying the progress bar while user registers on the server
////            progressBar = (ProgressBar) findViewById(R.id.progressBar);
////            progressBar.setVisibility(View.VISIBLE);
////        }
////
////        @Override
////        protected void onPostExecute(String s) {
////            super.onPostExecute(s);
////            //hiding the progressbar after completion
////            progressBar.setVisibility(View.GONE);
////
////            try {
////                //converting response to json object
////                JSONObject obj = new JSONObject(s);
////
////                //if no error in response
////                if (!obj.getBoolean("error")) {
////                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
////
////                    //getting the user from the response
////                    JSONObject userJson = obj.getJSONObject("user");
////
////                    //creating a new user object
////                    User user = new User(
////                            userJson.getInt("id"),
////                            userJson.getString("username"),
////                            userJson.getString("email"),
////                            userJson.getString("gender")
////                    );
////
////                    //storing the user in shared preferences
////                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
////
////                    //starting the profile activity
////                    finish();
////                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
////                } else {
////                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
////                }
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        }
////
////
////    //executing the async task
////    RegisterUser ru = new RegisterUser();
////        ru.execute();
////}
}
