package com.wocba.imbededsystem.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wocba.imbededsystem.R;

/**
 * Created by Administrator on 2017-11-30.
 */

public class ChatNick extends AppCompatActivity implements View.OnClickListener {

    Button btn_nick;
    EditText editTextNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatnickname);
        editTextNick = (EditText)findViewById(R.id.editTextNickname);
        findViewById(R.id.btn_nick).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("nickname",editTextNick.getText().toString());
        startActivity(intent);
    }
}
