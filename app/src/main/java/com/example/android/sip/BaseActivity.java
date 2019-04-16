package com.example.android.sip;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

public class BaseActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        tabLayout=findViewById(R.id.tb_layout);
        viewPager=findViewById(R.id.view_pager);
        adapter=new ViewPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new CallFragment(),"");
        adapter.addFragment(new ContactFragment(),"");
        adapter.addFragment(new SettingsFragment(),"");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contact);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setElevation(0);

    }
}
