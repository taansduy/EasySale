package com.example.asus.login_screen;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.login_screen.Model.saleAnalyst;
import com.example.asus.login_screen.Model.Store;
import com.example.asus.login_screen.Model.User;
import com.example.asus.login_screen.Model.daySale;
import com.example.asus.login_screen.Model.monthSale;
import com.example.asus.login_screen.Model.yearSale;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SignUp_Step2 extends android.support.v4.app.Fragment {


    public SignUp_Step2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        final String username=bundle.getString("username");
        View v=inflater.inflate(R.layout.fragment_sign_up__step2, container, false);
        TextInputEditText userName=(TextInputEditText) v.findViewById(R.id.userName);
        userName.setText(username);
        userName.setEnabled(false);
        final EditText shopName,phoneNum,shopAddress,name;
        final TextInputLayout shopNameWrapper,phoneWrapper,addressWrapper,nameWrapper;
        shopName=(EditText) v.findViewById(R.id.shopName);
        shopNameWrapper=(TextInputLayout) v.findViewById(R.id.shopNameWrapper);
        phoneNum=(EditText) v.findViewById(R.id.phone);
        name=(EditText) v.findViewById(R.id.name);
        phoneWrapper=(TextInputLayout) v.findViewById(R.id.phoneWrapper);
        shopAddress=(EditText) v.findViewById(R.id.address);
        addressWrapper=(TextInputLayout) v.findViewById(R.id.addressWrapper);
        nameWrapper=(TextInputLayout) v.findViewById(R.id.nameWrapper);
        shopName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !phoneNum.hasFocus() && !shopAddress.hasFocus() && !name.hasFocus()){
                    hideKeyboard(v);
                }
                if(hasFocus)
                {
                    shopNameWrapper.setError(null);
                }
            }
        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !phoneNum.hasFocus() && !shopAddress.hasFocus() && !shopName.hasFocus()){
                    hideKeyboard(v);
                }
                if(hasFocus)
                {
                    nameWrapper.setError(null);
                }
            }
        });
        phoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !shopName.hasFocus() && !shopAddress.hasFocus()&& !name.hasFocus()){
                    hideKeyboard(v);
                }
            }
        });
        shopAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !shopName.hasFocus() && !phoneNum.hasFocus()&& !name.hasFocus()){
                    hideKeyboard(v);
                }
            }
        });
        Button btnFinish;
        btnFinish=(Button) v.findViewById(R.id.btn_Finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mDatabase;
                if(shopName.getText().toString().isEmpty())
                {
                    shopNameWrapper.setErrorTextAppearance(R.style.error_appearance);
                    shopNameWrapper.setError("Vui lòng nhập tên cửa hàng");
                }
                else if(shopName.getText().toString().isEmpty())
                {
                    nameWrapper.setErrorTextAppearance(R.style.error_appearance);
                    nameWrapper.setError("Vui lòng nhập tên chur cửa hàng");
                } else {
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.child("stores").child(shopName.getText().toString()).exists()) {
                                Toast.makeText(getActivity(), "exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Store newStore = new Store();
                                newStore.setShopName(shopName.getText().toString());
                                newStore.setShopAdress(shopAddress.getText().toString());
                                User newUser = new User();
                                newUser.setEmail(username);
                                newUser.setPhoneNumber(phoneNum.getText().toString());
                                newUser.setmName(name.getText().toString());
                                newStore.setOwnerDetail(newUser);

                                saleAnalyst yearSales=new saleAnalyst();
                                yearSale n_yearSale=new yearSale();
                                monthSale n_monthSale=new monthSale();
                                daySale n_daySale=new daySale();
                                HashMap<String,daySale> temp=n_monthSale.getListOfDSale();
                                temp.put(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"_",n_daySale);
                                n_monthSale.setListOfDSale(temp);
                                HashMap<String,monthSale> temp1=n_yearSale.getListOfMSale();
                                temp1.put(Calendar.getInstance().get(Calendar.MONTH)+1+"_",n_monthSale);
                                n_yearSale.setListOfMSale(temp1);
                                yearSales.putYearSale(Calendar.getInstance().get(Calendar.YEAR)+"_",n_yearSale);
                                mDatabase.child("saleAnalyst").child(newStore.getShopName()).setValue(yearSales);
                                mDatabase.child("stores").child(newStore.getShopName()).setValue(newStore);
                                Local_Cache_Store.setListOfProductType(newStore.getListOfProductType());
                                Local_Cache_Store.setListOrders(newStore.getListOrders());
                                Local_Cache_Store.setOwnerDetail(newStore.getOwnerDetail());
                                Local_Cache_Store.setShopAdress(newStore.getShopAdress());
                                Local_Cache_Store.setShopName(newStore.getShopName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    startActivity(new Intent(getActivity().getBaseContext(),Main_Screen.class));
                    getActivity().finish();
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
