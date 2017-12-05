package com.wocba.imbededsystem.Main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wocba.imbededsystem.R;



/**
 * Created by jinwo on 2017-12-06.
 */

public class ContentDialog extends Dialog {

    private View.OnClickListener mContentListener;
    private Button mConfirmButton;
    public EditText mEditContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentdialog);

        mConfirmButton = (Button)findViewById(R.id.btn_confirm);
        mEditContent = (EditText)findViewById(R.id.edit_content);
        mConfirmButton.setOnClickListener(mContentListener);


    }

    public ContentDialog(Context context, View.OnClickListener contentListener){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContentListener = contentListener;
    }
}
