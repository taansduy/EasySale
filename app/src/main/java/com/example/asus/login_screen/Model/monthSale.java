package com.example.asus.login_screen.Model;

import java.util.HashMap;

public class monthSale {
    private int month;
    private double totalSale;
    private HashMap<String,daySale> listOfDSale;

    public monthSale() {
    }

    public void setListOfDSale(HashMap<String,daySale> listOfDSale) {
        this.listOfDSale = listOfDSale;
    }

    public monthSale(int month) {
        this.month = month;
        totalSale=0;
        listOfDSale=new HashMap<String,daySale>();
    }

    public monthSale(int month, double totalSale, HashMap<String,daySale> listOfDSale) {

        this.month = month;
        this.totalSale = totalSale;
        this.listOfDSale = listOfDSale;
    }

    public int getMonth() {

        return month;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public HashMap<String, daySale> getListOfDSale() {
        return listOfDSale;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }
}
