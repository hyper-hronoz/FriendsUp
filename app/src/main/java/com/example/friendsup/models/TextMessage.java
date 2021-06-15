package com.example.friendsup.models;

import com.google.gson.annotations.SerializedName;

public class TextMessage {
    @SerializedName("message")
    public String message;
    @SerializedName("username")
    public String username;


    public TextMessage(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }
}
