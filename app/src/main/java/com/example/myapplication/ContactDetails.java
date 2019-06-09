package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.sip.BaseActivity;
import com.example.android.sip.Contact;
import com.example.android.sip.R;
import com.example.android.sip.RestApi;
import com.example.android.sip.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactDetails extends AppCompatActivity {


    private static final String TAG = "APP_DEBUG";
    EditText etContactName;
    EditText etContactPhone;
    Button btnSaveContact;
    Button btnDeleteContact;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
                deleteContact();
            }
        });

        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked save");
                saveContact();
            }
        });


        //existing contact
        if (getIntent().getStringExtra("STATUS").equalsIgnoreCase("existing")) {
            String name = getIntent().getStringExtra("NAME");
            etContactName.setText(name);
            String phone = getIntent().getStringExtra("PHONE");
            etContactPhone.setText(phone);


        } else {//new contact
            btnDeleteContact.setVisibility(View.INVISIBLE);
            String phone1 = getIntent().getStringExtra("PHONE");
            if (!phone1.isEmpty()) {
                etContactPhone.setText(phone1);
            }

        }


    }

    public void saveContact() {

        RestApi restApi = RetrofitClient.getClient().create(RestApi.class);
        final String name = etContactName.getText().toString();
        final String phone = etContactPhone.getText().toString();

        Contact c = new Contact(name, phone);
        Call<Contact> call = restApi.saveContact(c);
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
//

                Log.d("APP_DEBUG", "RESPONSE IS " + response.code());
                if (response.code()!=200 && response.code()!=201 ) {
                    Toast.makeText(getApplicationContext(), "Oops Try again later", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    String name1 = response.body().getName();
                    String phone1 = response.body().getPhone();
                    Toast.makeText(getApplicationContext(), "Contact Saved Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ContactDetails.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {

                Log.d("APP_DEBUG", "ERROR IS " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Oops Try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deleteContact() {

        RestApi restApi = RetrofitClient.getClient().create(RestApi.class);
        final String name = etContactName.getText().toString();
        final String phone = etContactPhone.getText().toString();

        Contact c = new Contact(name, phone);
        Call<Void> call = restApi.deleteContact(c);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//

                Log.d("APP_DEBUG", "RESPONSE IS " + response.code());
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Oops Try again later", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    Toast.makeText(getApplicationContext(), "Contact Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ContactDetails.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Log.d("APP_DEBUG", "ERROR IS " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Oops Try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
