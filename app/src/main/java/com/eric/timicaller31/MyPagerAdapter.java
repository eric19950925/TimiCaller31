package com.eric.timicaller31;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter{
    private final List<Fragment> mList = new ArrayList<>();
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    public void addFragment(Fragment fragment){
        mList.add(fragment);
    }
}
