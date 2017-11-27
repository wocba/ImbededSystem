package com.wocba.imbededsystem.Data;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by jinwo on 2017-10-18.
 */

public class InfoClass {
    private Drawable mIcon;
    private String[] mData;
    private boolean mSelectable = true;

    public int _id;

    public InfoClass(){}

    public InfoClass(Context context, int _id, String name, String image, String lati, String longi, String content){
        mData = new String[5];
        this._id = _id;
        mData[0] = name;
        mData[1] = image;
        mData[2] = lati;
        mData[3] = longi;
        mData[4] = content;

 //       Assert.assertNotNull(context);

 //       Resources res = context.getResources();
//        Drawable icon = res.getDrawable(context.getResources().getIdentifier(image,"drawable",context.getPackageName()));
 //       mIcon = icon;
    }


    public boolean isSelectable() {
        return mSelectable;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    public Drawable getIcon() {
        return mIcon;
    }
    /**
     * Get data array
     *
     * @return
     */
    public String[] getData() {
        return mData;
    }

    /**
     * Get data
     */
    public String getData(int index) {
        if (mData == null || index >= mData.length) {
            return null;
        }

        return mData[index];
    }


    /**
     * Set data array
     *
     * @param obj
     */
    public void setData(String[] obj) {
        mData = obj;
    }

    public int compareTo(InfoClass other) {
        if (mData != null) {
            String[] otherData = other.getData();
            if (mData.length == otherData.length) {
                for (int i = 0; i < mData.length; i++) {
                    if (!mData[i].equals(otherData[i])) {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException();
        }

        return 0;
    }
}
