package com.eric.timicaller31;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.eric.timicaller31.DownloadRoom.IntoRoomFragment;
import com.eric.timicaller31.UploadRoom.MyRoomFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class DownloadActivity extends AppCompatActivity {
private static final int ACTIVITY_NUM = 2;
private Context mContext = DownloadActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBoNaView();
        setMyPager();
    }
    private void setBoNaView(){

        BottomNavigationViewEx b = (BottomNavigationViewEx)findViewById(R.id.bar);
        BoNaViewHelper.setBoNaView(b);
        BoNaViewHelper.enableNavigation(mContext,b);
        Menu menu = b.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void setMyPager(){
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IntoRoomFragment());
        adapter.addFragment(new MyRoomFragment());

        ViewPager viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icons8_scroll_down);
        tabLayout.getTabAt(1).setIcon(R.drawable.icons8_scroll_up);

    }
}
