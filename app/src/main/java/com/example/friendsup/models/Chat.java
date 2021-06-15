package com.example.friendsup.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chat {
    public String username;
    public String userPhoto;

    public Chat(String username, String userPhoto, String id) {
        this.username = username;
        this.userPhoto = userPhoto;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getId() {
        return id;
    }

    public Chat(String username, String id) {
        this.username = username;
        this.id = id;
    }

    @SerializedName("_id")
    public String id;


}
