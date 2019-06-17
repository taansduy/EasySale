package com.example.asus.login_screen.main.order;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.model.Bill;
import com.example.asus.login_screen.model.Store;
import com.example.asus.login_screen.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class Order extends Fragment {
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Bill> tmp;
    public Order() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);
        recyclerView=view.findViewById(R.id.orderList);
        swipeRefreshLayout=view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        orderAdapter=new OrderAdapter(this.getContext());
        tmp= new ArrayList<Bill>((Collection<? extends Bill>) Local_Cache_Store.getListOrders().values());
        Collections.sort(tmp, new Comparator<Bill>() {
            @Override
            public int compare(Bill o1, Bill o2) {
                String f = new String();
                return (o2.getId()).compareTo((o1.getId()));
            }
        });
        orderAdapter.setData(tmp);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(orderAdapter);

        return view;
    }
    void fetchData(){
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(Local_Cache_Store.getShopName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                Local_Cache_Store.setListOrders(store.getListOrders());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tmp= new ArrayList<Bill>((Collection<? extends Bill>) Local_Cache_Store.getListOrders().values());
        Collections.sort(tmp, new Comparator<Bill>() {
            @Override
            public int compare(Bill o1, Bill o2) {
                String f = new String();
                return (o2.getId()).compareTo((o1.getId()));
            }
        });

        orderAdapter.setData(new ArrayList<Bill>(tmp));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
