package com.tunisianfood_advisor.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Avis {
    @SerializedName("total")
    @Expose
    private String avis;

    public Avis(String avis) {
        this.avis = avis;
    }

    public String getAvis() {
        return avis;
    }

}
