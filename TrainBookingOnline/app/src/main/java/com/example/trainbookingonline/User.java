package com.example.trainbookingonline;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String phoneNumber;
    private String password;
    private String fullName;
    private String cccd;
    private String imageUrl;
    private String userUID;

    public User(){

    }
    public User(String email, String phoneNumber, String password, String fullname, String cccd, String imageUrl, String userUID) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullName = fullname;
        this.cccd = cccd;
        this.imageUrl = imageUrl;
        this.userUID = userUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullname) {
        this.fullName = fullname;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
