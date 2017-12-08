package com.wocba.imbededsystem.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChatReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())){

        }

    }
}
