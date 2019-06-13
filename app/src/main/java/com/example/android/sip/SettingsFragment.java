package com.example.android.sip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {


    private static final String TAG = "APP_DEBUG";

    Button btnLogout;
    Button btnTerms;
    Button btnContactUs;

    TextView tvUserName;
    TextView tvUserPhone;

    private View view;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);

        btnLogout=(Button)view.findViewById(R.id.btnLogout);
//        btnTerms=(Button)view.findViewById(R.id.btnTerms);
        btnContactUs=(Button)view.findViewById(R.id.btnContactUs);
//        btnTerms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//                builder1.setMessage("Write your message here.");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Ok",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
//            }
//        });

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "irfanmm96@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Ping App Support");
                startActivity(Intent.createChooser(intent, ""));
            }

        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutClicked();
            }
        });

        tvUserPhone=(TextView) view.findViewById(R.id.tvUserPhone);
        tvUserName=(TextView) view.findViewById(R.id.tvUserName);

        tvUserName.setText((((App) getActivity().getApplication()).getPrefManager().getUSER_Phone()));
        tvUserPhone.setText((((App) getActivity().getApplication()).getPrefManager().getUserEmail()));

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (getActivity() != null && getView() != null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
    }


    public void logoutClicked() {
        Log.d(TAG, "logouClicked: ");
        RestApi restApi = RetrofitClient.getClient().create(RestApi.class);
       Call<Void> call=restApi.logout();
        call.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {



                Log.d("APP_DEBUG", "RESPONSE IS " + response.code());
                if (response.code() != 200) {
                    Toast.makeText(getContext(),"Something occured unexpectedly",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ((App) getActivity().getApplication()).getPrefManager().setIsLoggedIn(false);
                    ((App) getActivity().getApplication()).getPrefManager().setUserAccessToken("");
                    ((App) getActivity().getApplication()).getPrefManager().setUSER_Phone("");
                    ((App) getActivity().getApplication()).getPrefManager().setUSER_Password("");
                    ((App) getActivity().getApplication()).getPrefManager().setUserEmail("");

                    BaseActivity context=(BaseActivity) getContext();
                    context.closeLocalProfile();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(),"Something occured unexpectedly",Toast.LENGTH_SHORT).show();

            }
        });
    }


}
