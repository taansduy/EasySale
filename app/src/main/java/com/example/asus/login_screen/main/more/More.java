package com.example.asus.login_screen.main.more;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.login_screen.main.more.AddProduct.AddProduct;
import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.MainActivity;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.model.Store;
import com.example.asus.login_screen.model.TypeOfProduct;
import com.example.asus.login_screen.model.User;
import com.example.asus.login_screen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ACTIVITY_SERVICE;

@SuppressLint("ValidFragment")
public class More extends android.support.v4.app.Fragment {
    Main_Screen main_screen;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout ln_ContentMore,ln_ListProduct;
    Button button;
    TextView title,tv_Name,tv_Email,tv_title;
    EditText edt_Search;
    Toolbar toolbar;
    LinearLayout lnAccount,lnProduct,lnCustomer,lnLogout;
    ImageView img_Back,img_Add;
    RecyclerView recyclerView;
    List<Product> productList;
    MoreProductAdapter myAdapter;
    DatabaseReference mDatabase;
    StorageReference mStorage;
    public More(Context context) {
        // Required empty public constructor
        main_screen=(Main_Screen)context;
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
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
        lnLogout= view.findViewById(R.id.logout);
        ln_ContentMore= view .findViewById(R.id.lnContentMore);
        ln_ListProduct= view.findViewById(R.id.lnListproduct);
        swipeRefreshLayout=view.findViewById(R.id.refresh);
        img_Back=view.findViewById(R.id.Back);
        img_Add=view.findViewById(R.id.Add);
        recyclerView=view.findViewById(R.id.listProduct);
        toolbar=view.findViewById(R.id.toolbar2);
        edt_Search=view.findViewById(R.id.search);
        edt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchData();
                String userInput=s.toString().toLowerCase();
                ArrayList<Product> newList;
                String tmpType = null;
                newList = new ArrayList<>();
                for(TypeOfProduct tmp :Local_Cache_Store.getListOfProductType().values()){
                    if (tmp.getType().toLowerCase().contains(userInput)){ 
                        tmpType=tmp.getID();
                    }
                }
                for(Product product : productList)
                {
                    if(product.getName().toLowerCase().contains(userInput)||product.getIdType().equals(tmpType) )
                    {
                        newList.add(product);
                    }
                }
                myAdapter.updateList(newList);
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        productList= new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //*************************************************//
        mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+ Local_Cache_Store.getShopName() +"/listOfProductType");
        mStorage =  FirebaseStorage.getInstance().getReference("uploads/"+Local_Cache_Store.getShopName()+"/");
        myAdapter=new MoreProductAdapter(this.getContext(), (ArrayList<Product>) productList,mDatabase,mStorage);



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
                ((Main_Screen) getActivity()).destroy();

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
    public void fetchData()
    {
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(Local_Cache_Store.getShopName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                assert store != null;
                Local_Cache_Store.setListOfProductType(store.getListOfProductType());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
                productList.clear();
        for (TypeOfProduct type: Local_Cache_Store.getListOfProductType().values()) {
            productList.addAll(type.getProductList().values());
        }
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }
}
