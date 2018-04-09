package com.ptit.mytrip;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

public class User {
    private String hoTen;
    private String email;
    private String avtUrl;

    public User(){
    }

    public User(String hoTen, String email) {
        this.hoTen = hoTen;
        this.email = email;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvtUrl() {
        return avtUrl;
    }

    public void setAvtUrl(String avtUrl) {
        this.avtUrl = avtUrl;
    }
}

