package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.sip.R;

public class ContactDetails extends AppCompatActivity {


    private static final String TAG = "APP_DEBUG";
    EditText etContactName;
    EditText etContactPhone;
    Button btnSaveContact;
    Button btnDeleteContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        etContactName = (EditText) findViewById(R.id.etContactName);
        etContactPhone = (EditText) findViewById(R.id.etContactPhone);
        btnDeleteContact = (Button) findViewById(R.id.btnDeleteContact);
        btnSaveContact = (Button) findViewById(R.id.btnSaveContact);

        btnDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked delete");
            }
        });

        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked save");
            }
        });


        //existing contact
        if (getIntent().getStringExtra("STATUS").equalsIgnoreCase("existing")) {
            String name = getIntent().getStringExtra("NAME");
            String phone = getIntent().getStringExtra("PHONE");
            etContactName.setText(name);
            etContactPhone.setText(phone);

        } else {//new contact
            btnDeleteContact.setVisibility(View.INVISIBLE);

        }


    }
}
