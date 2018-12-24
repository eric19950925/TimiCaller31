package com.eric.timicaller31;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private EditText eduserid;
    private EditText edpasswd;
    private String TAG= LoginActivity.class.getSimpleName();
    private CheckBox cbkeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eduserid = (EditText)findViewById(R.id.et_userid);
        edpasswd = (EditText)findViewById(R.id.et_passwd);
        cbkeep = findViewById(R.id.cb_keep);
        cbkeep.setChecked(getSharedPreferences("Timi",MODE_PRIVATE)
        .getBoolean("KEEP",false));
        cbkeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton c, boolean b) {
                getSharedPreferences("Timi",MODE_PRIVATE)
                        .edit()
                        .putBoolean("KEEP",b)
                        .apply();
            }
        });
        String userid=getSharedPreferences("Timi",MODE_PRIVATE)
                .getString("USERID","");
        eduserid.setText(userid);
        String userpw=getSharedPreferences("Timi",MODE_PRIVATE)
                .getString("USERPW","");
        edpasswd.setText(userpw);
    }
    public void login(View view){
        final String userid = eduserid.getText().toString();
        final String userpasswd = edpasswd.getText().toString();
        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String pw = (String) dataSnapshot.getValue();
                        if (pw.equals(userpasswd)) {
                            Boolean keep = getSharedPreferences("Timi",MODE_PRIVATE)
                                    .getBoolean("KEEP",false);
                            if(keep){
                                getSharedPreferences("Timi",MODE_PRIVATE)
                                        .edit()
                                        .putString("USERID",userid)
                                        .putString("USERPW",userpasswd)
                                        .apply();
                            }

                            setResult(RESULT_OK);
//                            finish();
                            gotoMain();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Login result")
                                    .setMessage("Login failed")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

    }
    public void exit(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void gotoMain(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    }