package com.example.asus.login_screen.model;

import java.util.HashMap;

public class Local_Cache_Store {

    public static String shopName;
    public static String shopAdress;
    public  static String shopEmail;
    public static HashMap<String,TypeOfProduct> listOfProductType;
    public static HashMap<String, Product> listOfProduct;
    public static User ownerDetail;
    public static String shopPhoneNumber;
    public static HashMap<String,Bill> listOrders;

    public Local_Cache_Store() {
    }

    public static String getShopName() {
        return shopName;
    }

    public static void setShopName(String shopName) {
        Local_Cache_Store.shopName = shopName;
    }

    public static void setShopEmail(String shopEmail){Local_Cache_Store.shopEmail=shopEmail;}

    public static String getShopEmail(){return shopEmail;}

    public static void setShopPhoneNumber(String shopPhoneNumber){Local_Cache_Store.shopPhoneNumber=shopPhoneNumber;}
    public static String getShopPhoneNumber(){return shopPhoneNumber;}

    public static String getShopAdress() {
        return shopAdress;
    }

    public static void setShopAdress(String shopAdress) {
        Local_Cache_Store.shopAdress = shopAdress;
    }

    public static HashMap<String, TypeOfProduct> getListOfProductType() {
        return listOfProductType;
    }

    public static void setListOfProductType(HashMap<String, TypeOfProduct> listOfProductType) {
        Local_Cache_Store.listOfProductType = listOfProductType;
    }
    public static void setListOfProduct(HashMap<String, Product> listOfProduct) {
        Local_Cache_Store.listOfProduct = listOfProduct;
    }

    public static User getOwnerDetail() {
        return ownerDetail;
    }

    public static void setOwnerDetail(User ownerDetail) {
        Local_Cache_Store.ownerDetail = ownerDetail;
    }

    public static HashMap<String, Bill> getListOrders() {
        return listOrders;
    }

    public static void setListOrders(HashMap<String, Bill> listOrders) {
        Local_Cache_Store.listOrders = listOrders;
    }
}
