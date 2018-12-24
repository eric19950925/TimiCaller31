package com.eric.timicaller31.DownloadRoom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.eric.timicaller31.R;

public class DRoomActivity extends AppCompatActivity {
    String edname,eduri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droom);
        Intent intent = getIntent();
        edname = intent.getStringExtra("NAME");
        eduri = intent.getStringExtra("URI");
        ((TextView) findViewById(R.id.name)).setText(edname);
        ((TextView) findViewById(R.id.uri)).setText(eduri);
    }
}
