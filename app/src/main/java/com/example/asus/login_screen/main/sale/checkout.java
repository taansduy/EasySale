package com.example.asus.login_screen.main.sale;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.model.Bill;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.model.Store;
import com.example.asus.login_screen.model.daySale;
import com.example.asus.login_screen.model.monthSale;
import com.example.asus.login_screen.model.saleAnalyst;
import com.example.asus.login_screen.model.yearSale;
import com.example.asus.login_screen.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.asus.login_screen.model.Local_Cache_Store.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class checkout extends Fragment {

    private Double total;
    TextView tv_paid,tv_paid2,tv_change,tv_change_label;
    Button btn_checkout;
    EditText edt_cus_charge ;
    LinearLayout change_container;
    public checkout() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public checkout(Double total) {
        this.total = total;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_checkout, container, false);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);
        TextView title= v.findViewById(R.id.title);
        tv_paid= v.findViewById(R.id.tv_totalpaid);
        tv_paid2= v.findViewById(R.id.tv_totalpaid1);
        tv_change= v.findViewById(R.id.tv_change);
        btn_checkout= v.findViewById(R.id.btn_Checkout);
        edt_cus_charge= v.findViewById(R.id.edt_cus_charge);
        change_container= v.findViewById(R.id.chang_cotainer);
        tv_change_label=v.findViewById(R.id.tv_change_label);
        mToolbar.setNavigationIcon(R.drawable.back_arrow_white);
        title.setText("Thanh toán");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { getActivity().onBackPressed();
            }
        });
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        tv_paid.setText(String.format("%1$,.0f",total));
        tv_paid2.setText(String.format("%1$,.0f",total));
        edt_cus_charge.setText(String.format("%1$,.0f",total));
        edt_cus_charge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                String cleanString = s.toString().replaceAll("[$,.]", "");
                if(!cleanString.isEmpty()) {
                    double parsed = Double.parseDouble(cleanString);

                    if (parsed > total) {
                        tv_change.setTextColor(Color.BLACK);
                        tv_change.setText(String.format("%1$,.0f", parsed - total));
                        tv_change_label.setVisibility(View.VISIBLE);
                        change_container.setVisibility(View.VISIBLE);

                        btn_checkout.setEnabled(true);
                    } else if (parsed==total) {
                        tv_change_label.setVisibility(View.VISIBLE);
                        change_container.setVisibility(View.INVISIBLE);
                        btn_checkout.setEnabled(true);
                    }
                    else
                    {
                        btn_checkout.setEnabled(false);
                        tv_change.setTextColor(Color.RED);
                        tv_change.setText("Số tiền vừa nhập thấp hơn tiền khách cần trả");
                        tv_change_label.setVisibility(View.INVISIBLE);
                        change_container.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bill nBill=new Bill();
                List<Product> listforBill=new ArrayList<>();
                for(Product product:Main_Screen.orderedProductList)
                {
                    listforBill.add(product);
                }
                nBill.setProductList(listforBill);
                final Calendar cal = Calendar.getInstance();
                Date newDate = cal.getTime();
                SimpleDateFormat df1 = new SimpleDateFormat("dd");
                final SimpleDateFormat df2 = new SimpleDateFormat("MM");
                final SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
                cal.setTime(newDate);
                String latestId="";
                String id= df3.format(cal.getTime())+df2.format(cal.getTime())+df1.format(cal.getTime());
                for(Map.Entry<String, Bill> entry : Local_Cache_Store.getListOrders().entrySet()) {
                    if(entry.getKey().contains(id) && entry.getKey().compareTo(latestId)>0) latestId=entry.getKey();
                }

                if(latestId.contains(id))
                {
                    String cleanString = latestId.toString().replaceAll("[_]", "");
                    String orderIndex=cleanString.substring(id.length());
                    int realIndex=Integer.parseInt(orderIndex);
                    realIndex++;
                    id+=String.format("%04d",realIndex)+"_";
                }
                else
                {
                   id+="0001_";
                }
                nBill.setId(id);
                nBill.setTotal(total);
                nBill.setTime(cal.getTime().toString());
                DatabaseReference mDatabase;
                /*mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("stores").child(getShopName()).child("listOrders").child(id).setValue(nBill);*/
                for (Product product:Main_Screen.orderedProductList) {
                    Local_Cache_Store.getListOfProductType().get(product.getIdType()).getProductList().get(product.getId()).setQuantity(
                            Local_Cache_Store.getListOfProductType().get(product.getIdType()).getProductList().get(product.getId()).getQuantity()-product.getQuantity()
                    );
                }
                HashMap<String,Bill> temp=Local_Cache_Store.getListOrders();
                temp.put(id,nBill);
                Local_Cache_Store.setListOrders(temp);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Store updateStore=new Store();
                updateStore.setShopName(Local_Cache_Store.getShopName());
                updateStore.setShopAdress(Local_Cache_Store.getShopAdress());
                updateStore.setOwnerDetail(Local_Cache_Store.getOwnerDetail());
                updateStore.setListOfProductType(Local_Cache_Store.getListOfProductType());
                updateStore.setListOrders(Local_Cache_Store.getListOrders());
                mDatabase.child("stores").child(getShopName()).setValue(updateStore);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query query= mDatabase.child("saleAnalyst").child(getShopName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            saleAnalyst temp=dataSnapshot.getValue(saleAnalyst.class);
                            if(!temp.getYearSales().containsKey(df3.format(cal.getTime())+"_"))
                            {
                                HashMap<String,daySale> data=new HashMap<>();
                                data.put(cal.get(Calendar.DAY_OF_MONTH)+"_",new daySale(total));
                                HashMap<String,monthSale> mdata=new HashMap<>();
                                mdata.put(df2.format(cal.getTime())+"_",new monthSale(total,data));
                                temp.getYearSales().put(df3.format(cal.getTime())+"_",new yearSale(total,mdata));
                            }
                            else {
                                temp.getYearSales().get(df3.format(cal.getTime()) + "_").setTotalSale(temp.getYearSales().get(df3.format(cal.getTime()) + "_").getTotalSale() + total);

                                if (temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().containsKey(df2.format(cal.getTime())+"_")) {
                                    temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().get(df2.format(cal.getTime()) + "_").setTotalSale(
                                            temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().get(df2.format(cal.getTime()) + "_").getTotalSale() + total);
                                    if (temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().get(df2.format(cal.getTime()) + "_").getListOfDSale().containsKey(cal.get(Calendar.DAY_OF_MONTH) + "_")) {
                                        temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().get(df2.format(cal.getTime()) + "_").getListOfDSale().get(cal.get(Calendar.DAY_OF_MONTH) + "_").setTotalSale(
                                                temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().get(df2.format(cal.getTime()) + "_").getListOfDSale().get(cal.get(Calendar.DAY_OF_MONTH) + "_").getTotalSale() + total);
                                    } else {
                                        temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().get(df2.format(cal.getTime()) + "_").getListOfDSale().put(cal.get(Calendar.DAY_OF_MONTH) + "_", new daySale(total));
                                    }
                                } else {
                                    HashMap<String, daySale> data = new HashMap<>();
                                    data.put(cal.get(Calendar.DAY_OF_MONTH) + "_", new daySale(total));
                                    temp.getYearSales().get(df3.format(cal.getTime()) + "_").getListOfMSale().put(df2.format(cal.getTime()) + "_", new monthSale(total, data));
                                }
                            }
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("saleAnalyst").child(getShopName()).setValue(temp);

                        }
                        else
                        {
                            HashMap<String,daySale> data=new HashMap<>();
                            data.put(cal.get(Calendar.DAY_OF_MONTH)+"_",new daySale(total));
                            HashMap<String,monthSale> mdata=new HashMap<>();
                            mdata.put(df2.format(cal.getTime())+"_",new monthSale(total,data));
                            HashMap<String,yearSale> ydata=new HashMap<>();
                            ydata.put(df3.format(cal.getTime())+"_",new yearSale(total,mdata));
                            saleAnalyst rdata=new saleAnalyst(ydata);
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("saleAnalyst").child(getShopName()).setValue(rdata);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Main_Screen.orderedProductList.clear();
                Main_Screen.total="0";
                ((Main_Screen) getActivity()).onBackPressed();
            }
        });
        return v;
    }


}
