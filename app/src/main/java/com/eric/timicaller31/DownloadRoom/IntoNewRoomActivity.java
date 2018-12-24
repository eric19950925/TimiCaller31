package com.eric.timicaller31.DownloadRoom;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eric.timicaller31.DownloadActivity;
import com.eric.timicaller31.R;

public class IntoNewRoomActivity extends AppCompatActivity {

    private EditText etname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_droom);
        etname = findViewById(R.id.et_name);
    }
    public void save(View view) {

        String name = etname.getText().toString();


        DRoomHelper helper = new DRoomHelper(this);
        ContentValues values = new ContentValues();
        values.put("COL_NAME", name);

        String uri = "20181206";
        values.put("COL_URI", uri);
        long id = helper.getWritableDatabase().insert("DROOM", null, values);


        if (id > -1) {
            Toast.makeText(this, "Insert Successful", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Insert Failure", Toast.LENGTH_LONG).show();

        Intent tomain = new Intent(this, DownloadActivity.class);
        startActivity(tomain);

    }
}
