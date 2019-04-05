package com.example.android.sip;

public class Contact {
    private  String name;
  private  String email;
  private  int phone;

    public Contact(String name, String email, int phone) {
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

    public int getPhone() {
        return phone;
    }
}
