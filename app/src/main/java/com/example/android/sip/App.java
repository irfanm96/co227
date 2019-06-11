package com.example.android.sip;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;

import com.facebook.stetho.Stetho;

public class App extends Application {


//    public static String deviceID;
    public static boolean isLoggedIn;
    public static String accessToken;
    public  PrefManager prefManager;
    public static String ip="10.30.7.43";
    public static String restApi="http://"+ip+":8000/api/";
    public static String channelAuth="http://"+ip+":8000/api/broadcast/auth";
//    public Snackbar mSnackBar;


    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {


        super.onCreate();
        Stetho.initializeWithDefaults(this);
        prefManager = new PrefManager(this);
//        FirebaseApp.initializeApp(this);
//        RetrofitClient.initialize(this);
        startService(new Intent(this, MyService.class));
        isLoggedIn = prefManager.isLoggedIn();
        accessToken = prefManager.getUserAccessToken();


//        deviceID = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID);


    }

    public PrefManager getPrefManager() {
        return prefManager;
    }


    public static class MessageEvent { /* Additional fields if needed */
    }


    public static String getDomain(){
        return domain;
    }

    private static String domain="192.168.1.3";

    public static String getAccessToken(){
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        App.accessToken = accessToken;
    }


}
