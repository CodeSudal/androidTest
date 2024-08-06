package com.example.androidtest.model;

public class AppUser {
    private String userId;
    private String secNum;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId='" + userId + '\'' +
                ", secNum='" + secNum + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
