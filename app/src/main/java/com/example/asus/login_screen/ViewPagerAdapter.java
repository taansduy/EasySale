package com.example.asus.login_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images;
    private String[] contents;
    public ViewPagerAdapter(Context context,Integer[] images,String[] contents){
        this.context=context;
        this.images=images;
        this.contents=contents;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.child_view_flipper_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_sample);
        TextView Description=(TextView) view.findViewById(R.id.edt_content);
        ImageView logoView=(ImageView) view.findViewById(R.id.img_Logo);
        if(position==0) logoView.setImageResource(R.drawable.logo);
        else logoView.setImageResource(0);
        imageView.setImageResource(images[position]);
        Description.setText(contents[position]);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
