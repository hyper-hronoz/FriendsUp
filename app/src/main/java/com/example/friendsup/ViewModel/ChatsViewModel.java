package com.example.friendsup.ViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.friendsup.models.Chat;
import com.example.friendsup.repository.ChatsRepository;

import java.util.List;

public class ChatsViewModel extends ViewModel {
    private MutableLiveData<List<Chat>> chats;
    ChatsRepository chatsRepository;

    public ChatsViewModel() {
        chatsRepository = new ChatsRepository(chats);
    }

    public LiveData<List<Chat>> getChats() {
        if (chats == null) {
            chats = new MutableLiveData<List<Chat>>();
            loadChats();
        }

        return chats;
    }

    private void loadChats() {
        chatsRepository.getUserChatsFromRemotes();
    }
}
