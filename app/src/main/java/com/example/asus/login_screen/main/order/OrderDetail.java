package com.example.asus.login_screen.main.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.login_screen.R;
import com.example.asus.login_screen.model.Bill;
import com.example.asus.login_screen.model.Product;

import java.util.ArrayList;

public class OrderDetail extends AppCompatActivity {
    Bill bill;
    RecyclerView recyclerView;
    OrderDetailAdapter adapter;
    TextView tv_id,tv_total,tv_date;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        img_back=findViewById(R.id.back);
        tv_id=findViewById(R.id.id);
        tv_date=findViewById(R.id.date);
        tv_total=findViewById(R.id.total);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bill= (Bill) getIntent().getSerializableExtra("detail");
        tv_id.setText(bill.getId());
        tv_total.setText(String.format("%1$,.0f", bill.getTotal()));
        tv_date.setText(bill.getTime());
        recyclerView=findViewById(R.id.recyclerView);
        adapter=new OrderDetailAdapter(OrderDetail.this, (ArrayList<Product>) bill.getProductList());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OrderDetail.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
