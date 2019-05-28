package com.example.asus.login_screen.model;

import java.util.HashMap;

public class monthSale {
    private double totalSale;
    private HashMap<String,daySale> listOfDSale;

    public monthSale() {
        totalSale=0;
        listOfDSale=new HashMap<String, daySale>();

    }
    public void setListOfDSale(HashMap<String,daySale> listOfDSale) {
        this.listOfDSale = listOfDSale;
    }
    public monthSale( double totalSale, HashMap<String,daySale> listOfDSale) {
        this.totalSale = totalSale;
        this.listOfDSale = listOfDSale;
    }
    public double getTotalSale() {
        return totalSale;
    }
    public HashMap<String, daySale> getListOfDSale() {
        return listOfDSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }
}
