package com.wocba.imbededsystem.ContentDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-12-04.
 */

public class ContentDetailImageFrag extends Fragment{
    private Bundle bundle;

//    private String menu1;
//    private String menu1p;
//    private String menu2;
//    private String menu2p;
//    private String menu3;
//    private String menu3p;
//
//    private TextView menuView1;
//    private TextView menuView2;
//    private TextView menuView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.detail_frag_2, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        bundle = getArguments();

//        menu1 = bundle.getString("menu1");
//        menu1p = bundle.getString("menu1p");
//
//        menu2 = bundle.getString("menu2");
//        menu2p = bundle.getString("menu2p");
//
//        menu3 = bundle.getString("menu3");
//        menu3p = bundle.getString("menu3p");
//
//        menuView1 = (TextView)getActivity().findViewById(R.id.menu_1);
//        menuView2 = (TextView)getActivity().findViewById(R.id.menu_2);
//        menuView3= (TextView)getActivity().findViewById(R.id.menu_3);
//
//        menuView1.setText(menu1 + " :   " + menu1p);
//        menuView2.setText(menu2 + " :   "  + menu2p);
//        menuView3.setText(menu3 + " :   "  + menu3p);

        super.onActivityCreated(savedInstanceState);

    }

}
