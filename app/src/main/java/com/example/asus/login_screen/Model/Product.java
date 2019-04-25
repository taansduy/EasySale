package com.example.asus.login_screen.Model;

public class Product {
    private int Id;
    private int Quantity;
    private double costPrice;
    private String producer;
    private double salePrice;

    public Product(int id, int quantity, double costPrice, String producer, double salePrice) {
        Id = id;
        Quantity = quantity;
        this.costPrice = costPrice;
        this.producer = producer;
        this.salePrice = salePrice;
    }

    public Product() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
}
