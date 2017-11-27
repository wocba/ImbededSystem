package com.wocba.imbededsystem.Content;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.wocba.imbededsystem.Common.BaseActivity;
import com.wocba.imbededsystem.Data.DbOpenHelper;
import com.wocba.imbededsystem.Data.InfoClass;
import com.wocba.imbededsystem.R;

import java.util.ArrayList;

/**
 * Created by jinwo on 2017-10-18.
 */

public class ContentActivity extends BaseActivity{
    ListView listView2;
    private DbOpenHelper mDbOpenHelper;
    public Cursor mCursor;
    public InfoClass mInfoClass;
    public ArrayList<InfoClass> mInfoArray;
    public  ContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mNavigationView.getMenu().getItem(1).setChecked(true);
        listView2 = (ListView) findViewById (R.id.content_list);
        mDbOpenHelper = new DbOpenHelper(this);
        mInfoArray = new ArrayList<InfoClass>();
        doWhileCursorToArray();
        adapter = new ContentAdapter(this,mInfoArray);
        listView2.setAdapter(adapter);
    }
    private void doWhileCursorToArray() {

        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        while (mCursor.moveToNext()) {
            mInfoClass = new InfoClass(this,
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("name")),
                    mCursor.getString(mCursor.getColumnIndex("image")),
                    mCursor.getString(mCursor.getColumnIndex("lati")),
                    mCursor.getString(mCursor.getColumnIndex("longi")),
                    mCursor.getString(mCursor.getColumnIndex("content"))
            );
            mInfoArray.add(mInfoClass);
        }
        mCursor.close();
    }

}
