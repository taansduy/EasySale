package com.example.asus.login_screen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.example.asus.login_screen.Fragment_of_MainScreen.PagerAdapter_MainScreen;
import com.example.asus.login_screen.Model.Store;
import com.example.asus.login_screen.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Main_Screen extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    public Bundle bundle=new Bundle();
    public User user;
    public String shopName;
    public String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        //***************get data from bundle**************************//
        bundle=getIntent().getBundleExtra("bundle");
        user= (User) bundle.getSerializable("user");
        shopName=bundle.getString("shopName");
        address=bundle.getString("address");
        //*************************************************************//
        viewPager=(ViewPager) findViewById(R.id.vp);
        tabLayout=(TabLayout) findViewById(R.id.tb);

        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter_MainScreen adapter = new PagerAdapter_MainScreen(manager,Main_Screen.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.sell);
        tabLayout.getTabAt(2).setIcon(R.drawable.order);
        tabLayout.getTabAt(3).setIcon(R.drawable.more);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#123123"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

}
