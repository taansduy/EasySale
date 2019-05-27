package com.example.asus.login_screen;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.asus.login_screen.main.sale.checkout;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.model.User;

import java.util.ArrayList;
import java.util.List;

public class Main_Screen extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    public Bundle bundle=new Bundle();
    public User user;
    public String shopName;
    public String address;
    static public List<Product> orderedProductList=new ArrayList<>();
    static public String total="0";
    PagerAdapter_MainScreen adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        /*android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();*/
        //***************get data from bundle**************************//
        bundle=getIntent().getBundleExtra("bundle");
        /*DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query ref=mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(Local_Cache_Store.getOwnerDetail().getEmail());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Store store = dataSnapshot.getValue(Store.class);
                    Local_Cache_Store.setListOfProductType(store.getListOfProductType());
                    Local_Cache_Store.setListOrders(store.getListOrders());
                    Local_Cache_Store.setOwnerDetail(store.getOwnerDetail());
                    Local_Cache_Store.setShopAdress(store.getShopAdress());
                    Local_Cache_Store.setShopName(store.getShopName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

        if(bundle!=null) {
            user = (User) bundle.getSerializable("user");
            shopName = bundle.getString("shopName");
            address = bundle.getString("address");
        }

        //*************************************************************//
        viewPager=(ViewPager) findViewById(R.id.vp);
        tabLayout=(TabLayout) findViewById(R.id.tb);

        FragmentManager manager = getSupportFragmentManager();
        adapter = new PagerAdapter_MainScreen(manager,Main_Screen.this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
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

    @Override
    public void onBackPressed()
    {
        if(!BackStackFragment.handleBackPressed(getSupportFragmentManager())){
            super.onBackPressed();
        }
    }

    public void openNewContentFragment(Double total) {
        HostFragment hostFragment = (HostFragment) adapter.getItem(viewPager.getCurrentItem());
        hostFragment.replaceFragment(new checkout(total), true);
    }
}
