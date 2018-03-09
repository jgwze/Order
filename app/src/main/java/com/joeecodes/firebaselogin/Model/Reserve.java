package com.joeecodes.firebaselogin.Model;

/**
 * Created by Lenovo on 8/3/2018.
 */

public class Reserve {
    private String phone;
    private String name;
    private String pax;

    public Reserve() {
    }

    public Reserve(String phone, String name, String pax) {
        this.phone = phone;
        this.name = name;
        this.pax = pax;
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

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }
}



