package com.example.android.sip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.google.gson.Gson;
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
import java.util.Set;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Log.d(TAG, "onCreate: started");


        contacts.add(new Contact( "Irfan"));
        recyclerView = findViewById(R.id.rvView);
        recyclerViewAdapter = new RecyclerViewAdapter(this, contacts);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PusherOptions options=new PusherOptions();
        options.setHost("192.168.8.105");
        options.setWsPort(6001);
        options.setEncrypted(false);
        options.buildUrl("ABCDEFG");
//
        HttpAuthorizer authorizer = new HttpAuthorizer("http://192.168.8.105:8000/api/broadcast/auth");
        HashMap<String, String> defaultHeaders = new HashMap<String, String>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("Authorization", "Bearer "+((App) getApplication()).getPrefManager().getUserAccessToken());
        authorizer.setHeaders(defaultHeaders);
        options.setAuthorizer(authorizer);
        pusher = new Pusher("ABCDEFG",options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());

            }
            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println(e.getMessage());
                Log.d(TAG, "onError: connectionn error");
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();

        PresenceChannel channel = pusher.subscribePresence("presence-chat", new PresenceChannelEventListener() {
            @Override
            public void onUsersInformationReceived(String s, Set<User> set) {
                Log.d(TAG, "onUsersInformationReceived: ");
            }

            @Override
            public void userSubscribed(String s, User user) {
                Log.d(TAG, "userSubscribed: ");
            }

            @Override
            public void userUnsubscribed(String s, User user) {
                Log.d(TAG, "userUnsubscribed: ");
            }

            @Override
            public void onAuthenticationFailure(String s, Exception e) {
                Log.d(TAG, "onAuthenticationFailure: "+e.getCause());
            }

            @Override
            public void onSubscriptionSucceeded(String s) {
                Log.d(TAG, "onSubscriptionSucceeded: ");
            }

            @Override
            public void onEvent(String s, String s1, String s2) {
                Log.d(TAG, "onEvent: ");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }
}
