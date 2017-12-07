package com.wocba.imbededsystem.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wocba.imbededsystem.Data.User;
import com.wocba.imbededsystem.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2017-11-29.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    // Firebase
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mChatReference;
    private FirebaseAuth mAuth;
    private ChildEventListener mChildEventListener;
    // Views
    private ListView mListView;
    private EditText mEdtMessage;
    // Values
    private static final String TAG = "ChatActivity";
    private ChatAdapter mAdapter;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initViews();
        initFirebaseDatabase();
        initValues();
    }

    private void hideKeyboard(){
        imm.hideSoftInputFromWindow(mEdtMessage.getWindowToken(),0);
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.list_message);
        mAdapter = new ChatAdapter(this, 0);
        mListView.setAdapter(mAdapter);

        mEdtMessage = (EditText) findViewById(R.id.edit_message);
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    private void initFirebaseDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        mChatReference = mDatabaseReference.child("chats");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                Log.d(TAG, "Value is: " + chatData);
                mAdapter.add(chatData);
                mListView.smoothScrollToPosition(mAdapter.getCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    if (mAdapter.getItem(i).firebaseKey.equals(firebaseKey)) {
                        mAdapter.remove(mAdapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mChatReference.addChildEventListener(mChildEventListener);

    }

    private void initValues() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Intent intent = getIntent();
//        userName = intent.getStringExtra("nickname");
//          userName = "Guest" + new Random().nextInt(5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatReference.removeEventListener(mChildEventListener);
    }

    @Override
    public void onClick(View v) {
        String message = mEdtMessage.getText().toString();
        String formattedDate = simpleDateFormat.format(calendar.getTime());
        if (!TextUtils.isEmpty(message)) {
            mEdtMessage.setText("");
            ChatData chatData = new ChatData();
            chatData.senderEmail = mAuth.getCurrentUser().getEmail();
            chatData.message = message;
            chatData.time = formattedDate;
            mChatReference.push().setValue(chatData);
        }
    }
}

