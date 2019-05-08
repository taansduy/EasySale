package com.example.asus.login_screen;

import com.example.asus.login_screen.Model.Bill;
import com.example.asus.login_screen.Model.TypeOfProduct;
import com.example.asus.login_screen.Model.User;

import java.util.HashMap;

public class Local_Cache_Store {

    public static String shopName;
    public static String shopAdress;
    public static HashMap<String,TypeOfProduct> listOfProductType;
    public static User ownerDetail;
    public static HashMap<String,Bill> listOrders;

    public Local_Cache_Store() {
    }

    public static String getShopName() {
        return shopName;
    }

    public static void setShopName(String shopName) {
        Local_Cache_Store.shopName = shopName;
    }

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
