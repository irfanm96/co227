package com.example.android.sip;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.ContactDetails;

public class IncomingCall extends Fragment {

    View view;
    //private static final String TAG = "APP_DEBUG";
    private Button ansButton;
    private Button endButton;
    private Dialog dialog;

    public IncomingCall() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.incoming_call,container,false);
        ansButton=(Button)view.findViewById(R.id.answer_call);
        endButton=(Button)view.findViewById(R.id.end_call);
        //editText=(EditText) view.findViewById(R.id.etPhoneNumber);


        ansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.outgoing_call); //add here to get updated
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.call_ended); //add here to get updated
                dialog.show();
            }
        });

        return view;
    }
}
