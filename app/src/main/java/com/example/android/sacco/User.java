package com.example.android.sacco;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Abhi on 20 Jan 2018 020.
 */

public class User {
    String username;
    String fullName;
    Date sessionExpiryDate;
    String imagePath;
    Bitmap bUserImage;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) { this.sessionExpiryDate = sessionExpiryDate; }

    public void setimagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setuserimage(Bitmap bUserImage) {
        this.bUserImage = bUserImage;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

    public String getimagePath() {
        return imagePath;
    }

    public Bitmap getuserimage() {
        return bUserImage;
    }
}