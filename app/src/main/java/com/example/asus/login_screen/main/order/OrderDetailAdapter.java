package com.example.asus.login_screen.main.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.login_screen.R;
import com.example.asus.login_screen.model.Bill;
import com.example.asus.login_screen.model.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Product> listItem;

    public OrderDetailAdapter(Context context, ArrayList<Product> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_detail_order,viewGroup,false);
        return (RecyclerView.ViewHolder) (new DetailViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((DetailViewHolder)viewHolder).tv_name.setText(listItem.get(i).getName());
        ((DetailViewHolder)viewHolder).tv_price.setText(String.format("%,d", listItem.get(i).getSalePrice()));
        ((DetailViewHolder)viewHolder).tv_quantity.setText(String.valueOf(listItem.get(i).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
    class DetailViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_price,tv_quantity;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.name);
            tv_quantity=itemView.findViewById(R.id.quantity);
            tv_price=itemView.findViewById(R.id.price);
        }
    }
}
