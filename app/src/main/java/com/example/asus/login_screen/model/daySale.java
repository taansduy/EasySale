package com.example.asus.login_screen.model;

public class daySale {

    private double totalSale;

    public daySale(double totalSale) {
        this.totalSale = totalSale;
    }

    public double getTotalSale() {
        return totalSale;
    }


    public daySale() {
        totalSale=0;

    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

}
