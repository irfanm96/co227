package com.example.android.sip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements TextWatcher {


    @BindView(R.id.etEmailSignup)
    EditText etEmailSigup;

    @BindView(R.id.etPasswordSignup)
    EditText etPasswordSignup;

    @BindView(R.id.etConfirmPassowordSignup)
    EditText etConfirmPassowordSignup;

    @BindView(R.id.etNameSignup)
    EditText etNameSignup;

    Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        validator=new Validator();
        etNameSignup.addTextChangedListener(this);
        etEmailSigup.addTextChangedListener(this);

    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }


    @Override
    public void afterTextChanged(Editable s) {
        // validation code goes here



    }


    @OnClick(R.id.btnSignup2)
    public void signupButtonClicked() {


        String password = etPasswordSignup.getText().toString();
        String confirm_password = etConfirmPassowordSignup.getText().toString();
        String email = etEmailSigup.getText().toString();
        String name = etNameSignup.getText().toString();

        int valid=0;

        if(!validator.isNameValid(name)){
            etNameSignup.setError("enter valid name");
            valid++;

        }

        if(!validator.isEmailValid(email)){
            etEmailSigup.setError("enter valid email");
          valid++;

        }

        if(!validator.isPasswordValid(password)){
            etPasswordSignup.setError("enter strong password");
            valid++;
        }
        if(valid!=0){
            return;
        }


        if (!password.equals(confirm_password)) {
            etConfirmPassowordSignup.setError("Password does not match");
            return;

        } else {

            RestApi restApi = RetrofitClient.getClient().create(RestApi.class);

            Call<ApiToken> call = restApi.register(new User(name, email, password));

            call.enqueue(new Callback<ApiToken>() {
                @Override
                public void onResponse(Call<ApiToken> call, Response<ApiToken> response) {


                    if (response.code() == 422) {
                        Toast.makeText(getApplicationContext(),"The email has already been taken.",Toast.LENGTH_SHORT).show();
                        return;
                    } else if(response.code()==200){

                        Intent intent = new Intent(SignupActivity.this, WalkieTalkieActivity.class);
                        startActivity(intent);

                    }else {
                        Toast.makeText(getApplicationContext(),"Something went wrong,Try again",Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<ApiToken> call, Throwable t) {

                }
            });


        }


    }




}
