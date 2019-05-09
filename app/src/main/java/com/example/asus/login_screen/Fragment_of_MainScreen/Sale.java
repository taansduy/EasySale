package com.example.asus.login_screen.Fragment_of_MainScreen;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.asus.login_screen.BottomSheet_TypeSort;
import com.example.asus.login_screen.Local_Cache_Store;
import com.example.asus.login_screen.Model.Product;
import com.example.asus.login_screen.Model.TypeOfProduct;
import com.example.asus.login_screen.ProductAdapter;
import com.example.asus.login_screen.R;

import java.util.ArrayList;
import java.util.List;

public class Sale extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        TextView tv_typeSort;
        RecyclerView recyclerView;
        Toolbar mToolbar;
        tv_typeSort=(TextView) v.findViewById(R.id.tv_typeSort);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_Sale);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        recyclerView=(RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this.getActivity());
        List<Product> productList=new ArrayList<>();
        for (TypeOfProduct type: Local_Cache_Store.getListOfProductType().values()) {
            for(Product product: type.getProductList().values())
            {
                productList.add(product);
            }

        }
        recyclerView.setLayoutManager(linearLayout);
        ProductAdapter myAdapter=new ProductAdapter(this.getContext(),productList);
        recyclerView.setAdapter(myAdapter);
        tv_typeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet_TypeSort bottomSheet=new BottomSheet_TypeSort();

                bottomSheet.show(getFragmentManager(),"");
            }
        });
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //mAdapter.getFilter().filter(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }




}
