package com.joeecodes.firebaselogin.Model;
import java.util.List;

/**
 * Created by Lenovo on 6/3/2018.
 */

public class DeliveryRequest {
    private String phone;
    private String name;
    private String address;
    private String status;     //Status of Order... Reservation for us
    private String total;
    private String comment;
    private List<Order> foods; //list of food ordered

    public DeliveryRequest() {
    }

    public DeliveryRequest(String phone, String name, String address, String total, String comment, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.status = "0"; //default is 0. 0:Placed/Awaiting Confirmation 1:Shipped/Reservation Placed        this.total = total;
        this.comment = comment;
        this.foods = foods;
        this.total=total;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}