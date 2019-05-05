package com.example.asus.login_screen;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.SmartLoginException;

public class MainActivity extends AppCompatActivity  {
    ViewPager viewPager;
    Button btn_Signup,btn_Signin;
    LinearLayout pageIndicatorsContainer;
    private int pageCount;
    private ImageView[] dots;
    Integer[] images;
    String[] contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initData();
        bindViews();
        ViewPagerAdapter customAdapter=new ViewPagerAdapter(this,images,contents);
        viewPager.setAdapter(customAdapter);
        pageCount = customAdapter.getCount();
        dots = new ImageView[pageCount];
        //Khởi tạo page indicator
        for(int i = 0; i < pageCount; i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(12, 0, 12, 0);
            pageIndicatorsContainer.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< pageCount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.alpha_anim));
                startActivity(new Intent(MainActivity.this,SignUp_Screen.class));
                overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.alpha_anim));
                startActivity(new Intent(MainActivity.this,SignIn_Screen.class));
                overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
    }
    private void initData(){
        images= new Integer[]{R.drawable.sale_sample, R.drawable.sale_sample, R.drawable.sale_sample};
        contents= new String[]{"Bán hàng tại quầy chuyên nghiệp, tiện lợi\n", "Báo cáo bán hàng minh bạch.\n Cập nhập kịp thời mọi lúc, mọi nơi", "Quản lý tồn kho chính xác\n"};

    }
    private void bindViews() {
        viewPager = findViewById(R.id.viewPager);
        pageIndicatorsContainer = (LinearLayout) findViewById(R.id.pageIndicators);
        btn_Signin=(Button) findViewById(R.id.btn_Signin);
        btn_Signup=(Button) findViewById(R.id.btn_Signup);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}



