package com.example.friendsup.models;

public class MessengerPagination {

    public String id;
    public String jwt;
    public int pagination;
    public int messagesWrote;

    public MessengerPagination(int pagination, int messagesWrote, String jwt, String id) {
        this.pagination = pagination;
        this.messagesWrote = messagesWrote;
        this.jwt = jwt;
        this.id = id;
    }
}
