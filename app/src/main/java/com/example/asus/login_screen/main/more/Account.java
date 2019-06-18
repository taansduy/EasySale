package com.example.asus.login_screen.main.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.model.User;
import com.example.asus.login_screen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Account extends AppCompatActivity {
    EditText edt_Name,edt_Email, edt_PhoneNumber,edt_Address;
    TextInputLayout phoneWrapper;
    Toolbar toolbar;
    ImageView img_Back,img_Yes;
    TextView tv_title;
    LinearLayout ln_Changepass;

    Bundle bundle=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
//        //get bundle
//        bundle=getIntent().getBundleExtra("bundle");
        //Anh xa
        phoneWrapper=findViewById(R.id.phoneWrapper);
        edt_Name=findViewById(R.id.Name);
        edt_Name.setText((Local_Cache_Store.getOwnerDetail().getmName()));
        edt_Email=findViewById(R.id.Email);
        edt_Email.setText(Local_Cache_Store.getOwnerDetail().getEmail());
        edt_PhoneNumber=findViewById(R.id.PhoneNumber);
        edt_PhoneNumber.setText(Local_Cache_Store.getOwnerDetail().getPhoneNumber());
        edt_Address=findViewById(R.id.Address);
        edt_Address.setText(Local_Cache_Store.shopAdress);
        tv_title=findViewById(R.id.title);
        tv_title.setText(Local_Cache_Store.getShopName());

        img_Back=findViewById(R.id.Back);
        img_Yes=findViewById(R.id.Yes);
        ln_Changepass=findViewById(R.id.changepass);

        ln_Changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Account.this, PassWord.class);
                startActivity(intent);
            }
        });

        toolbar=findViewById(R.id.toolbar);
        //*****************set Onlick Yes/Cancel on toolbar***************************//
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(img_Yes.getVisibility()!=View.VISIBLE)
                {
                    onBackPressed();
                }
                else
                {
                    new AlertDialog.Builder(Account.this, R.style.MyAlertDialogTheme)
                            .setTitle("Thông báo")
                            .setMessage("Bạn có muốn hủy bỏ ?")
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // save a changed info in database
                                    img_Yes.setVisibility(View.INVISIBLE);

                                }
                            })
                            .show();
                }
            }
        });
        img_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Account.this, R.style.MyAlertDialogTheme)
                        .setTitle("Thông báo")
                        .setMessage("Bạn có muốn cập nhật thông tin ?")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(checkValidPhoneNumber(edt_PhoneNumber.getText().toString())){
                                User tmp= new User(edt_Email.getText().toString(),edt_Name.getText().toString(),edt_PhoneNumber.getText().toString());
                                // save a changed info in database
                                final DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("stores").child(Local_Cache_Store.getShopName()).child("ownerDetail").child("email").setValue(edt_Email.getText().toString());
                                mDatabase.child("stores").child(Local_Cache_Store.getShopName()).child("ownerDetail").child("mName").setValue(edt_Name.getText().toString());
                                mDatabase.child("stores").child(Local_Cache_Store.getShopName()).child("ownerDetail").child("phoneNumber").setValue(edt_PhoneNumber.getText().toString());
                                mDatabase.child("stores").child(Local_Cache_Store.getShopName()).child("shopAdress").setValue(edt_Address.getText().toString());
                                //cap nhat local
                                Local_Cache_Store.setOwnerDetail(new User(edt_Email.getText().toString(),edt_Name.getText().toString(),edt_PhoneNumber.getText().toString()));
                                Local_Cache_Store.shopAdress=edt_Address.getText().toString();
                                bundle.clear();
                                User user=new User(edt_Email.getText().toString(),edt_Name.getText().toString(),edt_PhoneNumber.getText().toString());
                                bundle.putSerializable("user",user);
                                bundle.putString("address",edt_Address.getText().toString());
                                bundle.putString("shopName",tv_title.getText().toString());
                                Intent intent = new Intent(Account.this,Main_Screen.class);
                                intent.putExtra("bundle",bundle);
                                setResult(RESULT_OK, intent);
                                finish();


                                img_Yes.setVisibility(View.INVISIBLE);
                                } else{
                                    phoneWrapper.setErrorTextAppearance(R.style.error_appearance);
                                    phoneWrapper.setError("Vui lòng nhập đúng SĐT");

                                }

                            }
                        })
                        .show();


            }
        });
        //****************************************************************************//
        edt_Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                img_Yes.setVisibility(View.VISIBLE);
            }
        });
        edt_PhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                img_Yes.setVisibility(View.VISIBLE);
            }
        });
        edt_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                img_Yes.setVisibility(View.VISIBLE);
            }
        });
        edt_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                img_Yes.setVisibility(View.VISIBLE);
            }
        });


    }
    boolean checkValidPhoneNumber(String phone){
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}
