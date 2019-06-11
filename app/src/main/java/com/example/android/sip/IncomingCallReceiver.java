/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sip;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.*;
import android.util.Log;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * Listens for incoming SIP calls, intercepts and hands them off to WalkieTalkieActivity.
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    /**
     * Processes the incoming call, answers it, and hands it over to the
     * WalkieTalkieActivity.
     * @param context The context under which the receiver is running.
     * @param intent The intent being received.
     */

    private static final String TAG = "APP_DEBUG";

    private static final int ON_CALL_ENDED_IN = 1;
    private static final int ON_CALL_ERROR_IN = 2;
    private BaseActivity basicActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        SipAudioCall incomingCall = null;
        Log.d(TAG, "onReceive: recieved something");
        
        if(this.basicActivity==null){
            Log.d(TAG, "onReceive: the activity is null");
//            context.startActivity(new Intent(context, BaseActivity.class)
//                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


        final BaseActivity base=(BaseActivity) this.basicActivity;

        try {

            SipAudioCall.Listener listener = new SipAudioCall.Listener() {

                @Override
                public void onCallEnded(SipAudioCall call) {
//                    super.onCallEnded(call);
                    Log.d(TAG, "imconing call ended ");
                    base.updateIncomingCallDialog(ON_CALL_ENDED_IN);
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
                    base.updateIncomingCallDialog(ON_CALL_ERROR_IN);
                    call.close();
                    try {
                        call.endCall();
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCallBusy(SipAudioCall call) {
                    super.onCallBusy(call);
              }

                @Override
                public void onChanged(SipAudioCall call) {
                    super.onChanged(call);
              }
            };



            try {
                incomingCall=base.manager.takeAudioCall(intent,listener);
                base.incomingCall(incomingCall);
            } catch (SipException e) {
                e.printStackTrace();
                Log.d(TAG, "onReceive: "+e.getMessage());
            }
            
            incomingCall = base.manager.takeAudioCall(intent,null);
            incomingCall.setListener(listener,true);
        } catch (Exception e) {
            if (incomingCall != null) {
                incomingCall.close();
            }
        }
    }




    public void setActivity(BaseActivity baseActivity){
        this.basicActivity=baseActivity;
    }



}
