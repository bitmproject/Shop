package com.example.aunshon.shop;

public class porduct {
    private String productTitle;
    private String productCatagory;
    private Double productPrice;
    private int thumbnil;

    public porduct() {
    }

    public porduct(String productTitle, String productCatagory, Double productPrice, int thumbnil) {
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

    public Double getProductPrice() {
        return productPrice;
    }

    public int getThumbnil() {
        return thumbnil;
    }
}
