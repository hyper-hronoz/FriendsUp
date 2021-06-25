package com.example.friendsup.models;

import com.google.gson.annotations.SerializedName;

public class JwtToken {
    @SerializedName("token")
    public String token;


    public JwtToken(String response) {
        this.token = response;
    }

    public String getToken() {
        return token;
    }
}
