package com.example.aunshon.shop;

public class UserInfoClass {
    String name;
    String email;
    String phone;
    String token_;

    public UserInfoClass() {
    }

    public UserInfoClass(String name, String email, String phone, String token_) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.token_ = token_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String gettoken_() {
        return token_;
    }

    public void setDevice_token(String device_token) {
        this.token_ = token_;
    }
}
