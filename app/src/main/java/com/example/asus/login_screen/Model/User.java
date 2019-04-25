package com.example.asus.login_screen.Model;

import java.util.Date;

public class User {

    private String email;
    private String mName;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;

    public User(String email, String mName, Date birthDate,String phoneNumber) {
        this.email = email;
        this.mName = mName;
        this.phoneNumber=phoneNumber;
    }

    public User() {

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

}
