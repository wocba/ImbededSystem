package com.wocba.imbededsystem.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ChatReceiver extends BroadcastReceiver {

    private FirebaseAuth mAuth;
    private String userName;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mAuth = FirebaseAuth.getInstance();
        if("com.wocba.imbedesystem.sendreceiver".equals(intent.getAction())){
            userName = intent.getStringExtra("1");
            if(mAuth.getCurrentUser().getEmail().equals(userName)){
//                Toast.makeText(context, mAuth.getCurrentUser().getEmail() + "에게서 문자가왔습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, userName + "에게서 문자가왔습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
