package com.example.asus.login_screen.Fragment_of_MainScreen;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
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
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.asus.login_screen.BottomSheet_TypeSort;
import com.example.asus.login_screen.ItemClickListener;
import com.example.asus.login_screen.Local_Cache_Store;
import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.Model.Product;
import com.example.asus.login_screen.Model.Store;
import com.example.asus.login_screen.Model.TypeOfProduct;
import com.example.asus.login_screen.OnLoadMoreListener;
import com.example.asus.login_screen.ProductAdapter;
import com.example.asus.login_screen.ProductInCartAdapter;
import com.example.asus.login_screen.R;
import com.example.asus.login_screen.SplashScreen;
import com.example.asus.login_screen.cart_dialog;
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



public class Sale extends Fragment implements ItemClickListener {
    ProductAdapter myAdapter;
    protected Handler handler;
    List<Product> mainList=new ArrayList<>();
    List<Product> productList=new ArrayList<>();
    TextView tv_typeSort,tv_Quantity;
    RecyclerView recyclerView;
    Toolbar mToolbar;
    SwipeRefreshLayout pullToRefresh;
    String currentType="Tất cả sản phẩm";
    FloatingActionButton cart;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public Sale() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_sale, container, false);
        handler = new Handler();
        tv_typeSort=(TextView) v.findViewById(R.id.tv_typeSort);
        tv_Quantity=(TextView) v.findViewById(R.id.tv_quantity);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_Sale);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        cart=(FloatingActionButton) v.findViewById(R.id.fab) ;
        recyclerView=(RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this.getActivity());

        getAllProduct();
        get10FirstProduct();

        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        myAdapter=new ProductAdapter(this.getContext(),productList,recyclerView);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setClickListener(this);
        myAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (productList.size() < mainList.size()) {
                    productList.add(null);
                    myAdapter.notifyItemInserted(productList.size() - 1);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            productList.remove(productList.size() - 1);
                            myAdapter.notifyItemRemoved(productList.size());
                            //add items one by one
                            int start = productList.size();
                            int end = start + 20;

                            for (int i = start + 1; i <= end; i++) {
                                if (i == mainList.size()) break;
                                productList.add(mainList.get(i));
                                myAdapter.notifyItemInserted(productList.size());
                            }
                            myAdapter.setLoaded();
                        }
                    }, 2000);

                }
            }
        });

        tv_typeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet_TypeSort bottomSheet=new BottomSheet_TypeSort();
                bottomSheet.setOnListItemSelectedListener(new BottomSheet_TypeSort.TypeSelectListener() {
                    @Override
                    public void TypeSelect(String text) {
                        tv_typeSort.setText(text);
                        currentType=text;
                        getTypeProduct(text);
                        get10FirstProduct();
                        //myAdapter=new ProductAdapter(getContext(),productList,recyclerView);
                        myAdapter.updateData(productList);
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                if (productList.size() < mainList.size()) {
                                    productList.add(null);
                                    myAdapter.notifyItemInserted(productList.size() - 1);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //   remove progress item
                                            productList.remove(productList.size() - 1);
                                            myAdapter.notifyItemRemoved(productList.size());
                                            //add items one by one
                                            int start = productList.size();
                                            int end = start + 10;

                                            for (int i = start + 1; i <= end; i++) {
                                                if (i == mainList.size()) break;
                                                productList.add(mainList.get(i));
                                                myAdapter.notifyItemInserted(productList.size());
                                            }
                                            myAdapter.setLoaded();
                                        }
                                    }, 2000);

                                }
                            }
                        });
                    }
                });
                bottomSheet.show(getFragmentManager(),"");
            }
        });
        pullToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query ref=mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(Local_Cache_Store.getOwnerDetail().getEmail());
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Store store = dataSnapshot.getValue(Store.class);
                        Local_Cache_Store.setListOfProductType(store.getListOfProductType());
                        Local_Cache_Store.setListOrders(store.getListOrders());
                        Local_Cache_Store.setOwnerDetail(store.getOwnerDetail());
                        Local_Cache_Store.setShopAdress(store.getShopAdress());
                        Local_Cache_Store.setShopName(store.getShopName());
                        getTypeProduct(currentType);
                        get10FirstProduct();
                        myAdapter.updateData(productList);
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
                final Dialog cart_dialog=new Dialog(getContext());
                cart_dialog.setContentView(R.layout.cart_layout);
                final TextView total=(TextView) cart_dialog.findViewById(R.id.tv_total);
                Button checkout=(Button) cart_dialog.findViewById(R.id.btn_checkout);
                total.setText(Main_Screen.total);
                LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity().getBaseContext());
                RecyclerView rv_orderedProduct=(RecyclerView)cart_dialog.findViewById(R.id.rv_orderedProduct);
                rv_orderedProduct.setLayoutManager(linearLayout);
                ProductInCartAdapter cartAdapter=new ProductInCartAdapter(getContext(),Main_Screen.orderedProductList);
                cartAdapter.setClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, String data) {
                        Double paid= 0.0;
                        try {
                            paid = DecimalFormat.getNumberInstance().parse(Main_Screen.total).doubleValue();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Double add=Double.parseDouble(data);
                        paid+=add;
                        Main_Screen.total=String.format("%1$,.0f",paid);
                        total.setText(String.format("%1$,.0f",paid));
                        int temp= Main_Screen.orderedProductList.size();
                        if(temp!=0) tv_Quantity.setVisibility(View.VISIBLE);
                        else tv_Quantity.setVisibility(View.INVISIBLE);
                        tv_Quantity.setText(String.valueOf(temp));
                    }
                });
                rv_orderedProduct.setAdapter(cartAdapter);
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double paid= 0.0;
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
        int temp= Main_Screen.orderedProductList.size();
        if(temp!=0){
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
        int temp= Main_Screen.orderedProductList.size();
        if(temp!=0){
            tv_Quantity.setVisibility(View.VISIBLE);
            tv_Quantity.setText(String.valueOf(temp));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int temp= Main_Screen.orderedProductList.size();
        if(temp!=0){
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
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.searcha));
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput=newText.toLowerCase();
                List<Product> newList=new ArrayList<>();
                for(Product product : mainList)
                {
                    if(product.getId().toLowerCase().contains(userInput)||product.getName().toLowerCase().contains(userInput)||product.getIdType().toLowerCase().contains(userInput))
                    {
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
    void get10FirstProduct()
    {
        productList.clear();
        for (int i = 0; i < 10; i++) {
            if(i==mainList.size()) break;
            productList.add(mainList.get(i));

        }
    }
    void getAllProduct()
    {
        mainList.clear();
        for (TypeOfProduct type: Local_Cache_Store.getListOfProductType().values()) {
            for(Product product: type.getProductList().values())
            {
                mainList.add(product);
            }

        }
    }
    void getTypeProduct(String mType)
    {
            mainList.clear();

            if (mType.equals("Tất cả sản phẩm")) {
                getAllProduct();
                return;
            }
            for (TypeOfProduct type: Local_Cache_Store.getListOfProductType().values()) {
                if(type.getType().equals(mType))
                {
                    for (Product product : type.getProductList().values()) {
                        mainList.add(product);
                    }
                }
            }


    }


    @Override
    public void onClick(View view, int position, String data) {
        Product orderedProduct=new Product(productList.get(position));
        Double paid=0.0;
        try {
            paid = DecimalFormat.getNumberInstance().parse(Main_Screen.total).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paid+=orderedProduct.getSalePrice();
        Main_Screen.total=String.format("%1$,.0f",paid);
        for(Product product:Main_Screen.orderedProductList)
        {
            if(product.getId()==orderedProduct.getId())
            {
                product.setQuantity(product.getQuantity()+1);
                return;
            }
        }

        orderedProduct.setQuantity(1);
        int temp= Integer.parseInt(tv_Quantity.getText().toString());
        temp++;
        tv_Quantity.setVisibility(View.VISIBLE);

        Main_Screen.orderedProductList.add(orderedProduct);
        tv_Quantity.setText(String.valueOf(temp));
    }
}
