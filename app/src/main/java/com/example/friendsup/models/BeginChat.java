package com.example.friendsup.models;

public class BeginChat {
    public String jwt;
    public String id;

    public BeginChat(String jwt, String id) {
        this.jwt = jwt;
        this.id = id;
    }
}
