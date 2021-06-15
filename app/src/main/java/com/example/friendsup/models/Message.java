package com.example.friendsup.models;

public class Message {
    public String message;
    public String username;

    public Message(String message, String username) {
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
