package com.example.asus.login_screen.main.more.AddProduct.AddType;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.model.TypeOfProduct;
import com.example.asus.login_screen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
                if(edt_Name.getText().toString().equals("")){
                    Toast.makeText(AddType.this,"Loại sản phẩm không hợp lệ",Toast.LENGTH_SHORT).show();
                }
                else if (listtype==null) {
                    final DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+ Local_Cache_Store.shopName);
                    String id= mDatabase.child("listOfProductType").push().getKey();
                    assert id != null;
                    mDatabase.child("listOfProductType").child(id).setValue(new TypeOfProduct(id,edt_Name.getText().toString(),null));
                    onBackPressed();
                }
                else if(!listtype.contains(edt_Name.getText().toString())) {
                    final DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+ Local_Cache_Store.shopName);
                    String id= mDatabase.child("listOfProductType").push().getKey();
                    assert id != null;
                    mDatabase.child("listOfProductType").child(id).setValue(new TypeOfProduct(id,edt_Name.getText().toString(),null));
                    onBackPressed();
                }

                else if (listtype.contains(edt_Name.getText().toString())){
                    Toast.makeText(AddType.this,"Loại sản phẩm đã tồn tại",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
