package com.tunisianfood_advisor.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("phoneNum")
    @Expose
    private String phoneNum;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("historique_search")
    @Expose
    private String historique;
    @SerializedName("salt")
    @Expose
    private String salt;


    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getHistorique() {
        return historique;
    }

    public void setHistorique(String historique) {
        this.historique = historique;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
