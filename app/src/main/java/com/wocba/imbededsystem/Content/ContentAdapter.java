package com.wocba.imbededsystem.Content;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wocba.imbededsystem.Data.InfoClass;

import java.util.ArrayList;

/**
 * Created by jinwo on 2017-10-18.
 */

public class ContentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<InfoClass> mItems;


    public ContentAdapter(Context c, ArrayList<InfoClass> array){
        mContext = c;
        mItems = array;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertview, ViewGroup parent) {
        ContentView itemView;
        if (convertview == null) {
            itemView = new ContentView(mContext, mItems.get(position));
        } else {
            itemView = (ContentView) convertview;
//            itemView.setIcon(mItems.get(position).getIcon());
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(2, mItems.get(position).getData(4));
        }
        return itemView;
    }

    public void setArrayList(ArrayList<InfoClass> arrays){
        this.mItems = arrays;
    }

    public ArrayList<InfoClass> getArrayList(){
        return mItems;
    }

}
