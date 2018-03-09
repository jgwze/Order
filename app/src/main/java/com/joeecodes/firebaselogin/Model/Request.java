package com.joeecodes.firebaselogin.Model;

import java.util.List;

/**
 * Created by Lenovo on 6/3/2018.
 */

public class Request {
    private String phone;
    private String name;
    private String status;     //Status of Order... Reservation for us
    private String total;

    private List<Order>foods; //list of food ordered

    public Request() {
    }

    public Request(String phone, String name, String total, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.total = total;
        this.foods = foods;
        this.status="0"; //default is 0. 0:Placed/Awaiting Confirmation 1:Shipped/Reservation Placed
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
