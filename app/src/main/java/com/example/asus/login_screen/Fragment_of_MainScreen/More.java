package com.example.asus.login_screen.Fragment_of_MainScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.asus.login_screen.Activity_of_MoreComponent.Account;
import com.example.asus.login_screen.Activity_of_MoreComponent.AddProduct.AddProduct;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.Model.User;
import com.example.asus.login_screen.R;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class More extends android.support.v4.app.Fragment {
    Main_Screen main_screen;
    LinearLayout ln_ContentMore,ln_ListProduct;
    Button button;
    TextView title,tv_Name,tv_Email;
    Toolbar toolbar;
    LinearLayout lnAccount,lnProduct,lnCustomer,lnLogout;
    ImageView img_Back,img_Add,img_Search;
    public More(Context context) {
        // Required empty public constructor
        main_screen=(Main_Screen)context;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_more, container, false);
        title=view.findViewById(R.id.shopName);
        title.setText(main_screen.shopName);
        tv_Name=view.findViewById(R.id.Name);
        tv_Name.setText(main_screen.user.getmName());
        tv_Email=view.findViewById(R.id.Email);
        tv_Email.setText(main_screen.user.getEmail());
        button=view.findViewById(R.id.info);
        lnAccount=view.findViewById(R.id.account);
        lnProduct=view.findViewById(R.id.product);
        lnCustomer=view.findViewById(R.id.customer);
        lnLogout=(LinearLayout)view.findViewById(R.id.logout);
        ln_ContentMore=(LinearLayout)view .findViewById(R.id.lnContentMore) ;
        ln_ListProduct=(LinearLayout)view.findViewById(R.id.lnListproduct);
        img_Back=view.findViewById(R.id.Back);
        img_Add=view.findViewById(R.id.Add);
        img_Search=view.findViewById(R.id.Search);

        //*****************Set onClick*********************//
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_ListProduct.setVisibility(View.INVISIBLE);
                ln_ContentMore.setVisibility(View.VISIBLE);
            }
        });
        img_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddProduct.class);
                intent.putExtra("bundle",main_screen.bundle);
                startActivity(intent);
            }
        });
        //*************************************************//


        // set onclick for Linear views
        lnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Account.class);
                intent.putExtra("bundle",main_screen.bundle);
                startActivityForResult(intent,123);

            }
        });
        lnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_ListProduct.setVisibility(View.VISIBLE);
                ln_ContentMore.setVisibility(View.INVISIBLE);

            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (resultCode == RESULT_OK&&requestCode==123) { // Activity.RESULT_OK
            main_screen.bundle=data.getBundleExtra("bundle");
            main_screen.user= (User) main_screen.bundle.getSerializable("user");
            main_screen.shopName=main_screen.bundle.getString("shopName");
            main_screen.address=main_screen.bundle.getString("address");
            tv_Name.setText(main_screen.user.getmName());
            tv_Email.setText(main_screen.user.getEmail());

        }

    }


}
