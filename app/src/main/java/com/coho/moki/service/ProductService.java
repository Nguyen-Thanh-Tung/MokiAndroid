package com.coho.moki.service;

import android.net.Uri;

import com.coho.moki.data.remote.CheckNewItemResponse;
import com.coho.moki.data.remote.GetListProductResponceData;
import com.coho.moki.data.remote.MyLikeResponseData;
import com.coho.moki.data.remote.ProductResponseData;
import com.coho.moki.data.remote.UserProductResponseData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by trung on 17/10/2017.
 */

public interface ProductService {

    void getListProduct(String token, String categoryId, String campaignId, String lastId,
                        String index, int count, final ResponseListener<GetListProductResponceData> listener);

    void getProductOfUser(String token, int index, int count, String userId, String keyword, String categoryId,
                          final ResponseListener<ArrayList<UserProductResponseData>> listener);

    public void getMyLikeProduct(String token, int index, int count, final ResponseListener<List<MyLikeResponseData>> listener);

    void checkNewItem(String lastId, String categoryId, final ResponseListener<CheckNewItemResponse> listener);
    public void addProduct(String token, String name, int price, String productSizeId, String brandId, String categoryId,
                           List<Uri> image, Uri video, Uri thumb, String described, String shipsFrom, List<Integer> shipsFromId,
                           int condition, List<Integer> dimension, String weight, final ResponseListener<ProductResponseData> listener);
}
