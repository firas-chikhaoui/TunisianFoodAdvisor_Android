package com.tunisianfood_advisor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("id")
    @Expose
    private int placeId;
    @SerializedName("nom")
    @Expose
    private String placeName;
    @SerializedName("adresse")
    @Expose
    private String location;
    @SerializedName("rating")
    @Expose
    private String placeRating;
    @SerializedName("delivery")
    @Expose
    private String placeDelivery;

    @SerializedName("favoris")
    @Expose
    private int favorite;

    public Place(int placeId, String placeName, String location, String placeRating, String placeDelivery, int favorite) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.location = location;
        this.placeRating = placeRating;
        this.placeDelivery = placeDelivery;
        this.favorite = favorite;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getLocation() {
        return location;
    }

    public String getRating() {
        return placeRating;
    }

    public String getDelivery() {
        return placeDelivery;
    }

    public int isFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
