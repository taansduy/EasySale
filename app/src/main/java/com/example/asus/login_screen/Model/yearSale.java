package com.example.asus.login_screen.Model;

import java.util.HashMap;
import java.util.List;

public class yearSale {
    private double totalSale;
    private HashMap<String,monthSale> listOfMSale;

    public yearSale() {
        totalSale=0;
        listOfMSale=new HashMap<String,monthSale>();
    }

    public yearSale(double totalSale, HashMap<String, monthSale> listOfMSale) {
        this.totalSale = totalSale;
        this.listOfMSale = listOfMSale;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public HashMap<String, monthSale> getListOfMSale() {
        return listOfMSale;
    }


    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public void setListOfMSale(HashMap<String, monthSale> listOfMSale) {
        this.listOfMSale = listOfMSale;
    }

}
