package com.example.android.sip;

import android.content.Intent;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.tvError)
    TextView tvError;

    @BindView(R.id.tvRegsiter)
    TextView tvRegister;

    @BindView(R.id.btnLogin)
    Button btnLogin;


    PusherOptions options;

    //    Pusher pusher;
    private Pusher pusher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvError.setVisibility(View.INVISIBLE);

        if(!(SipManager.isVoipSupported(getApplicationContext()) && SipManager.isApiSupported(getApplicationContext()))){
            tvError.setText("Your phone does not support SIP");
            tvError.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            tvRegister.setEnabled(false);

        }else {

            ((App) getApplication()).getPrefManager().setIsLoggedIn(false);
            if (((App) getApplication()).getPrefManager().isLoggedIn()) {

                //check if session is valid and if valid
                Intent intent = new Intent(MainActivity.this, WalkieTalkieActivity.class);
                startActivity(intent);
                finish();

                //else login screen

            }
        }



    }


    @OnClick(R.id.tvRegsiter)
    public void signUpClicked() {

        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();


    }

    @OnClick(R.id.btnLogin)
    public void loginClicked(View view) {
        RestApi restApi = RetrofitClient.getClient().create(RestApi.class);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        UserCredentials user = new UserCredentials(email, password);
        Call<ApiToken> call = restApi.login(user);
        call.enqueue(new Callback<ApiToken>() {
            @Override
            public void onResponse(Call<ApiToken> call, Response<ApiToken> response) {
//

                Log.d("APP_DEBUG", "RESPONSE IS " + response.code());
                if (response.code()!=200) {
                    tvError.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    ((App) getApplication()).getPrefManager().setIsLoggedIn(true);
                    ((App) getApplication()).getPrefManager().setUserAccessToken(response.body().getApi_token());
                    ((App) getApplication()).getPrefManager().setUSER_Phone(response.body().getPhone());
                    Intent intent = new Intent(MainActivity.this, WalkieTalkieActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiToken> call, Throwable t) {

                Log.d("APP_DEBUG", "ERROR IS " + t.getMessage());

                tvError.setVisibility(View.VISIBLE);
//                tvError.setText(t.getMessage());

            }
        });
    }


}


