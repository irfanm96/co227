package com.example.android.sip;


import android.app.Dialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
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


        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.outgoing_call); //add here to get updated
                dialog.show();

                //"callended xml should be poped up when press the end_button in outgoing call"


//        view = inflater.inflate(R.layout.call_fragment, container, false);
//        imageButton = (ImageButton) view.findViewById(R.id.imgbtnNewContact);
//        editText = (EditText) view.findViewById(R.id.etPhoneNumber);
//
//        Log.d(TAG, "onCreateView: ");


            }
        });
        //edited
//        callButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), OutgoingCall.class);
//                v.getContext().startActivity(intent);
//            }
//        });
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (getActivity() != null && getView()!=null) {
            if (!isVisibleToUser) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            } else {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        }

    }
}
