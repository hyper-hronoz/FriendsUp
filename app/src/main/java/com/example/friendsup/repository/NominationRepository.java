package com.example.friendsup.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.friendsup.api.JSONPlaceHolderApi;
import com.example.friendsup.models.RegisteredUser;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NominationRepository {
    public MutableLiveData<RegisteredUser> user;

    public NominationRepository(MutableLiveData<RegisteredUser> user) {
        this.user = user;
    }

    public void getDataFromRemotes() {
        JSONPlaceHolderApi jsonPlaceHolderApi = Network.getJSONPalaceHolderAPI();

        Call<RegisteredUser> call = jsonPlaceHolderApi.findUser("Bearer " + Network.getJWT());

        call.enqueue(new Callback<RegisteredUser>() {

            @Override
            public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                Log.d("Search status code is", String.valueOf(response.code()));
                Log.d("Search response body is", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                if (response.code() == 200) {
                    user.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                Log.e("Search nomination error", t.getMessage());
            }
        });
    }
}
