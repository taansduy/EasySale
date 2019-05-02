package com.example.asus.login_screen.Model;

import java.util.HashMap;

public class TypeOfProduct {
    private int ID;
    private String Type;
    private HashMap<String,Product> productList;


    public TypeOfProduct(int ID, String type, HashMap<String, Product> productList) {
        this.ID = ID;
        Type = type;
        this.productList = productList;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public HashMap<String, Product> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<String, Product> productList) {
        this.productList = productList;
    }

    public TypeOfProduct() {

    }
}
