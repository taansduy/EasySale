package com.example.asus.login_screen.Activity_of_MoreComponent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.asus.login_screen.Activity_of_MoreComponent.AddProduct.AddProduct;
import com.example.asus.login_screen.R;

public class ListProduct extends AppCompatActivity {
    ImageView img_Back,img_Add,img_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        //*****************anh xa**************************//
        img_Back=findViewById(R.id.Back);
        img_Add=findViewById(R.id.Add);
        img_Search=findViewById(R.id.Search);
        //*************************************************//
        //*****************Set onClick*********************//
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListProduct.this, AddProduct.class);
                startActivity(intent);
            }
        });
        //*************************************************//
    }

}
