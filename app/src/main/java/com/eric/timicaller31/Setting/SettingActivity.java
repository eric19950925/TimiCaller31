package com.eric.timicaller31.Setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eric.timicaller31.BoNaViewHelper;
import com.eric.timicaller31.LoginActivity;
import com.eric.timicaller31.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class SettingActivity extends AppCompatActivity {
private static final int ACTIVITY_NUM = 0;
private Context mContext = SettingActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setBoNaView();
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }


        });
    }
    public void logout() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);//c
    }
    private void setBoNaView(){

        BottomNavigationViewEx b = (BottomNavigationViewEx)findViewById(R.id.bar);
        BoNaViewHelper.setBoNaView(b);
        BoNaViewHelper.enableNavigation(mContext,b);
        Menu menu = b.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
