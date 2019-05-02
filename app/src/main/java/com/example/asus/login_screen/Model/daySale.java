package com.example.asus.login_screen.Model;

import java.util.ArrayList;
import java.util.List;

public class daySale {
    private int day;
    private double totalSale;
    private List<Order> listOfOrder;

    public int getDay() {
        return day;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public List<Order> getListOfOrder() {
        return listOfOrder;
    }

    public daySale(int day) {

        this.day = day;
        totalSale=0;
        listOfOrder=new ArrayList<Order>();
    }

    public daySale() {

    }

    public daySale(int day, double totalSale, List<Order> listOfOrder) {

        this.day = day;
        this.totalSale = totalSale;
        this.listOfOrder = listOfOrder;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public void setListOfOrder(List<Order> listOfOrder) {
        this.listOfOrder = listOfOrder;
    }
}
