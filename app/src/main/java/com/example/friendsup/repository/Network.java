package com.example.friendsup.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.friendsup.api.JSONPlaceHolderApi;
import com.example.friendsup.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// singleton pattern create once use later
public class Network {
    private static Retrofit retrofit;
    private static JSONPlaceHolderApi jsonPlaceHolderApi;
    private static String JWT;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static JSONPlaceHolderApi getJSONPalaceHolderAPI() {
        if (jsonPlaceHolderApi == null) {
            jsonPlaceHolderApi = getRetrofit().create(JSONPlaceHolderApi.class);
        }
        return jsonPlaceHolderApi;
    }

    public static String getJWT(Context context) {
        if (JWT == null) {
            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
            JWT = sharedPref.getString(context.getString(R.string.JWTToken), "");
            return JWT;
        }
        return JWT;
    }

    public static String getJWT() {
        if (JWT == null) {
            return "";
        }
        return JWT;
    }

    public static void deleteJWT(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.JWTToken), "");
        JWT = "";
        editor.clear();
        editor.commit();
        getJWT();
    }

    public static void setJWT(Context context, String jwt) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("JWT toke from login", jwt);
        editor.putString(context.getString(R.string.JWTToken), jwt);
        editor.commit();
        JWT = jwt;
    }
}
