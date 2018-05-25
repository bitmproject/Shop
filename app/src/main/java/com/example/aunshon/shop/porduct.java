package com.example.aunshon.shop;

public class porduct {
    private String productTitle;
    private String productCatagory;
    private String productPrice;
    private String thumbnil;

    public porduct() {
    }

    public porduct(String productTitle, String productCatagory, String productPrice, String thumbnil) {
        if(productTitle.trim().equals("")){
            productTitle="No Title";
        }
        if(productCatagory.trim().equals("")){
            productCatagory="No Title";
        }
        if(productPrice.equals("")){
            productPrice="0.0";
        }
        if(thumbnil.trim().equals("")){
            thumbnil="No Title";
        }
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

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setProductCatagory(String productCatagory) {
        this.productCatagory = productCatagory;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setThumbnil(String thumbnil) {
        this.thumbnil = thumbnil;
    }
}
