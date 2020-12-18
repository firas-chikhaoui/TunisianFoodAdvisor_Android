package com.tunisianfood_advisor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("nom")
    @Expose
    private String mCategoryName;
    @SerializedName("image")
    @Expose
    private int mCategoryImage;

    public Category(String mCategoryName, int mCategoryImage) {
        this.mCategoryName = mCategoryName;
        this.mCategoryImage = mCategoryImage;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getCategoryDrawable() {
        return mCategoryImage;
    }

}
