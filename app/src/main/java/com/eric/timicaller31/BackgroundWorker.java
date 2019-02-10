package com.eric.timicaller31;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
        String hour = params[5];
        String min = params[6];
        String year = params[7];
        String month = params[8];
        String date = params[9];
        String massage = params[10];
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("userid", userid)
                .add("longitude", longitude)
                .add("latitude", latitude)
                .add("hour", hour)
                .add("min", min)
                .add("year", year)
                .add("month", month)
                .add("date", date)
                .add("massage",massage)
                .build();

        Request request = new Request.Builder()
                .url("http://122.146.54.250/location.php")
//                .url("http://192.168.43.26/location.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
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
//        String type = params[0];
//        String location_url = "https://tw.yahoo.com";
//        //String location_url = "192.168.43.26/location.php";
//        if(type.equals("location")) {
//            try {
//                String username = params[1];
//                String userid = params[2];
//                String longitude = params[3];
//                String latitude = params[4];
//                String hour = params[5];
//                String min = params[6];
//                String year = params[7];
//                String month = params[8];
//                String date = params[9];
//                URL url = new URL(location_url);
//                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                String post_data =
//                        URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
//                        +URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8")+"&"
//                        +URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(longitude,"UTF-8")+"&"
//                        +URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(latitude,"UTF-8")+"&"
//                        +URLEncoder.encode("hour","UTF-8")+"="+URLEncoder.encode(hour,"UTF-8")+"&"
//                        +URLEncoder.encode("min","UTF-8")+"="+URLEncoder.encode(min,"UTF-8")+"&"
//                        +URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8")+"&"
//                        +URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode(month,"UTF-8")+"&"
//                        +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8");
//                bufferedWriter.write(post_data);
//                bufferedWriter.flush();
//                bufferedWriter.close();
//                outputStream.close();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
//                String result="";
//                String line="";
//                while((line = bufferedReader.readLine())!= null) {
//                    result += line;
//                }
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//                return result;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////NameValue是eclips的用語嗎?
////        List<NameValuePair> params = new ArrayList<NameValuePair>();
////        params.add(new BasicNameValuePair("name", name));
////        params.add(new BasicNameValuePair("price", price));
////        params.add(new BasicNameValuePair("description", description));


        return null;
    }
    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("正在回報");
        alertDialog.setMessage("上傳時空資訊");
        alertDialog.show();
//        pDialog = new ProgressDialog(context);
//        pDialog.setMessage("Creating Product..");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(true);
//        pDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setTitle("正在回報");
        alertDialog.setMessage("上傳完畢");
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
