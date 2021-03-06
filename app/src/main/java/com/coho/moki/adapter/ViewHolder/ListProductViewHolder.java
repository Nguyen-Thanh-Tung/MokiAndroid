package com.coho.moki.adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coho.moki.R;
import com.coho.moki.callback.OnClickProductItemListenner;
import com.coho.moki.callback.OnLoadImageListener;
import com.coho.moki.data.model.Product;
import com.coho.moki.ui.custom.SquareImageView;
import com.coho.moki.util.network.LoadImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trung on 16/10/2017.
 */

public class ListProductViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.first_image)
    ImageView mFirstImage;

    @BindView(R.id.name)
    TextView mTxtName;

    @BindView(R.id.like)
    TextView mTxtLike;

    @BindView(R.id.likeIcon)
    ImageView mImgLikeIcon;

    @BindView(R.id.comment)
    TextView mTxtComment;

    @BindView(R.id.price)
    TextView mTxtPrice;

    @BindView(R.id.frameProgress)
    FrameLayout mFrameProgress;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.ll_product_item_small)
    LinearLayout mLLProductItemSmall;

    @BindView(R.id.sale_off_view_container)
    RelativeLayout mRLSaleOff;

    @BindView(R.id.salePercent)
    TextView mTxtSalePercent;

    OnClickProductItemListenner mListener;
    String mProductId;

    public ListProductViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initListener();
    }

    public void populate(Product product){
        mProductId = product.getProductId();

        mTxtName.setText(product.getName());
        mTxtLike.setText(product.getNumLike() + "");
        Log.d("trunglike", product.getIsLiked() + " " + product.getName());
        if (product.getIsLiked() == 1){

            mImgLikeIcon.setImageResource(R.drawable.grid_heart_on);
        }
        else {
            mImgLikeIcon.setImageResource(R.drawable.grid_heart_off);
        }

        mTxtComment.setText(product.getNumComment() + "");
        mTxtPrice.setText(product.getPrice() + "");
        mFrameProgress.setVisibility(View.VISIBLE);

        if (product.getPricePercent() == 0){
            mRLSaleOff.setVisibility(View.GONE);
        }
        else {
            mTxtSalePercent.setText(product.getPricePercent() + "");
        }

        String firstImageUrl = "";
        if (product.getImageUrls() != null && product.getImageUrls().size() > 0) {
            firstImageUrl = product.getImageUrls().get(0);
        }

        if (firstImageUrl != null && firstImageUrl != ""){
            LoadImageUtils.loadImageFromUrl(firstImageUrl, R.drawable.no_image, mFirstImage, new OnLoadImageListener() {
                @Override
                public void onSuccess() {
                    mFrameProgress.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }



    }

    public void setListener(OnClickProductItemListenner listener){
        mListener = listener;
    }

    private void initListener(){
        mLLProductItemSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(mProductId);
            }
        });
    }
}
