package com.example.asus.login_screen.Model;

import java.util.HashMap;

public class TypeOfProduct {
    private String ID;
    private String Type;
    private HashMap<String,Product> productList;

///////////////////////
    public TypeOfProduct() {
        ID="";
        Type="";
        productList=new HashMap<String,Product>();
    }
    ///////////////////

    public TypeOfProduct(String ID, String type, HashMap<String, Product> productList) {
        this.ID = ID;
        this.productList = productList;
        Type = type;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
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


}
