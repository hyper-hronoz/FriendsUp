package com.example.friendsup.models;

import com.google.gson.annotations.SerializedName;

public class ImageMessage {
    @SerializedName("username")
    public String username;

    public String image;

    public ImageMessage(String username, String image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;

    }
}
