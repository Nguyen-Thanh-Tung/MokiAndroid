package com.coho.moki.adapter.product;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coho.moki.R;
import com.coho.moki.data.model.ProductComment;
import com.coho.moki.data.remote.ProductCommentResponse;
import com.coho.moki.util.Utils;
import com.coho.moki.util.network.LoadImageUtils;

import java.util.List;

/**
 * Created by Khanh Nguyen on 10/13/2017.
 */

public class ProductCommentAdapter extends ArrayAdapter<ProductCommentResponse> {

    private static final String TAG = "ProductCommentAdapter";

    LayoutInflater mLayoutInflater;
    List<ProductCommentResponse> mProductCommentList;

    public ProductCommentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ProductCommentResponse> objects) {
        super(context, resource, objects);
        mLayoutInflater = LayoutInflater.from(context);
        this.mProductCommentList = objects;
    }

    /**
     * Return a view: view item in ListView
     * @param position position in the product list
     * @param convertView is new view inflate from xml layout, or scrapview in case reuse
     * @param parent parent view, in this case is ListView
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.product_comment_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindItem(viewHolder, position);
        return convertView;
    }

    private void bindItem(ViewHolder viewHolder, int position) {
        ProductCommentResponse comment = mProductCommentList.get(position);
        ProductCommentResponse.Poster poster = comment.getPoster();
        viewHolder.txtName.setText(poster.getName());
        LoadImageUtils.loadImageFromUrl(poster.getAvatar(), R.drawable.unknown_user, viewHolder.imgAvatar, null);
        viewHolder.txtComment.setText(comment.getComment());
        viewHolder.txtTime.setText(Utils.formatTime(comment.getCreated()));
        viewHolder.iconClock.setImageResource(R.drawable.icon_clock);
    }

    // using ViewHolder pattern to optimize performance of ListView
    private static class ViewHolder {
        private TextView txtName;
        private ImageView imgAvatar;
        private TextView txtComment;
        private ImageView iconClock;
        private TextView txtTime;

        private ViewHolder(View view) {
            txtName = view.findViewById(R.id.txtName);
            imgAvatar = view.findViewById(R.id.imgAvatar);
            txtComment = view.findViewById(R.id.txtComment);
            iconClock = view.findViewById(R.id.icon_clock);
            txtTime = view.findViewById(R.id.txtTime);
        }

    }

}
