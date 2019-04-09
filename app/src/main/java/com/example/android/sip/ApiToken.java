package com.example.android.sip;

import com.google.gson.annotations.SerializedName;

public class ApiToken {

    @SerializedName("api_token")
    private  String api_token;

    @SerializedName("phone")
    private  String phone;

    public String getApi_token() {
        return api_token;
    }

    public String getPhone() {
        return phone;
    }
}
