package com.wocba.imbededsystem.Main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-11-10.
 */

public class MainContentFrag extends Fragment{
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.main_frag_1, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        bundle = getArguments();

        super.onActivityCreated(savedInstanceState);
    }

}

