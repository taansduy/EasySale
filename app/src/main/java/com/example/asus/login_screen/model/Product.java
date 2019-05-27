package com.example.asus.login_screen.model;

import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private String id;
    private String idType;
    private int quantity;
    private double costPrice;
    private String manufacturer;
    private double salePrice;
    private String name;
    private HashMap<String,String> listImage;
    private String description;




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

    public Product() {
        listImage=new HashMap<String,String>();
    }

    public Product(Product another)
    {
        this.id = another.getId();
        this.idType = another.getIdType();
        this.quantity = another.getQuantity();
        this.costPrice = another.getCostPrice();
        this.manufacturer = another.getId();
        this.salePrice = another.getSalePrice();
        this.name = another.getName();
        this.listImage = another.getListImage();
        this.description = another.getId();
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

