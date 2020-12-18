package com.tunisianfood_advisor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("nom")
    @Expose
    private String mProductName;
    @SerializedName("description")
    @Expose
    private String mProductDescription;
    @SerializedName("restaurant")
    @Expose
    private String mProductRestaurant;
    @SerializedName("prix")
    @Expose
    private String mProductValue;

    public Product(String mProductName, String mProductDescription, String mProductRestaurant, String mProductValue) {
        this.mProductName = mProductName;
        this.mProductDescription = mProductDescription;
        this.mProductRestaurant = mProductRestaurant;
        this.mProductValue = mProductValue;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmProductDescription() {
        return mProductDescription;
    }

    public String getmProductRestaurant() {
        return mProductRestaurant;
    }

    public String getmProductValue() {
        return mProductValue;
    }
}
