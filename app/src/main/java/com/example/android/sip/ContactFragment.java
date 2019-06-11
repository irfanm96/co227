package com.example.android.sip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myapplication.ContactDetails;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private static final String TAG = "APP_DEBUG";
    private View view;
    private EditText etSearch;
    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;
    private ArrayList<Contact> contactList = new ArrayList<>();
    private  RecyclerViewAdapter recyclerViewAdapter;


    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_fragment, container, false);

        etSearch = (EditText) view.findViewById(R.id.etSearch);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabNewContact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContactDetails.class);
                intent.putExtra("STATUS", "NEW");
                intent.putExtra("PHONE", "");
                getActivity().startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_contact);
         recyclerViewAdapter = new RecyclerViewAdapter(getContext(), contactList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                recyclerViewAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    public void test() {
        Log.d(TAG, "test: ok");
    }

    public void setContactList(ArrayList<Contact> contactList) {
        this.contactList.clear();
        this.contactList.addAll(contactList);
    }


    public void updateList(ArrayList<Contact> list){
        if(recyclerViewAdapter != null) {
            Log.d(TAG, "updateList: updting list");
            recyclerViewAdapter.updateList(list);
        }
    }


    public RecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }



}
