package com.example.asus.login_screen.Activity_of_MoreComponent.AddProduct.AddType;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.login_screen.Model.TypeOfProduct;
import com.example.asus.login_screen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AddType extends AppCompatActivity {

    ImageView img_Back,img_Ok;
    ArrayList<String> listtype=new ArrayList<String>();
    EditText edt_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        listtype=getIntent().getExtras().getStringArrayList("listtype");
        img_Back=findViewById(R.id.Back);
        img_Ok=findViewById(R.id.Ok);
        edt_Name=findViewById(R.id.name);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Kiem tra co ton tai r` post
                if(listtype.contains(edt_Name.getText().toString())==false)
                {
                    final DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference("stores/Cong duy");
                    TypeOfProduct typeOfProduct=new TypeOfProduct(1,edt_Name.getText().toString(),null);
                    mDatabase.child("listOfProductType").push().setValue(typeOfProduct);
                }
                onBackPressed();

            }
        });
    }
}
