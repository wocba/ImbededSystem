package com.wocba.imbededsystem.ContentDetail;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by jinwo on 2017-12-04.
 */

public class ContentDetailAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    String lati;
    String longi;


    public ContentDetailAdapter(FragmentManager fragmentManager, int mNumOfTabs, String lati, String longi)
    {
        super(fragmentManager);
        this.mNumOfTabs = mNumOfTabs;

        this.lati = lati;
        this.longi = longi;
    }




    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();

        switch (position) {
            case 0:
                ContentDetailMapFrag contentDetailMapFrag = new ContentDetailMapFrag();
                args.putString("lati", lati);
                args.putString("longi", longi);
                contentDetailMapFrag.setArguments(args);
                return contentDetailMapFrag;
            case 1:
                ContentDetailImageFrag contentDetailImageFrag = new ContentDetailImageFrag();
                contentDetailImageFrag.setArguments(args);
                return contentDetailImageFrag;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
