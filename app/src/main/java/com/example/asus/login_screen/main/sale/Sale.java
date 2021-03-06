package com.example.asus.login_screen.main.sale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.model.Store;
import com.example.asus.login_screen.model.TypeOfProduct;
import com.example.asus.login_screen.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Sale extends Fragment implements ItemClickListener {
    ProductAdapter myAdapter;
    protected Handler handler;
    List<Product> mainList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    TextView tv_typeSort, tv_Quantity;
    RecyclerView recyclerView;
    Toolbar mToolbar;
    SwipeRefreshLayout pullToRefresh;
    String currentType = "Tất cả sản phẩm";
    FloatingActionButton cart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public Sale() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sale, container, false);
        handler = new Handler();
        tv_typeSort = v.findViewById(R.id.tv_typeSort);
        tv_Quantity = v.findViewById(R.id.tv_quantity);
        mToolbar = v.findViewById(R.id.toolbar_Sale);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        cart = v.findViewById(R.id.fab);
        recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this.getActivity());

        getAllProduct();

        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        myAdapter = new ProductAdapter(this.getContext(), mainList, recyclerView);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setClickListener(this);


        tv_typeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet_TypeSort bottomSheet = new BottomSheet_TypeSort();
                bottomSheet.setOnListItemSelectedListener(new BottomSheet_TypeSort.TypeSelectListener() {
                    @Override
                    public void TypeSelect(String text) {
                        tv_typeSort.setText(text);
                        currentType = text;
                        getTypeProduct(text);
                        myAdapter.updateData(mainList);
                        recyclerView.setAdapter(myAdapter);
                    }
                });
                bottomSheet.show(getFragmentManager(), "");
            }
        });
        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query ref = mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(Local_Cache_Store.getOwnerDetail().getEmail());
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Store store = dataSnapshot.getValue(Store.class);
                        assert store != null;
                        Local_Cache_Store.setListOfProductType(store.getListOfProductType());
                        Local_Cache_Store.setListOrders(store.getListOrders());
                        Local_Cache_Store.setOwnerDetail(store.getOwnerDetail());
                        Local_Cache_Store.setShopAdress(store.getShopAdress());
                        Local_Cache_Store.setShopName(store.getShopName());
                        getTypeProduct(currentType);
                        myAdapter.updateData(mainList);
                        recyclerView.setAdapter(myAdapter);
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
                pullToRefresh.setRefreshing(false);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog cart_dialog = new Dialog(getContext());
                cart_dialog.setContentView(R.layout.cart_layout);
                final TextView total = cart_dialog.findViewById(R.id.tv_total);
                final Button checkout = cart_dialog.findViewById(R.id.btn_checkout);
                total.setText(Main_Screen.total);
                LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity().getBaseContext());
                RecyclerView rv_orderedProduct = (RecyclerView) cart_dialog.findViewById(R.id.rv_orderedProduct);
                rv_orderedProduct.setLayoutManager(linearLayout);
                final ProductInCartAdapter cartAdapter = new ProductInCartAdapter(getContext(), Main_Screen.orderedProductList);
                cartAdapter.setClickListener(new ItemClickListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onClick(View view, int position, String data) {
                        Double paid = 0.0;
                        try {
                            paid = DecimalFormat.getNumberInstance().parse(Main_Screen.total).doubleValue();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Double add = Double.parseDouble(data);
                        paid += add;
                        if(add<0 &&cartAdapter.getItemCount()==0) checkout.setEnabled(false);
                        Main_Screen.total = String.format("%1$,.0f", paid);
                        total.setText(String.format("%1$,.0f", paid));
                        int temp = Main_Screen.orderedProductList.size();
                        if (temp != 0) tv_Quantity.setVisibility(View.VISIBLE);
                        else tv_Quantity.setVisibility(View.INVISIBLE);
                        tv_Quantity.setText(String.valueOf(temp));
                    }
                });
                rv_orderedProduct.setAdapter(cartAdapter);
                if(cartAdapter.getItemCount()>0) checkout.setEnabled(true); else checkout.setEnabled(false);
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double paid = 0.0;
                        try {
                            paid = DecimalFormat.getNumberInstance().parse(Main_Screen.total).doubleValue();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        ((Main_Screen) getActivity()).openNewContentFragment(paid);
                        cart_dialog.dismiss();
                    }
                });


                cart_dialog.show();
            }
        });
        int temp = Main_Screen.orderedProductList.size();
        if (temp != 0) {
            tv_Quantity.setVisibility(View.VISIBLE);
            tv_Quantity.setText(String.valueOf(temp));
        }

        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        int temp = Main_Screen.orderedProductList.size();
        if (temp != 0) {
            tv_Quantity.setVisibility(View.VISIBLE);
            tv_Quantity.setText(String.valueOf(temp));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int temp = Main_Screen.orderedProductList.size();
        if (temp != 0) {
            tv_Quantity.setVisibility(View.VISIBLE);
            tv_Quantity.setText(String.valueOf(temp));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) mSearch.getActionView();
//change icon color
        ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.searcha));
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<Product> newList = new ArrayList<>();
                for (Product product : mainList) {
                    if (product.getId().toLowerCase().contains(userInput) || product.getName().toLowerCase().contains(userInput)) {
                        newList.add(product);
                    }
                }
                myAdapter.updateData(newList);
                recyclerView.setAdapter(myAdapter);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    void getAllProduct() {
        mainList.clear();
        for (TypeOfProduct type : Local_Cache_Store.getListOfProductType().values()) {
            for (Product product : type.getProductList().values()) {
                if(product.getQuantity()>0) mainList.add(product);
            }

        }
    }

    void getTypeProduct(String mType) {
        mainList.clear();

        if (mType.equals("Tất cả sản phẩm")) {
            getAllProduct();
            return;
        }
        for (TypeOfProduct type : Local_Cache_Store.getListOfProductType().values()) {
            if (type.getType().equals(mType)) {
                for (Product product : type.getProductList().values()) {
                    mainList.add(product);
                }
            }
        }


    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View view, int position, String data) {
        Product orderedProduct = new Product(mainList.get(position));
        Double paid = 0.0;
        try {
            paid = DecimalFormat.getNumberInstance().parse(Main_Screen.total).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        for (Product product : Main_Screen.orderedProductList) {
            if (product.getId() == orderedProduct.getId()) {
                return;
            }
        }
        paid += orderedProduct.getSalePrice();
        Main_Screen.total = String.format("%1$,.0f", paid);
        orderedProduct.setQuantity(1);
        int temp = Integer.parseInt(tv_Quantity.getText().toString());
        temp++;
        tv_Quantity.setVisibility(View.VISIBLE);

        Main_Screen.orderedProductList.add(orderedProduct);
        tv_Quantity.setText(String.valueOf(temp));
    }
}
