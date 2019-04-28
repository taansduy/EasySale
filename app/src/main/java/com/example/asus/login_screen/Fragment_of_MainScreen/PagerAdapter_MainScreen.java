package com.example.asus.login_screen.Fragment_of_MainScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter_MainScreen extends FragmentStatePagerAdapter {
    public PagerAdapter_MainScreen(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment frag=null;
        switch (i){
            case 0:
                frag = new Overview();
                break;
            case 1:
                frag = new Sale();
                break;
            case 2:
                frag = new Order();
                break;
            case 3:
                frag=new More();
        }
        return frag;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Tổng quan";
                break;
            case 1:
                title = "Bán hàng";
                break;
            case 2:
                title = "Đơn hàng";
                break;
            case 3:
                title = "Thêm";
                break;
        }
        return title;
    }
    @Override
    public int getCount() {
        return 4;
    }
}
