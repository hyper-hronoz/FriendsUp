package com.example.friendsup.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.friendsup.models.Chat;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryChats {
    MutableLiveData<List<Chat>> chats;


    public RepositoryChats(MutableLiveData<List<Chat>> chats) {
        this.chats = chats;
    }

    public void getUserChatsFromRemotes() {

        Call<List<Chat>> call = Network.getJSONPalaceHolderAPI().getUsersChatRooms("Bearer " + Network.getJWT());

        call.enqueue(new Callback<List<Chat>>() {

            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                Log.d("Chats code is", String.valueOf(response.code()));
                Log.d("Chats response body is", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                chats.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Log.e("Chats error", t.getMessage());
            }
        });
    }

    public void getUserChatsFromLocals() {

    }
}
