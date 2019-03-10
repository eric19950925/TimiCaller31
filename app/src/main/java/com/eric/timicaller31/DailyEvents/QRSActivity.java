package com.eric.timicaller31.DailyEvents;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.eric.timicaller31.ObjectClass.Room;
import com.eric.timicaller31.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRSActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, ValueEventListener {
    private static final int REQUEST_CAMERA = 1;
    private static final String TAG = QRSActivity.class.getSimpleName();

    ZXingScannerView zXingScannerView;
    private Room room01;
    private AlertDialog.Builder builder;
    String userid;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrs);

        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();

            }
        }
        userid = this.getSharedPreferences("Timi", Context.MODE_PRIVATE)
                .getString("USERID", "");
    }
    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED); }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QRSActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(zXingScannerView == null) {
                    zXingScannerView = new ZXingScannerView(this);
                    setContentView(zXingScannerView);
                }
                zXingScannerView.setResultHandler(this);
                zXingScannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        zXingScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result rawResult) {
//        String RN = room01.getTitle();
//        Toast.makeText(this, rawResult.getText() , Toast.LENGTH_LONG).show();
        Log.d("QRCodeScanner", rawResult.getBarcodeFormat().toString());
        result = rawResult.getText();
        if(rawResult.getText().indexOf(".")!=-1){
            //拜訪網站
            builder.setNeutralButton("以瀏覽器開啟", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                    startActivity(browserIntent);
                }
            });
            builder.setMessage(rawResult.getText());
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
        else if(rawResult.getText().indexOf("*")!=-1){
            builder = new AlertDialog.Builder(this);
            builder.setMessage(result);
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    zXingScannerView.resumeCameraPreview(QRSActivity.this);
                }
            });
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
        else if(rawResult.getText().length()==19){
            builder = new AlertDialog.Builder(this);
            builder.setMessage("發票號碼:"+result);
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    zXingScannerView.resumeCameraPreview(QRSActivity.this);
                }
            });
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
        //進入房間
        else {
            String RR[] = {"", ""};
            int i = 0;
            String[] separated = result.split(":");
            for (String token : separated) {

                RR[i] = token;

                i++;
            }
            Log.d("rK", RR[0]);
            Log.d("rBID", RR[1]);

            Log.d("QRCodeScanner", rawResult.getBarcodeFormat().toString());

            todailog(RR[0], RR[1]);
        }
    }

    private void todailog(final String RK, final String RBID) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("掃描結果");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                zXingScannerView.resumeCameraPreview(QRSActivity.this);
            }
        });

//            DatabaseReference RF = FirebaseDatabase.getInstance().getReference("users").child(userid).child("rooms").child(rawResult.getText());
//            Log.d("rk", room01.getKey());
            builder.setNeutralButton("瀏覽", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent vr = new Intent(QRSActivity.this, VisitRoomActivity.class);
                vr.putExtra("ROOM_KEY",RK);
                vr.putExtra("ROOM_NAME",room01.getTitle());
                vr.putExtra("ROOM_BUILDERID",RBID);
                startActivity(vr);
            }
        });
        FirebaseDatabase.getInstance().getReference("users").child(RBID).child("rooms").child(RK)
                    .addValueEventListener(this);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        room01 = dataSnapshot.getValue(Room.class);
        String RN = room01.getTitle();
//        Toast.makeText(this, RN , Toast.LENGTH_LONG).show();
        builder.setMessage("您要瀏覽公佈欄 : "+RN+" 嗎?");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}