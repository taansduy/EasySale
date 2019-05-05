package com.example.asus.login_screen.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Bill {
    private String time;
    private String id;
    private List<Product> productList;
    private double total;

    public Bill() {
        productList=new ArrayList<Product>();
    }



    public Bill(String time, String id, List<Product> productList, double total) {
        this.time = time;
        this.id = id;
        this.productList = productList;
        this.total = total;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
