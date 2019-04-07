package com.example.android.sip;

import android.app.Activity;
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

    private RecyclerView recyclerView;

    //    Pusher pusher;
    private Pusher pusher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Log.d(TAG, "onCreate: started");


        recyclerView = findViewById(R.id.rvView);
        recyclerViewAdapter = new RecyclerViewAdapter(this, contacts);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PusherOptions options = new PusherOptions();
        options.setHost(App.ip);
        options.setWsPort(6001);
        options.setEncrypted(false);
        options.buildUrl("ABCDEFG");


        HttpAuthorizer authorizer = new HttpAuthorizer(App.channelAuth);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", ((App) getApplication()).getPrefManager().getUserAccessToken());
        authorizer.setHeaders(headers);
        options.setAuthorizer(authorizer);

        pusher = new Pusher("ABCDEFG", options);
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
                Log.d(TAG, "userSubscribed: " + user.getInfo());
                Gson g = new Gson();
                Contact p = g.fromJson(user.getInfo(), Contact.class);
                Log.d(TAG, "userSubscribed: " + p.getPhone());
                Log.d(TAG, "userSubscribed: " + p.getName());
//                        contacts.add(p);
//                        recyclerViewAdapter.notifyDataSetChanged();
//                        recyclerViewAdapter.setContactListFull(contacts);
                updateContacts(p);

            }

            @Override
            public void userUnsubscribed(String s, User user) {
                Log.d("APP_DEBUG_UNSUBSCRIBER", s);
                Log.d(TAG, "userUnsubscribed: ");
                Gson g = new Gson();
                Contact p = g.fromJson(user.getInfo(), Contact.class);
                removeContacts(p);
                Log.d(TAG, "userUnsubscribed: " + p.getPhone());
                Log.d(TAG, "userUnsubscribed: " + p.getName());
            }

            @Override
            public void onAuthenticationFailure(String s, Exception e) {
                Log.d("APP_DEBUG_AUTH_FAIL", s);
                Log.d(TAG, "onAuthenticationFailure: " + e.getMessage());

            }

            @Override
            public void onSubscriptionSucceeded(String s) {
                Log.d("APP_DEBUG_SUB_SUCCESS", s);
            }

            @Override
            public void onEvent(String s, String s1, String s2) {

            }
        });

    }

    private void updateContacts(final Contact c) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contacts.add(c);
                recyclerViewAdapter.notifyDataSetChanged();
                recyclerViewAdapter.setContactListFull(contacts);
            }
        });
    }

    private void removeContacts(final Contact c) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int i=0;

                for (Contact p:contacts) {
                    if(p.getEmail().equals(c.getEmail())){
                        contacts.remove(i);
                        recyclerViewAdapter.notifyDataSetChanged();
                        recyclerViewAdapter.setContactListFull(contacts);
                        Log.d(TAG, "run: "+"removed");
                        break;
                    }
                    i++;
                }

            }
        });
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

}
