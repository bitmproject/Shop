package com.example.aunshon.shop;

public class FavouritePInfo {
    String title;
    String price;
    String catagory;
    String imageurl;

    public FavouritePInfo() {
    }

    public FavouritePInfo(String title, String price, String catagory, String imageurl) {
        this.title = title;
        this.price = price;
        this.catagory = catagory;
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
