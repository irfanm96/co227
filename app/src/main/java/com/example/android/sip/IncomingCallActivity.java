package com.example.android.sip;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;
    private Ringtone mRingtone;

    private static final int REQUEST_SIP = 10;
    private static final int USERS_ONLINE = 5;
    public static String sipAddress = null;

    private static final String TAG = "APP_DEBUG";
    public SipManager manager = null;
    public SipProfile me = null;
    public SipAudioCall call = null;
    public IncomingCallReceiver callReceiver;
    private ArrayList<Contact> contactList=new ArrayList<>();



    private CallFragment callFragment;
    private ContactFragment contactFragment;
    private SettingsFragment settingsFragment;
    private Dialog mydialog;
    private ImageButton hangUp;
    private ImageButton accept;

    public void setContact(Contact contact) {
        this.contact = contact;
        Log.d(TAG, "setContact: "+ contact.getPhone());
    }

    private Context context;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        fetchContacts();
//        tabLayout = findViewById(R.id.tb_layout);
//        viewPager = findViewById(R.id.view_pager);
//        adapter = new ViewPageAdapter(getSupportFragmentManager());

//        callFragment = new CallFragment();
//        contactFragment = new ContactFragment();
//        settingsFragment = new SettingsFragment();
//        adapter.addFragment(callFragment, "");
//        adapter.addFragment(contactFragment, "");
//        adapter.addFragment(settingsFragment, "");

//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_contact);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_settings);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setElevation(0);


        make();
        receivedIntent(getIntent());
//        fetchContacts();

    }



    public void make() {
//        ToggleButton pushToTalkButton = (ToggleButton) findViewById(R.id.pushToTalk);
//        pushToTalkButton.setOnTouchListener(this);

        // Set up the intent filter.  This will be used to fire an
        // IncomingCallReceiver when someone calls the SIP address used by this
        // application.

        Log.d(TAG, "make: function is called");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.SipDemo.INCOMING_CALL");
        callReceiver = new IncomingCallReceiver();
//        callReceiver.setActivity(this);
        this.registerReceiver(callReceiver, filter);

        // "Push to talk" can be a serious pain when the screen keeps turning off.
        // Let's prevent that.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initializeManager();
    }

    public void initializeManager() {
        if (manager == null) {
            manager = SipManager.newInstance(this);
            Log.d(TAG, "initializeManager: manager initialiased");
        }

        initializeLocalProfile();
    }


    /**
     * Logs you into your SIP provider, registering this device as the location to
     * send SIP calls to for your SIP address.
     */
    public void initializeLocalProfile() {
        if (manager == null) {
            return;
        }

        if (me != null) {
            closeLocalProfile();
        }

        String username = ((App) getApplication()).getPrefManager().getUSER_Phone();
        String domain = ((App) getApplication()).getPrefManager().getDomain();
        String password = ((App) getApplication()).getPrefManager().getUSER_Password();

        try {
            SipProfile.Builder builder = new SipProfile.Builder(username, domain);
            builder.setPassword(password);
            me = builder.build();

            Log.d(TAG, "initializeLocalProfile: profile built");
            Intent i = new Intent();
            i.setAction("android.SipDemo.INCOMING_CALL");
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, Intent.FILL_IN_DATA);
            manager.open(me, pi, null);


            // This listener must be added AFTER manager.open is called,
            // Otherwise the methods aren't guaranteed to fire.

            manager.setRegistrationListener(me.getUriString(), new SipRegistrationListener() {
                public void onRegistering(String localProfileUri) {
                    Log.d("APP_DEBUG", "onRegistering: ");
                    updateStatus("Registering with SIP Server...");
                }

                public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    Log.d("APP_DEBUG", "onRegistrationDone: ");
                    updateStatus("Ready");
                }

                public void onRegistrationFailed(String localProfileUri, int errorCode,
                                                 String errorMessage) {
                    Log.d("APP_DEBUG", "onRegistrationFailed: " + errorMessage + " " + errorCode);
                    updateStatus("Registration failed.  Please check settings.");
                }
            });
        } catch (ParseException pe) {
            updateStatus("Connection Error.");
        } catch (SipException se) {
            updateStatus("Connection error.");
        }
    }

    private void updateStatus(String s) {
        Log.d(TAG, "updateStatus: "+s);
    }

    /**
     * Closes out your local profile, freeing associated objects into memory
     * and unregistering your device from the server.
     */
    public void closeLocalProfile() {
        if (manager == null) {
            return;
        }
        try {
            if (me != null) {
                manager.close(me.getUriString());
            }
        } catch (Exception ee) {
            Log.d(TAG, "Failed to close local profile.", ee);
        }
    }

    private SipAudioCall incCall = null;

    public void incomingCall(SipAudioCall c) {
        if (c == null) {
            return;
        }
        if (c.isInCall()) {
            return;
        }
        if (incCall != null) {
            return;
        }
        incCall = c;
        String ringtoneUri =
                Settings.System.DEFAULT_RINGTONE_URI.toString();
        mRingtone = RingtoneManager.getRingtone(getBaseContext(),
                Uri.parse(ringtoneUri));
        mRingtone.play();

        SipProfile caller = incCall.getPeerProfile();

        Log.d(TAG, "incomingCall: " + caller.getUserName());
//         contact = getMatch(caller.getUserName());

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                showIncomingCallDialog(contact);

            }

        }, 50);



    }


    private boolean isInMuted = false;
    private boolean isHoldIn = false;
    private boolean isSpeakerIn = false;

    @SuppressLint("SetTextI18n")
    public void showIncomingCallDialog(Contact c) {


        Log.d(TAG, "showIncomingCallDialog: showing incomin call dialog");
        
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

//        Log.d(TAG, "call to  " + c.getPhone() + " name " + c.getName());
        mydialog = new Dialog(this, android.R.style.Widget_DeviceDefault_ActionBar);
        mydialog.setContentView(R.layout.incoming_call);
        mydialog.show();
        TextView tvCallName = (TextView) mydialog.findViewById(R.id.tvCallNameIncoming);

        tvCallName.setText("From: " + c.getName() + " - " + c.getPhone());

        hangUp = (ImageButton) mydialog.findViewById(R.id.btnHangUpIncoming);
        accept = (ImageButton) mydialog.findViewById(R.id.btnAnswerIncoming);
        final TextView st = (TextView) mydialog.findViewById(R.id.tvStatusIncoming);
        final Chronometer chronometer = (Chronometer) mydialog.findViewById(R.id.cmTimerIncoming);

        final ImageView mic = (ImageView) mydialog.findViewById(R.id.ivMicIn);
        final ImageView speaker = (ImageView) mydialog.findViewById(R.id.ivSpeakerIn);
        final ImageView pause = (ImageView) mydialog.findViewById(R.id.ivPauseIn);

        mic.setVisibility(View.INVISIBLE);
        speaker.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);


        chronometer.setVisibility(View.INVISIBLE);
        accept.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                if (incCall != null) {

                    mRingtone.stop();
                    try {
                        incCall.answerCall(30);
                        st.setText("On Call..");
                        chronometer.setVisibility(View.VISIBLE);
                        mic.setVisibility(View.VISIBLE);
                        speaker.setVisibility(View.VISIBLE);
                        pause.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.INVISIBLE);

                        ObjectAnimator mover = ObjectAnimator.ofFloat(hangUp, "translationX", 0, 200);
                        mover.start();

                        chronometer.start();
                        incCall.startAudio();

//                        incCall.setSpeakerMode(true);

                        if (incCall.isMuted()) {
                            Log.d(TAG, "call was muted ");
                            incCall.toggleMute();
                        }
                    } catch (SipException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        hangUp.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "APP_DEBUG";

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: hang up clikced");
//                finishActivity(0);
                mRingtone.stop();
//                finish();
//                if (incCall != null) {
                try {
                    incCall.endCall();
                } catch (SipException e) {
                    e.printStackTrace();
                }
                incCall.close();
                incCall = null;
//                }

            finish();
            }

        });


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mute the call
                if (!isInMuted) {
                    isInMuted = true;
                    if (!incCall.isMuted()) {
                        call.toggleMute();
                    }
                    mic.setImageResource(R.drawable.microphone_off);
                } else {

                    isInMuted = false;
                    if (incCall.isMuted()) {
                        incCall.toggleMute();
                    }
                    mic.setImageResource(R.drawable.microphone_on);

                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mute the call
                if (!isHoldIn) {
                    isHoldIn = true;
                    try {
                        incCall.holdCall(30);
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                    pause.setImageResource(R.drawable.ic_pause_on);
                } else {

                    isHoldIn = false;
                    try {
                        incCall.continueCall(30);
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                    pause.setImageResource(R.drawable.ic_pause_off);

                }
            }
        });
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mute the call
                if (!isSpeakerIn) {
                    isSpeakerIn = true;
                    incCall.setSpeakerMode(true);
                    speaker.setImageResource(R.drawable.speaker_mode_on);
                } else {
                    isSpeakerIn = false;
                    incCall.setSpeakerMode(false);
                    speaker.setImageResource(R.drawable.speaker_mode_off);

                }
            }
        });


    }


    private static final int ON_CALL_ENDED_IN = 1;
    private static final int ON_CALL_ERROR_IN = 2;


    public void updateIncomingCallDialog(final int mode) {
        // Be a good citizen.  Make sure UI changes fire on the UI thread.
        this.runOnUiThread(new Runnable() {
            public void run() {

                switch (mode) {
                    case ON_CALL_ENDED_IN:
                        mRingtone.stop();
                        finish();
//                        mydialog.dismiss();
                        break;
                    case ON_CALL_ERROR_IN:
                        mRingtone.stop();
                        finish();
                        final Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                mydialog.dismiss();
                            }
                        }, 5000);

                        break;

                }
            }
        });
    }


    private void receivedIntent(Intent intent){
        SipAudioCall incomingCall = null;
        final IncomingCallActivity c=this;
        try {

            SipAudioCall.Listener listener = new SipAudioCall.Listener() {

                @Override
                public void onCallEnded(SipAudioCall call) {
//                    super.onCallEnded(call);
                    Log.d(TAG, "imconing call ended ");
                    c.updateIncomingCallDialog(ON_CALL_ENDED_IN);
                    call.close();
                    try {
                        call.endCall();
                    } catch (SipException e) {
                        e.printStackTrace();
                    }

                    
                }

                @Override
                public void onError(SipAudioCall call, int errorCode, String errorMessage) {
//                    super.onError(call, errorCode, errorMessage);
                    Log.d(TAG, "incoming call error "+ errorMessage+ " code "+errorCode);
                    c.updateIncomingCallDialog(ON_CALL_ERROR_IN);
                    call.close();
                    try {
                        call.endCall();
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                    
                }
            };



            try {
                incomingCall=c.manager.takeAudioCall(intent,listener);
                fetchContacts(incomingCall.getPeerProfile().getUserName());
                c.incomingCall(incomingCall);
            } catch (SipException e) {
                e.printStackTrace();
                Log.d(TAG, "onReceive: "+e.getMessage());
            }

            incomingCall = c.manager.takeAudioCall(intent,null);
            incomingCall.setListener(listener,true);
        } catch (Exception e) {
            if (incomingCall != null) {
                incomingCall.close();
            }
        }
    }


    public void fetchContacts(String s) {

        RestApi restApi = RetrofitClient.getClient().create(RestApi.class);
        Call<Contact> call = restApi.checkContact(new Contact("asasa",s));
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {

                setContact(response.body());

            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {

            }
        });
    }


}
