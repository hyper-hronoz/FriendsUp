package com.example.friendsup.models;

import java.util.ArrayList;

public class Contact {
    private String name;
    private String lastMessage;
    private boolean isOnline;

    public Contact(String name, String lastMessage, boolean online) {
        this.name = name;
        this.lastMessage = lastMessage;
        isOnline = online;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public static int getLastContactId() {
        return lastContactId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    private static int lastContactId = 0;

    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Contact("Alex Fish", "Hey, nice shoes where are...", true));
        }

        return contacts;
    }
}
