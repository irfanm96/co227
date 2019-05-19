package com.example.android.sip;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.ContactDetails;

public class CallFragment extends Fragment {

    View view;
    ImageButton imageButton;
    EditText editText;
    private static final String TAG = "APP_DEBUG";
    private ImageButton callButton;
    private Dialog dialog;

    public CallFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.call_fragment,container,false);
        imageButton=(ImageButton)view.findViewById(R.id.imgbtnNewContact);
        callButton=(ImageButton)view.findViewById(R.id.imgbtnNewCall);
        editText=(EditText) view.findViewById(R.id.etPhoneNumber);


        //callButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
//                dialog = new Dialog(v.getContext());
//                dialog.setContentView(R.layout.outgoing_call); //add here to get updated
//               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();

            //}
       // });
        //edited
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OutgoingCall.class);
                v.getContext().startActivity(intent);
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
        return view;
    }
}
