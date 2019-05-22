package com.example.android.sip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static final int MODIFY_AUDIO_SETTINGS = 10;
    private static final String TAG = "APP_DEBUG";
    private static final int RECORD_AUDIO = 20;
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


        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED) {

            Log.d("APP_DEBUG", "onCreate: no permission given for audio");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, MODIFY_AUDIO_SETTINGS);
        } else if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            Log.d("APP_DEBUG", "onCreate: no permission given for mic");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);

        }
        if (!(SipManager.isVoipSupported(getApplicationContext()) && SipManager.isApiSupported(getApplicationContext()))) {
            tvError.setText("Your phone does not support SIP");
            tvError.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            tvRegister.setEnabled(false);

            finish();
        }

//        ((App) getApplication()).getPrefManager().setIsLoggedIn(false);
        if (((App) getApplication()).getPrefManager().isLoggedIn()) {
            //check if session is valid and if valid
            Intent intent = new Intent(MainActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();

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
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        UserCredentials user = new UserCredentials(email, password);
        Call<ApiToken> call = restApi.login(user);
        call.enqueue(new Callback<ApiToken>() {
            @Override
            public void onResponse(Call<ApiToken> call, Response<ApiToken> response) {
//

                Log.d("APP_DEBUG", "RESPONSE IS " + response.code());
                if (response.code() != 200) {
                    tvError.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    App.setAccessToken(response.body().getApi_token());
                    ((App) getApplication()).getPrefManager().setIsLoggedIn(true);
                    ((App) getApplication()).getPrefManager().setUserAccessToken(response.body().getApi_token());
                    ((App) getApplication()).getPrefManager().setUSER_Phone(response.body().getPhone());
                    ((App) getApplication()).getPrefManager().setUSER_Password(password);
                    ((App) getApplication()).getPrefManager().setUserEmail(email);
                    Intent intent = new Intent(MainActivity.this, BaseActivity.class);
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

//    private int p;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
//        int p=0;
        switch (requestCode) {
            case MODIFY_AUDIO_SETTINGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    Log.d("APP_DEBUG", "onRequestPermissionsResult: permission granted for audio");

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: permission denied for audio");
//                    finish();
                }
            }
            case RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    Log.d("APP_DEBUG", "onRequestPermissionsResult: permission granted for mic");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: permission denied for mic");
//                    finish();
                }
            }
        }

    }


}


