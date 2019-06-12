package com.example.android.sip;

import java.util.List;
import java.util.zip.CheckedOutputStream;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestApi {

//    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("login")
    Call<ApiToken> login(@Body UserCredentials user_credentials);
    @POST("register")
    Call<ApiToken> register(@Body User user);

    @POST("logout")
    Call<Void> logout();

    @POST("delete-contact")
    Call<Void> deleteContact(@Body Contact c);

    @POST("save-contact")
    Call<Contact> saveContact(@Body Contact c);

    @POST("contacts")
    Call<List<Contact>> contacts();

    @POST("check-contact")
    Call<Contact> checkContact(@Body Contact c);


}
