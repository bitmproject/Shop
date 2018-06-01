package com.example.aunshon.shop;

import com.google.firebase.database.Exclude;

public class cartmodel {
    private String productTitle;
    private String productCatagory;
    private String productPrice;
    private String thumbnil;
    private String mkey,email;

    public cartmodel() {
    }



    public cartmodel(String productTitle, String productCatagory, String productPrice, String thumbnil, String email) {
        this.productTitle = productTitle;
        this.productCatagory = productCatagory;
        this.productPrice = productPrice;
        this.thumbnil = thumbnil;
        this.email=email;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductCatagory() {
        return productCatagory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getThumbnil() {
        return thumbnil;
    }

    @Exclude
    public String getMkey() {
        return mkey;
    }
    @Exclude
    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
