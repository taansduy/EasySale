package com.example.asus.login_screen.main.overview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.asus.login_screen.R;
import com.example.asus.login_screen.model.Product;

import java.util.ArrayList;

public class HotProduct_Adapter extends ArrayAdapter<Product> implements View.OnClickListener {
    private ArrayList<Product> dataSet;
    Context mContext;

    public HotProduct_Adapter(ArrayList<Product> data, Context context) {
        super(context, R.layout.row_item_hot_product_lv, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private static class ViewHolder {
        TextView tv_Rank;
        TextView tv_Name;
        TextView tv_Quantity;

    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
//<<<<<<< HEAD
            convertView = inflater.inflate(R.layout.row_item_hot_product_lv, parent, false);
            viewHolder.tv_Name = convertView.findViewById(R.id.tv_Name);
            viewHolder.tv_Rank = convertView.findViewById(R.id.tv_Rank);
            viewHolder.tv_Quantity = convertView.findViewById(R.id.tv_Quantity);
//=======
//            convertView = inflater.inflate(R.layout.row_item_hot_product_lv, parent,false);
//            viewHolder.tv_Name = convertView.findViewById(R.id.tv_Name);
//            viewHolder.tv_Rank = (TextView) convertView.findViewById(R.id.tv_Rank);
//            viewHolder.tv_Quantity = (TextView) convertView.findViewById(R.id.tv_Quantity);
//
//            result=convertView;
//>>>>>>> 75f2258ecb1714e6c14cc7904f7a8889820e2b9d

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        assert product != null;
        viewHolder.tv_Name.setText(product.getName());
        viewHolder.tv_Rank.setText("0"+ (position + 1));
        viewHolder.tv_Quantity.setText(String.valueOf(product.getQuantity()));

        return convertView;
    }
}
