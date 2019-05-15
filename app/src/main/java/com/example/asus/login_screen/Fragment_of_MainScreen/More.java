package com.example.asus.login_screen.Fragment_of_MainScreen;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.asus.login_screen.Activity_of_MoreComponent.Account;
import com.example.asus.login_screen.Activity_of_MoreComponent.AddProduct.AddProduct;
import com.example.asus.login_screen.Local_Cache_Store;
import com.example.asus.login_screen.MainActivity;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.Model.User;
import com.example.asus.login_screen.R;
import com.example.asus.login_screen.SignIn_Screen;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ACTIVITY_SERVICE;

@SuppressLint("ValidFragment")
public class More extends android.support.v4.app.Fragment {
    Main_Screen main_screen;
    LinearLayout ln_ContentMore,ln_ListProduct;
    Button button;
    TextView title,tv_Name,tv_Email,tv_title;
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
        title.setText(Local_Cache_Store.shopName);
        tv_title=view.findViewById(R.id.title);
        tv_title.setText(Local_Cache_Store.shopName);
        tv_Name=view.findViewById(R.id.Name);
        tv_Name.setText(Local_Cache_Store.getOwnerDetail().getmName());
        tv_Email=view.findViewById(R.id.Email);
        tv_Email.setText(Local_Cache_Store.getOwnerDetail().getEmail());
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
                startActivity(intent);
            }
        });
        //*************************************************//


        // set onclick for Linear views
        lnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Account.class);
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
        lnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                FirebaseAuth.getInstance().signOut();
                ActivityManager mngr = (ActivityManager) getActivity().getSystemService( ACTIVITY_SERVICE );

                List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

                if(taskList.get(0).numActivities == 1 &&
                        taskList.get(0).topActivity.getClassName().equals(getActivity().getClass().getName())) {
                    Intent intent= new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                getActivity().finish();

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
