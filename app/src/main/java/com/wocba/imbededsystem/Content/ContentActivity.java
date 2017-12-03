package com.wocba.imbededsystem.Content;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wocba.imbededsystem.Common.BaseActivity;
import com.wocba.imbededsystem.ContentDetail.ContentDetailActivity;
import com.wocba.imbededsystem.Data.DbOpenHelper;
import com.wocba.imbededsystem.Data.InfoClass;
import com.wocba.imbededsystem.R;

import java.util.ArrayList;

/**
 * Created by jinwo on 2017-10-18.
 */

public class ContentActivity extends BaseActivity{
    private ListView listView2;
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
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoClass curItem = (InfoClass) adapter.getItem(position);
                String[] data = curItem.getData();

                Intent intent = new Intent(getApplicationContext(), ContentDetailActivity.class);

                Toast.makeText(getApplicationContext(), ""  + data[0] + "," + data[4] , Toast.LENGTH_SHORT).show();

                intent.putExtra("name", data[0]);
                intent.putExtra("content", data[4]);
                intent.putExtra("lati", data[2]);
                intent.putExtra("longi", data[3]);

                createBackStack(intent);
            }
        });
    }
    private void doWhileCursorToArray() {

        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        while (mCursor.moveToNext()) {
            mInfoClass = new InfoClass(this,
                    mCursor.getString(mCursor.getColumnIndex("_id")),
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
