package com.joeecodes.firebaselogin.Model;

/**
 * Created by Lenovo on 5/2/2018.
 */

public class Category {
    private String name;
    private String image;

    public Category(){
    }

    public Category(String name, String image) {
        name = name;
        image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
