package com.example.asus.login_screen.Model;

import com.example.asus.login_screen.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class TypeOfProduct {
    private int ID;
    private String Type;
    private List<Product> productList;


    public TypeOfProduct(int ID, String type, List<Product> productList) {
        this.ID = ID;
        Type = type;
        this.productList = productList;
    }

    public TypeOfProduct(int ID, String type) {
        this.ID = ID;
        Type = type;
        productList=new ArrayList<Product>() ;

    }
}
