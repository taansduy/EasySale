package com.example.asus.login_screen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.login_screen.Model.Product;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_hot_product_lv, parent, false);
            viewHolder.tv_Name = (TextView) convertView.findViewById(R.id.tv_Name);
            viewHolder.tv_Rank = (TextView) convertView.findViewById(R.id.tv_Rank);
            viewHolder.tv_Quantity = (TextView) convertView.findViewById(R.id.tv_Quantity);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.tv_Name.setText(product.getName());
        viewHolder.tv_Rank.setText("0"+String.valueOf(position+1));
        viewHolder.tv_Quantity.setText(String.valueOf(product.getQuantity()));

        return convertView;
    }
}
