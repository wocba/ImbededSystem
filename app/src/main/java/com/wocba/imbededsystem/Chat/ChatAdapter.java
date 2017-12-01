package com.wocba.imbededsystem.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wocba.imbededsystem.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Administrator on 2017-11-29.
 */

public class ChatAdapter extends ArrayAdapter<ChatData> {
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());

    public ChatAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_chatlistitem, null);

            viewHolder = new ViewHolder();
            viewHolder.mTxtUserName = (TextView) convertView.findViewById(R.id.txt_userName);
            viewHolder.mTxtMessage = (TextView) convertView.findViewById(R.id.txt_message);
            viewHolder.mTxtTime = (TextView) convertView.findViewById(R.id.txt_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChatData chatData = getItem(position);
        viewHolder.mTxtUserName.setText(chatData.userName);
        viewHolder.mTxtMessage.setText(chatData.message);
        viewHolder.mTxtTime.setText(mSimpleDateFormat.format(chatData.time));

        return convertView;
    }

    private class ViewHolder {
        private TextView mTxtUserName;
        private TextView mTxtMessage;
        private TextView mTxtTime;
    }
}
