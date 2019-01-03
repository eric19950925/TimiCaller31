package com.eric.timicaller31;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.eric.timicaller31.Info.InfoActivity;
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
                    case R.id.ic_start:
                        Intent intent1 = new Intent(c,DailyEventsActivity.class);
                        c.startActivity(intent1);
                        break;
                    case R.id.ic_weekly_plan:
                        Intent intent2 = new Intent(c,WeeklyPlanActivity.class);
                        c.startActivity(intent2);
                        break;
                    case R.id.ic_favorite:
                        Intent intent3 = new Intent(c,FavoriteRoomActivity.class);
                        c.startActivity(intent3);
                        break;
                    case R.id.ic_myroom:
                        Intent intent4 = new Intent(c,BuildMyRoomActivity.class);
                        c.startActivity(intent4);
                        break;
                    case R.id.ic_ad:
                        Intent intent5 = new Intent(c,ADRoomsActivity.class);
                        c.startActivity(intent5);
                        break;
                }
                return false;
            }
        });
    }
}
