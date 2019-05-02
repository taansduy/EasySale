package com.example.asus.login_screen.Model;

import java.util.HashMap;
import java.util.List;

public class yearSale {
    private int year;
    private double totalSale;
    private HashMap<String,monthSale> listOfMSale;

    public yearSale() {
    }


    public yearSale(int year) {

        this.year = year;
        totalSale=0;
        listOfMSale=new HashMap<String,monthSale>();
    }

    public int getYear() {
        return year;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public HashMap<String, monthSale> getListOfMSale() {
        return listOfMSale;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public void setListOfMSale(HashMap<String, monthSale> listOfMSale) {
        this.listOfMSale = listOfMSale;
    }
}
