package com.example.friendsup.repository;

import com.example.friendsup.API.JSONPlaceHolderApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkAction {
    public Retrofit initializeRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(NetworkConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public JSONPlaceHolderApi initializeApi(Retrofit retrofit) {
        return retrofit.create(JSONPlaceHolderApi.class);
    }
}
