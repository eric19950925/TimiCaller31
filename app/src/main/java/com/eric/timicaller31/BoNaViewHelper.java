package com.eric.timicaller31;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.eric.timicaller31.Setting.SettingActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BoNaViewHelper {
    public static final String TAG=BoNaViewHelper.class.getSimpleName();

    public static void setBoNaView(BottomNavigationViewEx b) {

        b.enableAnimation(false);
        //b.enableItemShiftingMode(false);
        //b.enableShiftingMode(false);
        //b.setTextVisibility(false);
    }

    public static void enableNavigation(final Context c, BottomNavigationViewEx b) {
        b.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_set:
                        Intent intent1 = new Intent(c,SettingActivity.class);
                        c.startActivity(intent1);
                        break;
                    case R.id.ic_event:
                        Intent intent2 = new Intent(c,MainActivity.class);
                        c.startActivity(intent2);
                        break;
                    case R.id.ic_cloud:
                        Intent intent3 = new Intent(c,DownloadActivity.class);
                        c.startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
}
