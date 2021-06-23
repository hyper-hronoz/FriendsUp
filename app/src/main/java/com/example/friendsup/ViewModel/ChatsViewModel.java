package com.example.friendsup.ViewModel;


import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.friendsup.models.Chat;
import com.example.friendsup.repository.RepositoryChats;

import java.util.ArrayList;
import java.util.List;

public class ChatsViewModel extends ViewModel {
    private MutableLiveData<List<Chat>> chats;

    public LiveData<List<Chat>> getChats() {
        if (chats == null) {
            chats = new MutableLiveData<List<Chat>>();
            loadChats();
        }

        return chats;
    }

    private void loadChats() {
        RepositoryChats repositoryChats = new RepositoryChats(chats);
        repositoryChats.getUserChatsFromRemotes();
    }
}
