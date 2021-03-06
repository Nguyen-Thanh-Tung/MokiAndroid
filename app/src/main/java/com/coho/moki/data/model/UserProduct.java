package com.coho.moki.data.model;

import java.util.List;

/**
 * Created by nguyenthanhtung on 15/11/2017.
 */

public class UserProduct {

    private String productId;

    private String name;

    private List<String> imageUrls;

    private int price;

    private int pricePercent;

    private int isLiked;

    private int banned;

    private String description;

    private int numLike;

    private int numComment;

    public UserProduct(String productId, String name, List<String> imageUrls, int price, int pricePercent, int isLiked, int banned, String description, int numLike, int numComment) {
        this.productId = productId;
        this.name = name;
        this.imageUrls = imageUrls;
        this.price = price;
        this.pricePercent = pricePercent;
        this.isLiked = isLiked;
        this.banned = banned;
        this.description = description;
        this.numLike = numLike;
        this.numComment = numComment;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPricePercent() {
        return pricePercent;
    }

    public void setPricePercent(int pricePercent) {
        this.pricePercent = pricePercent;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumLike() {
        return numLike;
    }

    public void setNumLike(int numLike) {
        this.numLike = numLike;
    }

    public int getNumComment() {
        return numComment;
    }

    public void setNumComment(int numComment) {
        this.numComment = numComment;
    }
}
