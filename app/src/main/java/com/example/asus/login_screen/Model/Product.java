package com.example.asus.login_screen.Model;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product {
    private String id;
    private String idType;
    private int quantity;
    private double costPrice;
    private String manufacturer;
    private double salePrice;
    private String name;
    private HashMap<String,String> listImage;
    private String description;
    Product(int i, int idType, Integer value, String substring){

    }
    public Product(){
        this.id = "";
        this.idType = "";
        this.quantity = 1;
        this.costPrice = 1;
        this.manufacturer = "";
        this.salePrice = salePrice;
        this.name = "";
        this.listImage =new HashMap<String,String>();
        this.description = "";
    }


    public Product(int i, int i1, int parseInt, double v, String s, double parseDouble, String toString, List<String> tmp) {
        listImage= new HashMap<String, String>();
    }

    public Product(String id, String idType, int quantity, double costPrice, String manufacturer, double salePrice, String name, HashMap<String,String> listImage, String description) {
        this.id = id;
        this.idType = idType;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.manufacturer = manufacturer;
        this.salePrice = salePrice;
        this.name = name;
        this.listImage = listImage;
        this.description = description;
    }

    public Product(String id, String idType, int quantity, String name) {
        this.id = id;
        this.idType = idType;
        this.quantity = quantity;
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String,String> getListImage() {
        return listImage;
    }

    public void setListImage(HashMap<String,String> listImage) {
        this.listImage = listImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }
}

