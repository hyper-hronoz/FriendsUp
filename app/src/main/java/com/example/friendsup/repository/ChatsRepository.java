package com.example.friendsup.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.friendsup.api.JSONPlaceHolderApi;
import com.example.friendsup.models.Chat;
import com.example.friendsup.models.JwtToken;
import com.example.friendsup.models.RegisteredUser;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsRepository {
    private MutableLiveData<List<Chat>> chats;


    public ChatsRepository(MutableLiveData<List<Chat>> chats) {
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

    public void addUserChatsToRemotes(RegisteredUser registeredUser) {
        JSONPlaceHolderApi jsonPlaceHolderApi = Network.getJSONPalaceHolderAPI();

        Call<JwtToken> call = jsonPlaceHolderApi.createChatRoom("Bearer " + Network.getJWT(), registeredUser);

        call.enqueue(new Callback<JwtToken>() {

            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                Log.d("Add chat status code", String.valueOf(response.code()));
                if (response.code() == 200) {
                    setUserChatsToLocals();
                }
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.e("Error adding new chat", t.getMessage());
            }
        });
    }

    public void getUserChatsFromLocals() {

    }

    public void setUserChatsToLocals() {

    }
}
