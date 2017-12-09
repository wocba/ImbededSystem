package com.wocba.imbededsystem.Main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-12-08.
 */

public class ErrorDialog extends Dialog {

    private String mErrorMessage;
    private View.OnClickListener mErrorListener;
    private TextView mErrorContent;
    private Button mErrorBtn;

//    static {
//        System.loadLibrary("jniExample");
//    }
//
//    public native String getJNIString();
//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errordialog);
        mErrorContent = (TextView)findViewById(R.id.error_content);
        mErrorBtn = (Button)findViewById(R.id.btn_error);
        mErrorBtn.setOnClickListener(mErrorListener);
        mErrorContent.setText(mErrorMessage);
    }

    public ErrorDialog(Context context, View.OnClickListener errorListener, String errorMessage){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mErrorListener = errorListener;
        this.mErrorMessage = errorMessage;
    }
}
