package com.wocba.imbededsystem.Content;

import android.os.Bundle;

import com.wocba.imbededsystem.Common.BaseActivity;
import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-10-18.
 */

public class ContentActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mNavigationView.getMenu().getItem(1).setChecked(true);
    }
}
