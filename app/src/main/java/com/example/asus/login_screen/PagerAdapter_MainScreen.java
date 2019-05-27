package com.example.asus.login_screen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.asus.login_screen.main.more.More;
import com.example.asus.login_screen.main.order.Order;
import com.example.asus.login_screen.main.overview.Overview;
import com.example.asus.login_screen.main.sale.Sale;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter_MainScreen extends FragmentStatePagerAdapter {
    public PagerAdapter_MainScreen(FragmentManager fm) {
        super(fm);
    }
    public Context context;
    private List<Fragment> tabs = new ArrayList<>();

    public PagerAdapter_MainScreen(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabs.add(HostFragment.newInstance(new Overview()));
        tabs.add(HostFragment.newInstance(new Sale()));
        tabs.add(HostFragment.newInstance(new Order()));
        tabs.add(HostFragment.newInstance(new More(context)));
    }

    @Override
    public Fragment getItem(int i) {
        return tabs.get(i);
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
