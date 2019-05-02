package com.example.asus.login_screen.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable {
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

    public List<TypeOfProduct> getListOfProductType() {
        return listOfProductType;
    }

    public void setListOfProductType(List<TypeOfProduct> listOfProductType) {
        this.listOfProductType = listOfProductType;
    }

    public User getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(User userName) {
        this.ownerDetail = userName;
    }

    private String shopName;
    private String shopAdress;
    List<TypeOfProduct> listOfProductType;
    private User  ownerDetail;


    public Store(String shopName, String shopAdress, List<TypeOfProduct> listOfProductType, User userName) {
        this.shopName = shopName;
        this.shopAdress = shopAdress;
        this.listOfProductType = listOfProductType;
        this.ownerDetail = userName;
    }

    public Store() {
        ownerDetail=new User();
        shopName="";
        shopAdress="";
        listOfProductType=new ArrayList<TypeOfProduct>();
    }
}
