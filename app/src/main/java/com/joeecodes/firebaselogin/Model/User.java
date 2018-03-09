package com.joeecodes.firebaselogin.Model;

/**
 * Created by Lenovo on 4/3/2018.
 */

public class User {

    private String name;
    private String password;
    private String phone;
    private String staffaccount;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        staffaccount="false";
    }

    public String getStaffaccount() {
        return staffaccount;
    }

    public void setStaffaccount(String staffaccount) {
        this.staffaccount = staffaccount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
