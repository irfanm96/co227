package com.example.android.sip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    private ArrayList<Contact> contactList;


    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.contact_fragment,container,false);

        recyclerView=(RecyclerView) view.findViewById(R.id.rv_contact);
        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(getContext(),contactList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactList=new ArrayList<>();
        contactList.add(new Contact("demo","200@ping.com",200));
        contactList.add(new Contact("Irfan","3000@ping.com",3000));
        contactList.add(new Contact("Wishma","3001@ping.com",3001));
        contactList.add(new Contact("Rishi","3002@ping.com",3002));
    }
}
