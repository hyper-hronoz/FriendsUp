package com.example.friendsup.models;

public class MessengerPagination {

    public int pagination;
    public int messagesWrote;
    public String jwt;
    public String id;

    public MessengerPagination(int pagination, int messagesWrote, String jwt, String id) {
        this.pagination = pagination;
        this.messagesWrote = messagesWrote;
        this.jwt = jwt;
        this.id = id;
    }
}
