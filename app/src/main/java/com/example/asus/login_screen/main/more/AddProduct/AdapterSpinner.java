package com.example.asus.login_screen.main.more.AddProduct;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.asus.login_screen.main.more.AddProduct.AddType.AddType;
import com.example.asus.login_screen.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSpinner extends ArrayAdapter<String> {

    private Context context;
    private LayoutInflater mInflater;
    private int layout;
    private ArrayList<String> arr;

    public AdapterSpinner(Context context, int layout, ArrayList<String> arr) {
        super(context, layout, arr);

        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.arr = arr;
        this.arr.add(new String());
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return createItemView(position, convertView, parent);

    }
    public void setData(List<String> tmp){
        arr.clear();
        arr.addAll(tmp);
        notifyDataSetChanged();

    }


    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view;
                if(position!=arr.size()-1)
                {
                    view= mInflater.inflate(layout, parent, false);
                    TextView tv = (TextView) view.findViewById(R.id.tv);
                    tv.setTextColor(Color.BLACK);
                    tv.setText(arr.get(position));
                }
                else
                {
                    view=mInflater.inflate(R.layout.spinner_image,parent,false);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(context, AddType.class);
                            intent.putStringArrayListExtra("listtype",arr);
                            context.startActivity(intent);
                        }
                    });
                }


        return view;
    }
}
