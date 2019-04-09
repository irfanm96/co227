package com.example.android.sip;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "pabx_preference";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String USER_EMAIL = "Email";
    private static final String USER_Phone = "Phone";
    private static final String USER_Name = "Name";
    private static final String USER_Password = "Password";
    private static final String USER_ACCESS_TOKEN = "access_token";
    private static final String USER_ID = "Id";
    private static final String WINNERS_IN_TIME = "WinnersInTime";

    private static final String TAG = "APP_DEBUG";
    // shared pref mode
    int PRIVATE_MODE = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "setIsLoggedIn: "+isLoggedIn);
    }
    public void setUSER_Password(String password) {
        editor.putString(USER_Password, password);
        editor.commit();
        Log.d(TAG, "setUSER_Password: "+password);
    }

    public boolean isLoggedIn() {

        return pref.getBoolean(IS_LOGGED_IN, false);

    }

    public String getUserAccessToken() {

        return pref.getString(USER_ACCESS_TOKEN, "");

    }
     public String getUSER_Password() {

        return pref.getString(USER_Password, "");

    }

    public String getUSER_Name() {

        return pref.getString(USER_Name, "");

    }
    public String getUSER_Phone() {

        return pref.getString(USER_Phone, "200");

    }

    public void setUserAccessToken(String apiToken) {
        editor.putString(USER_ACCESS_TOKEN, apiToken);
        editor.commit();
        Log.d(TAG, "setUserAccessToken: "+apiToken);
    }
    public void setUserName(String name) {
        editor.putString(USER_Name, name);
        editor.commit();
        Log.d(TAG, "setUserName: "+name);
    }
    public void setUSER_Phone(String phone) {
        editor.putString(USER_Phone, phone);
        editor.commit();
        Log.d(TAG, "setUSER_Phone: "+phone);
    }


    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
        Log.d(TAG, "setUserEmail: "+email);
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String id) {
        editor.putString(USER_ID, id);
        editor.commit();
        Log.d(TAG, "setUserId: "+id);
    }

}