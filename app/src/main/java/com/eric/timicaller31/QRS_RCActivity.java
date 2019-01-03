package com.eric.timicaller31;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.eric.timicaller31.ObjectClass.Receipt;
import com.eric.timicaller31.ObjectClass.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRS_RCActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, ValueEventListener {
    private static final int REQUEST_CAMERA = 1;
    private static final String TAG = QRS_RCActivity.class.getSimpleName();

    ZXingScannerView zXingScannerView;
    private Room room01;
    private AlertDialog.Builder builder;
    private String edrk;
    private String edrn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrs);
        Intent intent = getIntent();
        edrk = intent.getStringExtra("ROOM_KEY");
        edrn = intent.getStringExtra("ROOM_NUM");
        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();

            }
        }
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
        new android.support.v7.app.AlertDialog.Builder(QRS_RCActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
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
    public void handleResult(final Result rawResult) {
//        String RN = room01.getTitle();
//        Toast.makeText(this, RN , Toast.LENGTH_LONG).show();
        final String result = rawResult.getText();

        Log.d("QRCodeScanner", rawResult.getText());
        Log.d("QRCodeScanner", rawResult.getBarcodeFormat().toString());

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                zXingScannerView.resumeCameraPreview(QRS_RCActivity.this);
            }
        });

        if(rawResult.getText().length()==19){
            //加入發票
            final String rr = rawResult.getText().substring(7,15);
            builder.setNeutralButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DatabaseReference rcRef = FirebaseDatabase.getInstance().getReference("rcrooms").child(edrk)
                            .child("Receipts").push();
                    Receipt receipt = new Receipt();
                    receipt.setNum(rr);
                    receipt.setStar(false);
                    rcRef.setValue(receipt);
                    String key = rcRef.getKey();
                    receipt.setKey(key);
                    rcRef.child("key").setValue(key);

                    Intent toURoom = new Intent(QRS_RCActivity.this, URCRoomActivity.class);
                    toURoom.putExtra("ROOM_KEY",edrk);
                    toURoom.putExtra("ROOM_NUM",edrn);
                    startActivity(toURoom);
                }
            });
            builder.setMessage(rawResult.getText());
            AlertDialog alert1 = builder.create();
            alert1.show();
        }

        //其他
        else {
            builder.setMessage("no");
            AlertDialog alert1 = builder.create();
            alert1.show();
        }


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        room01 = dataSnapshot.getValue(Room.class);
        String RN = room01.getTitle();
//        Toast.makeText(this, RN , Toast.LENGTH_LONG).show();
        builder.setMessage("Do you want to visit room : "+RN+" ?");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}