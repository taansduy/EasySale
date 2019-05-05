package com.example.asus.login_screen.Model;

import java.util.HashMap;

public class Store {
    private String shopName;
    private String shopAdress;
    private HashMap<String,TypeOfProduct> listOfProductType;
    private User  ownerDetail;
    private HashMap<String,Bill> listOrders;



    public Store() {
        ownerDetail=new User();
        shopName="";
        shopAdress="";
        listOfProductType=new HashMap<String,TypeOfProduct>();
        listOrders=new HashMap<String,Bill>();

    }

    public Store(String shopName, String shopAdress, HashMap<String, TypeOfProduct> listOfProductType, User ownerDetail, HashMap<String, Bill> listOrders) {
        this.shopName = shopName;
        this.shopAdress = shopAdress;
        this.listOfProductType = listOfProductType;
        this.ownerDetail = ownerDetail;
        this.listOrders = listOrders;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAdress() {
        return shopAdress;
    }

    public void setShopAdress(String shopAdress) {
        this.shopAdress = shopAdress;
    }


    public User getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(User ownerDetail) {
        this.ownerDetail = ownerDetail;
    }


    public HashMap<String, TypeOfProduct> getListOfProductType() {
        return listOfProductType;
    }

    public void setListOfProductType(HashMap<String, TypeOfProduct> listOfProductType) {
        this.listOfProductType = listOfProductType;
    }

    public HashMap<String, Bill> getListOrders() {
        return listOrders;
    }

    public void setListOrders(HashMap<String, Bill> listOrders) {
        this.listOrders = listOrders;
    }
}
