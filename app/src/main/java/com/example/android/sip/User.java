package com.example.android.sip;

public class User {

    private String name;
    private  String email;
    private  String password;
    private  String api_token;
    private int phone;

    public User(String name, String email, String password , int phone , String api_token) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.api_token=api_token;
        this.phone=phone;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getApi_token() {
        return api_token;
    }

    public int getPhone() {
        return phone;
    }
}
