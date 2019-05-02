package com.example.asus.login_screen.Activity_of_MoreComponent;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.Model.Store;
import com.example.asus.login_screen.Model.User;
import com.example.asus.login_screen.R;
import com.example.asus.login_screen.SignIn_Screen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Account extends AppCompatActivity {
    EditText edt_Name,edt_Email, edt_PhoneNumber,edt_Address,edt_Date;
    Toolbar toolbar;
    ImageView img_Back,img_Yes;
    TextView tv_title;

    Bundle bundle=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        //get bundle
        bundle=getIntent().getBundleExtra("bundle");
        //Anh xa
        edt_Name=findViewById(R.id.Name);
        edt_Name.setText(((User)bundle.getSerializable("user")).getmName());
        edt_Email=findViewById(R.id.Email);
        edt_Email.setText(((User)bundle.getSerializable("user")).getEmail());
        edt_PhoneNumber=findViewById(R.id.PhoneNumber);
        edt_PhoneNumber.setText(((User)bundle.getSerializable("user")).getPhoneNumber());
        edt_Address=findViewById(R.id.Address);
        edt_Address.setText(bundle.getString("address"));
        edt_Date=findViewById(R.id.Date);
        tv_title=findViewById(R.id.title);
        tv_title.setText(bundle.getString("shopName"));

        img_Back=findViewById(R.id.Back);
        img_Yes=findViewById(R.id.Yes);

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
                                // save a changed info in database
                                final DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
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
                            }
                        })
                        .show();


            }
        });
        //****************************************************************************//

        edt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                final int day=calendar.get(Calendar.DATE);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog=new DatePickerDialog(Account.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        edt_Date.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
                img_Yes.setVisibility(View.VISIBLE);
            }
        });
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
}
