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
    String image;


    public ContentDetailAdapter(FragmentManager fragmentManager, int mNumOfTabs, String lati, String longi, String image)
    {
        super(fragmentManager);
        this.mNumOfTabs = mNumOfTabs;

        this.lati = lati;
        this.longi = longi;
        this.image = image;
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
                args.putString("image",image);
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
