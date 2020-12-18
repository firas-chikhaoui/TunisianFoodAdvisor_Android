package com.tunisianfood_advisor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("id")
    @Expose
    private int placeId;
    @SerializedName("nom")
    @Expose
    private String itemName;
    @SerializedName("description")
    @Expose
    private String itemDesc;
    @SerializedName("prix")
    @Expose
    private String itemPrice;

    public Item(String itemName, String itemDesc,String itemPrice) {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemPrice() {
        return itemPrice;
    }
}
