package com.coho.moki.adapter.customadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coho.moki.R;
import com.coho.moki.data.remote.ChatConsersation;
import com.coho.moki.data.remote.ListConversationResponceData;
import com.coho.moki.util.Utils;
import com.coho.moki.util.network.LoadImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 14/11/2017.
 */

public class NotificationAdapter extends ArrayAdapter<ChatConsersation> {

    LayoutInflater mLayoutInflater;
    List<ChatConsersation> mChats;

    public NotificationAdapter(@NonNull Context context, @LayoutRes int resource, List<ChatConsersation> chats) {
        super(context, resource);
        mLayoutInflater = LayoutInflater.from(context);

        if (chats != null && chats.size() > 0) {
            this.mChats = chats;
        } else {
            this.mChats = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return mChats.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.notification_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindItem(viewHolder, position);
        return convertView;
    }

    private void bindItem(ViewHolder viewHolder, int position) {
        ChatConsersation chat = mChats.get(position);
        LoadImageUtils.loadImageFromUrl(chat.product.image, R.drawable.unknown_user, viewHolder.imgAvatar, null);
        viewHolder.txtTitle.setText(chat.partner.username);
        viewHolder.txtMessContent.setText(chat.lastMessage.message);
        String sendAt = Utils.formatTime(chat.lastMessage.created);
        viewHolder.txtDate.setText(sendAt);
    }

    private static class ViewHolder {
        private TextView txtTitle;
        private ImageView imgAvatar;
        private TextView txtMessContent;
        private TextView txtDate;

        private ViewHolder(View view) {
            txtTitle = view.findViewById(R.id.txtTitle);
            imgAvatar = view.findViewById(R.id.imgAvatar);
            txtMessContent = view.findViewById(R.id.txtMessContent);
            txtDate = view.findViewById(R.id.txtDate);
        }

    }

    public void addItemsToLast(List<ChatConsersation> addItems) {
        if (addItems != null && addItems.size() > 0) {
            mChats.addAll(addItems);
            notifyDataSetChanged();
        }
    }

    public List<ChatConsersation> getChats() {
        return mChats;
    }

}
