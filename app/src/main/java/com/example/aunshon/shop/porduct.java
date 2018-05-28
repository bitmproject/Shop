package com.example.aunshon.shop;

import com.google.firebase.database.Exclude;

public class porduct {
    private String productTitle;
    private String productCatagory;
    private String productPrice;
    private String thumbnil;
    private String mkey;

    public porduct() {
    }

    public porduct(String productTitle, String productCatagory, String productPrice, String thumbnil) {
        this.productTitle = productTitle;
        this.productCatagory = productCatagory;
        this.productPrice = productPrice;
        this.thumbnil = thumbnil;
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
}
