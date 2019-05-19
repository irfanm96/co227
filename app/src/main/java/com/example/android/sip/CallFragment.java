package com.example.android.sip;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.ContactDetails;

import java.util.ArrayList;

public class CallFragment extends Fragment {

    View view;
    ImageButton imageButton;
    EditText editText;
    private static final String TAG = "APP_DEBUG";
    private ImageButton callButton;
    private Dialog dialog;
    private ImageButton hangUp;
    Dialog mydialog;

    private RecyclerView recyclerView;
    private ArrayList<Contact> contactList;


    public CallFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.call_fragment, container, false);
        imageButton = (ImageButton) view.findViewById(R.id.imgbtnNewContact);
        callButton = (ImageButton) view.findViewById(R.id.imgbtnNewCall);
        editText = (EditText) view.findViewById(R.id.etPhoneNumber);

        callButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mydialog = new Dialog(v.getContext(), android.R.style.Widget_DeviceDefault_ActionBar);
                mydialog.setContentView(R.layout.outgoing_call);
                mydialog.show();
                hangUp = (ImageButton) mydialog.findViewById(R.id.btnHangUp);

                hangUp.setOnClickListener(new View.OnClickListener() {
                    private static final String TAG = "APP_DEBUG";

                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: hang up clikced");
                        mydialog.dismiss();

                    }
                });


            }
        });


        //edited
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ContactDetails.class);
                intent.putExtra("STATUS", "NEW");
                intent.putExtra("PHONE", editText.getText().toString());
                v.getContext().startActivity(intent);
            }
        });

        ContactFragment contactFragment;

        contactFragment = ((ContactFragment) getActivity()
                .getSupportFragmentManager().getFragments().get(1)
        );

        contactFragment.test();



        recyclerView = (RecyclerView) view.findViewById(R.id.rv_contact_call);
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), contactList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        editText.setText("----------sds");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d(TAG, "onTextChanged: "+s);
                if(s.toString().isEmpty()){
                    s="*-------------";
                }
                recyclerViewAdapter.getPhoneFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (getActivity() != null && getView() != null) {
            if (!isVisibleToUser) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            } else {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactList = new ArrayList<>();
        contactList.add(new Contact("demo", "200@ping.com", "200"));
        contactList.add(new Contact("Irfan", "3000@ping.com", "3000"));
        contactList.add(new Contact("Wishma", "3001@ping.com", "3001"));
        contactList.add(new Contact("Rishi", "3002@ping.com", "3002"));
    }


}
