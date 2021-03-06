package com.example.asus.login_screen.main.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.login_screen.model.Bill;
import com.example.asus.login_screen.R;
import com.example.asus.login_screen.model.Local_Cache_Store;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter {
    ArrayList<Bill> listBill;
    Context context;

    public OrderAdapter(Context context) {
        this.context=context;
        this.listBill=new ArrayList<Bill>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_order,viewGroup,false);
        return (RecyclerView.ViewHolder) (new OrderViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ((OrderViewHolder)viewHolder).tv_id.setText(listBill.get(i).getId());
        int pos=listBill.get(i).getTime().indexOf("GMT");
        ((OrderViewHolder)viewHolder).tv_time.setText(listBill.get(i).getTime().substring(0,pos));
        ((OrderViewHolder)viewHolder).tv_total.setText( String.format("%1$,.0f", listBill.get(i).getTotal()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,OrderDetail.class);
                Bundle bundle=new Bundle();
                for(Bill tmp:Local_Cache_Store.getListOrders().values()){
                    if(tmp.getId().equals(listBill.get(i).getId())){
                        intent.putExtra("detail", tmp);
                        context.startActivity(intent);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listBill.size();
    }
    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_time, tv_total;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id=itemView.findViewById(R.id.id);
            tv_time=itemView.findViewById(R.id.time);
            tv_total=itemView.findViewById(R.id.total);
        }
    }
    public void setData(ArrayList<Bill> tmp){
        listBill.clear();
        if(tmp!=null){
            listBill.addAll(tmp);
        }
        notifyDataSetChanged();
    }
}
