package com.example.androidtest.model;

public class AppUser {
    private String userId;
    private String secNum;
    private String nam;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecNum() {
        return secNum;
    }

    public void setSecNum(String secNum) {
        this.secNum = secNum;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId='" + userId + '\'' +
                ", secNum='" + secNum + '\'' +
                ", nam='" + nam + '\'' +
                '}';
    }
}
