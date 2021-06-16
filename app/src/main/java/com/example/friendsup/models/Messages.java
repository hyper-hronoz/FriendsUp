package com.example.friendsup.models;

import java.util.ArrayList;
import java.util.List;

public class Messages {
    Object[] messages;


    public Messages(Object[] sections) {
        this.messages = sections;
    }

    public int getSize() {
        return this.messages.length;
    }

    public Object getObject(int i) {
       return messages[i];
    }
}
