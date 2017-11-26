package com.wocba.imbededsystem.Content;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wocba.imbededsystem.Data.InfoClass;
import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-10-18.
 */

public class ContentView extends LinearLayout {

    private ImageView mIcon;
    private TextView mText01;
    private TextView mText02;
    private TextView mText03;

    public ContentView(Context context, InfoClass aItem){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mainlayout_onecol, this, true); // 리스트뷰 한 조각을 인플레이션

        // Set Icon
        mIcon = (ImageView) findViewById(R.id.imageicon);
        mIcon.setImageDrawable(aItem.getIcon());

        // Set Text 01
        mText01 = (TextView) findViewById(R.id.name);
        mText01.setText(aItem.getData(0));

        // Set Text 03
        mText03 = (TextView) findViewById(R.id.content);
        mText03.setText(aItem.getData(4));

    }

    public void setText(int index, String data) {
        if (index == 0) {
            mText01.setText(data);
        } else if (index == 1) {
            mText02.setText(data);
        }else if (index == 2) {
            mText03.setText(data);
        }else
            throw new IllegalArgumentException();
    }

    public void setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
    }
}

