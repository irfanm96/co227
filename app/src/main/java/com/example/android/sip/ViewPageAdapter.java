package com.example.android.sip;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }


    private final List<Fragment> fragmentList=new ArrayList<>();
    private final List<String> stringList=new ArrayList<>();


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }


    public void addFragment(Fragment fragment , String title){
        fragmentList.add(fragment);
        stringList.add(title);
    }
}
