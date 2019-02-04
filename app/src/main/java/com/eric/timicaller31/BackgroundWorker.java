package com.eric.timicaller31;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

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

public class BackgroundWorker extends AsyncTask<String,Void,String> {


    Context context;

    BackgroundWorker (Context ctx) {
        context = ctx;
    }AlertDialog alertDialog;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String location_url = "https://122.146.55.200:443/location.php";
        if(type.equals("location")) {
            try {
                String username = params[1];
                String userid = params[2];
                String longitude = params[3];
                String latitude = params[4];
                String hour = params[5];
                String min = params[6];
                String year = params[7];
                String month = params[8];
                String date = params[9];
                URL url = new URL(location_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =
                        URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8")+"&"
                        +URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(longitude,"UTF-8")+"&"
                        +URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(latitude,"UTF-8")+"&"
                        +URLEncoder.encode("hour","UTF-8")+"="+URLEncoder.encode(hour,"UTF-8")+"&"
                        +URLEncoder.encode("min","UTF-8")+"="+URLEncoder.encode(min,"UTF-8")+"&"
                        +URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8")+"&"
                        +URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode(month,"UTF-8")+"&"
                        +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
