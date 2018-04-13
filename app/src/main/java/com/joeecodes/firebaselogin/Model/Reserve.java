package com.joeecodes.firebaselogin.Model;

public class Reserve {
    private String phone;
    private String name;
    private String pax;
    private String confirmation;     //Status of reservation

    public Reserve() {
    }

    public Reserve(String phone, String name, String pax) {
        this.phone = phone;
        this.name = name;
        this.pax = pax;
        this.confirmation = "0"; //default is 0. 0:Awaiting Confirmation 1:Accepted 2. Rejected
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

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}



