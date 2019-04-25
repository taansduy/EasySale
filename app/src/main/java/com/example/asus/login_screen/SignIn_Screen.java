package com.example.asus.login_screen;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.florent37.materialtextfield.MaterialTextField;

public class SignIn_Screen extends AppCompatActivity {
    EditText edt_email,edt_password;
    MaterialTextField email_field,password_field;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in__screen);
        setupToolBar();
        email_field=(MaterialTextField) findViewById(R.id.email);
        edt_email=(EditText) findViewById(R.id.edt_email);
        password_field=(MaterialTextField) findViewById(R.id.password);
        edt_password=(EditText) findViewById(R.id.edt_password);
        edt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    email_field.setHasFocus(false);
                    hideKeyboard(v);
                }
            }
        });
        email_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //email_field.toggle();
                password_field.reduce();
                if(email_field.isExpanded())
                {
                    email_field.reduce();
                    hideKeyboard(v);
                }
                else
                {
                    email_field.setHasFocus(true);
                }
            }
        });
        edt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    password_field.setHasFocus(false);
                    hideKeyboard(v);
                }
            }
        });
        password_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //email_field.toggle();
                email_field.reduce();
                if(password_field.isExpanded())
                {
                    password_field.reduce();
                    hideKeyboard(v);
                }
                else
                {
                    password_field.setHasFocus(true);
                }
            }
        });

    }

    private void setupToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
