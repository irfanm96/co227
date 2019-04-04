package com.example.android.sip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PresenceChannel;
import com.pusher.client.channel.PresenceChannelEventListener;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.channel.User;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {


    private static final String TAG = "APP_DEBUG";

    private ArrayList<Contact> contacts = new ArrayList<>();

    private RecyclerViewAdapter recyclerViewAdapter;


    //    FirebaseDatabase database=FirebaseDatabase.getInstance();
//    DatabaseReference mRootRef=database.getReference();
//    DatabaseReference mConditionRef=mRootRef.child("contacts");
//
    private RecyclerView recyclerView;
    //    Pusher pusher;
    private Pusher pusher;

    private static String auth = "\"{\"auth\":\"cde729701e7a414aee3e:dc2c800485b5d90000fdaea73f4d5c3f6560ea93aeb7e79c8a8c3101eda4d93e\",\"channel_data\":\"{\"user_id\":468,\"user_info\":{\"user_id\":468,\"email\":\"abcd@gmail.com\"}}\"}\"";

    public static void setAuth(String auth) {
        ContactActivity.auth = auth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Log.d(TAG, "onCreate: started");


        contacts.add(new Contact("Irfan"));
        recyclerView = findViewById(R.id.rvView);
        recyclerViewAdapter = new RecyclerViewAdapter(this, contacts);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PusherOptions options = new PusherOptions();
        options.setHost("192.168.8.105");
        options.setWsPort(6001);
        options.setEncrypted(false);
        options.buildUrl("ABCDEFG");


        HttpAuthorizer authorizer = new HttpAuthorizer("http://192.168.8.105:8000/api/broadcast/auth");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization",((App) getApplication()).getPrefManager().getUserAccessToken());
        authorizer.setHeaders(headers);
        options.setAuthorizer(authorizer);

        Pusher pusher = new Pusher("ABCDEFG", options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange connectionStateChange) {
                Log.d("APP_DEBUG", "Changed to " + connectionStateChange.getCurrentState());
            }

            @Override
            public void onError(String s, String s1, Exception e) {

                Log.d("APP_DEBUG", "Failed " + s + " " + s1);
            }
        });

        Channel channel = pusher.subscribe("ch");

        channel.bind("App\\Events\\ContactCreated", new SubscriptionEventListener() {
            @Override
            public void onEvent(String s, String s1, String s2) {
                Log.d("APP_DEBUG", "Event " + s + " " + s2);
            }
        });



        PresenceChannel presenceChannel = pusher.subscribePresence("presence-chat", new PresenceChannelEventListener() {
            @Override
            public void onUsersInformationReceived(String s, Set<User> set) {
                Log.d(TAG, "onUsersInformationReceived: ");
            }

            @Override
            public void userSubscribed(String s, User user) {
                Log.d("APP_DEBUG_SUBSCRIBED", s);
                Log.d(TAG, "userSubscribed: "+user.getInfo());

            }

            @Override
            public void userUnsubscribed(String s, User user) {
                Log.d("APP_DEBUG_UNSUBSCRIBER", s);
                Log.d(TAG, "userUnsubscribed: "+user.getInfo());

            }

            @Override
            public void onAuthenticationFailure(String s, Exception e) {
                Log.d("APP_DEBUG_AUTH_FAIL", s);
                Log.d(TAG, "onAuthenticationFailure: "+e.getMessage());

            }

            @Override
            public void onSubscriptionSucceeded(String s) {
                Log.d("APP_DEBUG_SUB_SUCCESS", s);

            }

            @Override
            public void onEvent(String s, String s1, String s2) {

            }
        });





//        PusherOptions options = new PusherOptions();
//        options.setHost("192.168.8.105");
//        options.setWsPort(6001);
//        options.setEncrypted(false);
//        options.buildUrl("ABCDEFG");
////
//        HttpAuthorizer authorizer = new HttpAuthorizer("http://192.168.8.105:8000/api/broadcast/auth");
//        HashMap<String, String> defaultHeaders = new HashMap<String, String>();
//        defaultHeaders.put("Content-Type", "application/x-www-form-urlencoded");
//        defaultHeaders.put("Accept", "application/json");
//        defaultHeaders.put("Authorization", "Bearer " + ((App) getApplication()).getPrefManager().getUserAccessToken());
//        authorizer.setHeaders(defaultHeaders);
////        options.setAuthorizer(authorizer);
//
//
//        RestApi restApi = RetrofitClient.getClient().create(RestApi.class);
//        Call<String> call = restApi.auth(new channel(channel,socket_id));
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
////
//
//                Log.d("APP_DEBUG", "RESPONSE IS " + response.code());
//                if (response.code() != 200) {
////                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    Log.d(TAG, "onResponse: "+response.body());
////                    setAuth(response.body());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//                Log.d("APP_DEBUG", "ERROR IS " + t.getMessage());
//
//            }
//        });
//
//        options.setAuthorizer(new Authorizer() {
//            @Override
//            public String authorize(String s, String s1) throws AuthorizationFailureException {
//                Log.d(TAG, "authorize: " + s);
//                Log.d(TAG, "authorize: " + s1);
//                ContactActivity.authorize(s,s1);
//
//                return ContactActivity.auth;
//            }
//        });
//        pusher = new Pusher("ABCDEFG", options);
//
//        pusher.connect(new ConnectionEventListener() {
//            @Override
//            public void onConnectionStateChange(ConnectionStateChange change) {
//                System.out.println("State changed to " + change.getCurrentState() +
//                        " from " + change.getPreviousState());
//
//            }
//
//            @Override
//            public void onError(String message, String code, Exception e) {
//                System.out.println(e.getMessage());
//                Log.d(TAG, "onError: connectionn error");
//            }
//        });
//

    }


    @Override
    protected void onStart() {
        super.onStart();
//
//        PresenceChannel channel = pusher.subscribePresence("presence-chat", new PresenceChannelEventListener() {
//            @Override
//            public void onUsersInformationReceived(String s, Set<User> set) {
//                Log.d(TAG, "onUsersInformationReceived: ");
//            }
//
//            @Override
//            public void userSubscribed(String s, User user) {
//                Log.d(TAG, "userSubscribed: ");
//            }
//
//            @Override
//            public void userUnsubscribed(String s, User user) {
//                Log.d(TAG, "userUnsubscribed: ");
//            }
//
//            @Override
//            public void onAuthenticationFailure(String s, Exception e) {
//                Log.d(TAG, "onAuthenticationFailure: " + e.getMessage());
//            }
//
//            @Override
//            public void onSubscriptionSucceeded(String s) {
//                Log.d(TAG, "onSubscriptionSucceeded: ");
//            }
//
//            @Override
//            public void onEvent(String s, String s1, String s2) {
//                Log.d(TAG, "onEvent: ");
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                recyclerViewAdapter.getFilter().filter(s);
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }


    private static void authorize(String channel, String socket_id) {

    }
}
