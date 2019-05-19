package com.example.android.sip;

<<<<<<< HEAD
import android.app.Dialog;
=======
import android.content.Context;
>>>>>>> a01a5933302fa8b784a721a6a3b3d29dd60fb069
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
<<<<<<< HEAD
    private ImageButton callButton;
    private Dialog dialog;
=======
>>>>>>> a01a5933302fa8b784a721a6a3b3d29dd60fb069

    public CallFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

<<<<<<< HEAD
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
=======
        view = inflater.inflate(R.layout.call_fragment, container, false);
        imageButton = (ImageButton) view.findViewById(R.id.imgbtnNewContact);
        editText = (EditText) view.findViewById(R.id.etPhoneNumber);

        Log.d(TAG, "onCreateView: ");
>>>>>>> a01a5933302fa8b784a721a6a3b3d29dd60fb069

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
