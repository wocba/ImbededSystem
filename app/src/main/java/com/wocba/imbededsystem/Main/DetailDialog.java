package com.wocba.imbededsystem.Main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-11-30.
 */

public class DetailDialog extends Dialog {

    private View.OnClickListener mDetailChatListener;
    private View.OnClickListener mDetailCancelListenr;
    private Button mChatButton;
    private Button mCancelButton;
    private String mName;
    private String mContent;
    private TextView mTextName;
    private TextView mTextContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaildialog);
        mChatButton = (Button)findViewById(R.id.btn_chat);
        mCancelButton = (Button)findViewById(R.id.btn_back);
        mChatButton.setOnClickListener(mDetailChatListener);
        mCancelButton.setOnClickListener(mDetailCancelListenr);
        mTextName = (TextView)findViewById(R.id.text_detail_name);
        mTextContent = (TextView)findViewById(R.id.text_detail_content);
        mTextName.setText(mName);
        mTextContent.setText(mContent);
    }

    public DetailDialog(Context context, View.OnClickListener detailChatListener, View.OnClickListener detailCancelListener, String name, String content){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mDetailChatListener = detailChatListener;
        this.mDetailCancelListenr = detailCancelListener;
        this.mName = name;
        this.mContent = content;
    }
}
