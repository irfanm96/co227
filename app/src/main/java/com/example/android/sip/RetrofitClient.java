package com.example.android.sip;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static String baseUrl = "http://192.168.8.105:8000/api/";
    //        private static String baseUrl = "http://d061db10.ngrok.io/api/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()

                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Accept", "application/json;versions=1");

                        if (App.isLoggedIn) {
                            ongoing.addHeader("Authorization", App.accessToken);
                        }
                        return chain.proceed(ongoing.build());
                    }
                })
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        Response response = chain.proceed(request);
//                        if (response.code() == 401) {
//                            Log.d("APP_DEBUG", "IN RESPONSE HEADER");
//                            EventBus.getDefault().post(new App.MessageEvent());
//
//                        }
//                        return response;
//
//                    }
//                })
//
                ;


        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()

                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


}