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


public class OutgoingCall extends Fragment {

    View view;
    private Button endButton;
    private Dialog dialog;

    public OutgoingCall() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.outgoing_call,container,false);
//        endButton=(Button)view.findViewById(R.id.end_button);


        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        endButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), CallEnded.class);
//                v.getContext().startActivity(intent);
//            }
//        });
        return view;
    }
}

