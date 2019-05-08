package com.example.asus.login_screen.Model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private int idType;
    private int quantity;
    private double costPrice;
    private String manufacturer;
    private double salePrice;
    private String name;
    private List<String> listImage;
    private String description;
    Product(){

    }

    public Product(int i, int i1, int parseInt, double v, String s, double parseDouble, String toString, List<String> tmp) {
        listImage=new ArrayList<String>();
    }

    public Product(int id, int idType, int quantity, double costPrice, String manufacturer, double salePrice, String name, List<String> listImage, String description) {
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

    public Product(int id, int idType, int quantity, String name) {
        this.id = id;
        this.idType = idType;
        this.quantity = quantity;
        this.name = name;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }
}

