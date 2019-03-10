package com.eric.timicaller31.DailyEvents;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.util.Log;

import java.util.Calendar;

public class ExampleJobService extends JobService{
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    private LocationManager lms;
    private String bestProvider;    //最佳資訊提供者

    private static final int REQUEST_ACCESS_FINE_LOCATION = 8;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 11;
    private String smin;
    private String shour;
    private String ssid;
    private int ip;
    private int rssi;
    private String ipAddress;
    private String userid;


    private double latitude;
    private double longitude;
    private Looper mloop;
    private MyLocationListener myLocationListener;
    private HandlerThread handlerThread;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");

        doBackgroundWork(params);
//        lms.removeUpdates(locationListener);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "get1 = "+latitude+"+"+longitude);
                int i = 1;
                while( i==1 ){

                    if (jobCancelled) {//超重要別漏掉別擺錯位置
                        return;
                    }

                    locationServiceInitial();

//                    upload("122", "25", "5");

//                    Intent serviceIntent = new Intent(ExampleJobService.this, NotiService.class);
//                    serviceIntent.putExtra("inputExtra", "抓取位置資訊");
//                    ContextCompat.startForegroundService(ExampleJobService.this, serviceIntent);

                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "job Finished");
                jobFinished(params, false);
            }
        }).start();
    }

    private void upload(String lo, String la,String massage) {
        String type = "location";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if(hour-10<0){
            shour = "0"+String.valueOf(hour);
        }
        else {
            shour =String.valueOf(hour);
        }
        if(min-10<0){
            smin = "0"+String.valueOf(min);
        }
        else {
            smin=String.valueOf(min);
        }
        String UN = getSharedPreferences("Timi", MODE_PRIVATE)
                .getString("USERNAME", null);
        get_wifiinfo();
        userid = getSharedPreferences("Timi", MODE_PRIVATE)
                .getString("USERID", null);


        Log.d(TAG, "UPLOAD = "+latitude+"+"+longitude);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,UN, userid, lo,
                la,String.valueOf(year)+"-"+String.valueOf(month + 1)+"-"+String.valueOf(date)+"-"+shour+"-"+smin
                ,massage, ipAddress, ssid,String.valueOf(rssi));



    }

    private void get_wifiinfo() {
        ////wifi info
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") WifiInfo info = wifi.getConnectionInfo();
        //String maxText = info.getMacAddress();
        ip = info.getIpAddress();
        ipAddress = Formatter.formatIpAddress(ip);
        ssid = info.getSSID();
        //int networkID = info.getNetworkId();
        //int speed = info.getLinkSpeed();
        rssi = info.getRssi();
        if(rssi <-70){
//            Toast.makeText(ExampleJobService.this, "rssi:"+rssi, Toast.LENGTH_LONG).show();

//            new AlertDialog.Builder(ExampleJobService.this).setTitle("即將失去網路連線，請往訊號良好之處移動!")
//                    .setPositiveButton("我知道了", null).show();
            Intent serviceIntent = new Intent(this, NotiService.class);
            serviceIntent.putExtra("inputExtra", "即將失去連線，請網訊號良好處移動");
            ContextCompat.startForegroundService(this, serviceIntent);

        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    private String locationServiceInitial() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Toast.makeText(this, "進入if迴圈", Toast.LENGTH_LONG).show();
            return "EDF";
        }
        else{
//            Toast.makeText(this, "資源讀取中...", Toast.LENGTH_LONG).show();
            lms = (LocationManager) getSystemService(LOCATION_SERVICE);	//取得系統定位服務

            Criteria criteria = new Criteria();	//資訊提供者選取標準
            bestProvider = lms.getBestProvider(criteria, true);	//選擇精準度最高的提供者
            myLocationListener = new MyLocationListener();

//            lms.requestLocationUpdates("network", 0, 0, myLocationListener);
//            Log.d(TAG, "first get = "+latitude+"+"+longitude);


            //Toast.makeText(this, "最佳定位方式:"+bestProvider, Toast.LENGTH_LONG).show();
            handlerThread = new HandlerThread("MyHandlerThread");
            handlerThread.start();
            mloop = handlerThread.getLooper();
            myLocationListener = new MyLocationListener();
//            Looper.prepare();

            lms.requestSingleUpdate("network", myLocationListener,mloop);

//            lms.requestLocationUpdates("network", 0, 0, myLocationListener,mloop);

//            lms.removeUpdates(myLocationListener);
//            Looper.myLooper().quit();
//            mloop.quit();
//            Looper.loop();
//            mloop =Looper.myLooper();
//            mloop=null;
//            Log.d(TAG, "mloop:"+mloop);
//            mloop.quit();

            Log.d(TAG, "ready to sleep");

//            lms.removeUpdates(myLocationListener);
//            mainHandler.getLooper().quitSafely();
            return "ABC";
            //以位置管理員每(0毫秒)呼叫位置監聽服務

        }


    }
    public class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            //获取纬度，平行
            latitude = location.getLatitude();
            //获取经度，垂直
            longitude = location.getLongitude();
            Log.d(TAG, "first get = "+latitude+"+"+longitude);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(DailyEventsActivity.this, "經度:"+String.valueOf(longitude)+"，緯度:"+String.valueOf(latitude), Toast.LENGTH_LONG).show();
//
//                }
//
//            });
//           Toast.makeText(DailyEventsActivity.this, "準備顯示選單", Toast.LENGTH_LONG).show();

            upload(String.valueOf(longitude),String.valueOf(latitude),"5");
            lms.removeUpdates(myLocationListener);
            mloop.quitSafely();
            handlerThread.quitSafely();
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

}
