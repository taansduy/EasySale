package com.example.asus.login_screen.authentication;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.asus.login_screen.MainActivity;
import com.example.asus.login_screen.R;
import com.facebook.FacebookSdk;

public class SignUp_Screen extends AppCompatActivity implements FragmentChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__screen);
        String value = getIntent().getStringExtra("Screen");

        FacebookSdk.sdkInitialize(this);
        setupToolBar();
        if(value==null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag_placeholder, new SignUp_Step1());
            ft.commit();
        }else{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            String email=getIntent().getExtras().getString("email");
            bundle.putString("username",email); // Put anything what you want
            SignUp_Step2 fr = new SignUp_Step2();
            fr.setArguments(bundle);
            ft.replace(R.id.frag_placeholder, fr);
            ft.commit();
        }

    }
    //Re-custom ToolBar
    private void setupToolBar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp_Screen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (android.support.v4.app.Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_placeholder, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
