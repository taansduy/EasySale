package com.example.asus.login_screen.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Store {
    private String shopName;
    private String shopAdress;
    private HashMap<String,TypeOfProduct> listOfProductType;
    private User  ownerDetail;
    private HashMap<String, yearSale> yearSales;



    public Store() {
        ownerDetail=new User();
        shopName="";
        shopAdress="";
        listOfProductType=new HashMap<String,TypeOfProduct>();
        yearSales=new HashMap<String,yearSale>();
        yearSale n_yearSale=new yearSale(Calendar.getInstance().get(Calendar.YEAR));
        monthSale n_monthSale=new monthSale(Calendar.getInstance().get(Calendar.MONTH)+1);
        daySale n_daySale=new daySale(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        HashMap<String,daySale> temp=n_monthSale.getListOfDSale();
        temp.put(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"_",n_daySale);
        n_monthSale.setListOfDSale(temp);
        HashMap<String,monthSale> temp1=n_yearSale.getListOfMSale();
        temp1.put(Calendar.getInstance().get(Calendar.MONTH)+1+"_",n_monthSale);
        n_yearSale.setListOfMSale(temp1);
        yearSales.put(Calendar.getInstance().get(Calendar.YEAR)+"_",n_yearSale);

    }

    public Store(String shopName, String shopAdress, HashMap<String, TypeOfProduct> listOfProductType, User ownerDetail, HashMap<String, yearSale> yearSales) {
        this.shopName = shopName;
        this.shopAdress = shopAdress;
        this.listOfProductType = listOfProductType;
        this.ownerDetail = ownerDetail;
        this.yearSales = yearSales;
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

    public HashMap<String, yearSale> getYearSales() {
        return yearSales;
    }

    public void setYearSales(HashMap<String, yearSale> yearSales) {
        this.yearSales = yearSales;
    }

    public HashMap<String, TypeOfProduct> getListOfProductType() {
        return listOfProductType;
    }

    public void setListOfProductType(HashMap<String, TypeOfProduct> listOfProductType) {
        this.listOfProductType = listOfProductType;
    }
}
