package com.tunisianfood_advisor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Extra {

    @SerializedName("id")
    @Expose
    private int extraId;
    @SerializedName("nom")
    @Expose
    private String extraName;
    @SerializedName("prix")
    @Expose
    private String extraValue;
    private boolean isAdded = false;

    public Extra(int extraId, String extraName, String extraValue) {
        this.extraId = extraId;
        this.extraName = extraName;
        this.extraValue = extraValue;
    }

    public String getExtraName() {
        return extraName;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public int getExtraId() {
        return extraId;
    }

    public void addExtra(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
