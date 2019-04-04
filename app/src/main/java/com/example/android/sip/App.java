package com.example.android.sip;

import android.annotation.SuppressLint;
import android.app.Application;

import com.facebook.stetho.Stetho;

public class App extends Application {


//    public static String deviceID;
    public static boolean isLoggedIn;
    public static String accessToken;
    public PrefManager prefManager;
//    public Snackbar mSnackBar;


    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {


        super.onCreate();
        Stetho.initializeWithDefaults(this);
        prefManager = new PrefManager(this);
//        FirebaseApp.initializeApp(this);
//        RetrofitClient.initialize(this);

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


}