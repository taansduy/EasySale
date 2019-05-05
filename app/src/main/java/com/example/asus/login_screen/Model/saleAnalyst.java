package com.example.asus.login_screen.Model;

import java.util.HashMap;

public class saleAnalyst {
    private HashMap<String, yearSale> yearSales;

    public saleAnalyst() {
        yearSales=new HashMap<String, yearSale>();
    }

    public saleAnalyst(HashMap<String, yearSale> yearSales) {

        this.yearSales = yearSales;
    }

    public HashMap<String, yearSale> getYearSales() {
        return yearSales;
    }

    public void setYearSales(HashMap<String, yearSale> yearSales) {
        this.yearSales = yearSales;
    }
    public void putYearSale(String year,yearSale sale)
    {
        yearSales.put(year,sale);
    }
}
