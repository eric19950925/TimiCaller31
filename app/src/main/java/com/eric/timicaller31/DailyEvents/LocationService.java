package com.eric.timicaller31.DailyEvents;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

public class LocationService {
    private LocationManager lms;
    private double longitude = 0;
    private double latitude = 0;
    private Context mContext;
    private String provider;
    private MyLocationListener myLocationListener;
    private String TAG= LocationService.class.getSimpleName();
//LocationService(Context ctx){mContext=ctx;}
    public LocationService(Context context){

        lms = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();    //資訊提供者選取標準
        provider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者

        if (provider == null) {

            return;
        }
        else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Toast.makeText(this, "進入if迴圈", Toast.LENGTH_LONG).show();
            return;
        }
        else {

            myLocationListener = new MyLocationListener();
            Looper.prepare();
            lms.requestSingleUpdate("network", myLocationListener, null);
            Looper.loop();
            Looper.myLooper().quit();
        }
    }


    public double getLongitude(){
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void onResume() {
        // 取得位置提供者，不下條件，讓系統決定最適用者，true 表示生效的 provider        String provider = this.locationMgr.getBestProvider(new Criteria(), true);
            Criteria criteria = new Criteria();    //資訊提供者選取標準
            provider = this.lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者

        if (provider == null) {

            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Toast.makeText(this, "進入if迴圈", Toast.LENGTH_LONG).show();
            return;
        } else {
//            Toast.makeText(this, "資源讀取中...", Toast.LENGTH_LONG).show();
//            lms = (LocationManager)getSystemService(LOCATION_SERVICE);    //取得系統定位服務
//            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);


            myLocationListener = new MyLocationListener();

//            lms.requestLocationUpdates("network", 0, 0, myLocationListener);
//            Log.d(TAG, "first get = "+latitude+"+"+longitude);


            //Toast.makeText(this, "最佳定位方式:"+bestProvider, Toast.LENGTH_LONG).show();
//            myLocationListener = new MyLocationListener();
//            Looper.prepare();
//            mloop =Looper.myLooper();
//            mainHandler = new Handler(Looper.myLooper());
            this.lms.requestSingleUpdate("network", myLocationListener, null);

//            lms.requestLocationUpdates("network", 0, 0, locationListener);
//            locationManager.requestLocationUpdates("network", 0, 0, locationListener);
//            lms.removeUpdates(myLocationListener);
            // 註冊 listener，兩個 0 不適合在實際環境使用，太耗電        this.locationMgr.requestLocationUpdates(provider, 0, 0, mLocationListener);
        }

    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //获取纬度，平行
            latitude = location.getLatitude();
            //获取经度，垂直
            longitude = location.getLongitude();
            Log.d(TAG, "LS = "+latitude+"+"+longitude);
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
