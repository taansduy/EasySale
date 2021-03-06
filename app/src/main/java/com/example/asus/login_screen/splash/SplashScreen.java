package com.example.asus.login_screen.splash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.MainActivity;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.model.Store;
import com.example.asus.login_screen.R;
import com.example.asus.login_screen.authentication.SignUp_Screen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            // mAuth.signOut();
            final DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Query ref=mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(currentUser.getEmail());
            synchronized (this) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            Query ref=mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(currentUser.getEmail());
                            ref.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Store store = dataSnapshot.getValue(Store.class);
                                    Local_Cache_Store.setListOfProductType(store.getListOfProductType());
                                    Local_Cache_Store.setListOrders(store.getListOrders());
                                    Local_Cache_Store.setOwnerDetail(store.getOwnerDetail());
                                    Local_Cache_Store.setShopAdress(store.getShopAdress());
                                    Local_Cache_Store.setShopName(store.getShopName());
                                    Intent intent=new Intent(SplashScreen.this, Main_Screen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else
                        {
                            Intent mIntent = new Intent(SplashScreen.this, SignUp_Screen.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putString("Screen","Step2");
                            mBundle.putString("email",currentUser.getEmail().toString());
                            mIntent.putExtras(mBundle);
                            startActivity(mIntent,mBundle);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


        }
        else{
            Intent intent=new Intent(SplashScreen.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
