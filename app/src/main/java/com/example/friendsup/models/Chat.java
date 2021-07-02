package com.example.friendsup.models;

//import androidx.annotation.NonNull;
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

//@Entity
public class Chat {

//    @NonNull
//    @PrimaryKey
    @SerializedName("_id")
    public String id;

//    @ColumnInfo(name = "username")
    public String username;

//    @ColumnInfo(name = "userPhoto")
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

}
