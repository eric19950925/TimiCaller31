package com.eric.timicaller31.DailyEvents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    private static final String TAG = "zzzzzzzzzzzzzzzzz~~";
    Context context;
    private ProgressDialog pDialog;
    private String massage;

    BackgroundWorker (Context ctx) {
        context = ctx;
    }AlertDialog alertDialog;
    @Override
    protected String doInBackground(String... params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String username = params[1];
        String userid = params[2];
        String longitude = params[3];
        String latitude = params[4];
        String time = params[5];
        massage = params[6];
        String ap_ip = params[7];
        String ap_ssid = params[8];
        String ap_rssi = params[9];

        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("userid", userid)
                .add("longitude", longitude)
                .add("latitude", latitude)
                .add("time",time)
                .add("m_code", massage)
                .add("ap_ip",ap_ip)
                .add("ap_ssid", ap_ssid)
                .add("ap_rssi", ap_rssi)
                .build();
        Request request = new Request.Builder()
//                .url("http://192.168.4.11/location.php")
                .url("http://122.146.55.211/location.php")
//                .url("http://192.168.43.26/location.php")
                .post(body)
                .build();


        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://192.168.43.26/location.php")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            Log.d(TAG, response.body().toString());
//            Log.d(TAG, response.body().string());//可以看到完整原碼，以確認聯網成功。
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    @Override
    protected void onPreExecute() {
//        if(!massage.equals("5")){
//            alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("正在回報");
//            alertDialog.setMessage("上傳時空資訊");
//            alertDialog.show();
//    }
//        String input = editTextInput.getText().toString();

//        Intent serviceIntent = new Intent(context, NotiService.class);
//        serviceIntent.putExtra("inputExtra", "正在上傳時空資訊");
//
//        ContextCompat.startForegroundService(context, serviceIntent);

    }

    @Override
    protected void onPostExecute(String result) {
//        if(!massage.equals("5")){
//            alertDialog.setTitle("正在回報");
//            alertDialog.setMessage("上傳完畢");
//            alertDialog.show();
//    }
        Intent serviceIntent = new Intent(context, NotiService.class);
        serviceIntent.putExtra("inputExtra", "上傳完畢");

        ContextCompat.startForegroundService(context, serviceIntent);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
