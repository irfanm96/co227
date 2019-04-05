package com.example.android.sip;

public class User {

    private String name;
    private  String email;
    private  String password;
    private int phone;

    public User(String name, String email, String password , int phone ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone=phone;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, int phone ,String api_token) {
        this.name = name;
        this.email = email;
        this.phone = phone;

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

    public int getPhone() {
        return phone;
    }
}
