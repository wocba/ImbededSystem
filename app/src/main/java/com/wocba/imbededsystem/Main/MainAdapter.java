package com.wocba.imbededsystem.Main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by jinwo on 2017-11-10.
 */

public class MainAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainAdapter(FragmentManager fragmentManager, int mNumOfTabs)
    {
        super(fragmentManager);
        this.mNumOfTabs = mNumOfTabs;
//        this.image = image;
//        this.lati = lati;
//        this.longi = longi;
//        this.menu1 = menu1;
//        this.menu1p = menu1p;
//        this.menu2 = menu2;
//        this.menu2p = menu2p;
//        this.menu3 = menu3;
//        this.menu3p = menu3p;
    }


    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();

        switch (position) {
            case 0:
                MainMapFrag mainMapFrag = new MainMapFrag();
//                args.putString("lati", lati);
//                args.putString("longi", longi);
                mainMapFrag.setArguments(args);
                return mainMapFrag;
            case 1:
                MainContentFrag mainContentFrag = new MainContentFrag();
//                args.putString("menu1",menu1);
//                args.putString("menu1p",menu1p);
//                args.putString("menu2",menu2);
//                args.putString("menu2p",menu2p);
//                args.putString("menu3",menu3);
//                args.putString("menu3p",menu3p);
                mainContentFrag.setArguments(args);
                return mainContentFrag;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
