package com.example.friendsup.fragments.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.friendsup.API.JSONPlaceHolderApi;
import com.example.friendsup.R;
import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.repository.NetworkAction;
import com.example.friendsup.utils.OnSwipeTouchListener;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Nominations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Nominations extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ScrollView nominationScrollView;
    private ImageView nominationPhoto;

    public Nominations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Nominations newInstance(String param1, String param2) {
        Nominations fragment = new Nominations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void getRandomUser() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
        String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

        Retrofit retrofit = new NetworkAction().initializeRetrofit();

        JSONPlaceHolderApi jsonPlaceHolderApi = new NetworkAction().initializeApi(retrofit);

        Log.d("JWT is" , JWTToken);

        Call<RegisteredUser> call = jsonPlaceHolderApi.findUser("Bearer " + JWTToken);

        call.enqueue(new Callback<RegisteredUser>() {
            @Override
            public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                Log.d("Search status code is", String.valueOf(response.code()));
                Log.d("Search response body is", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                if (response.code() == 401) {

                } else if (response.code() == 500) {
                    Toast.makeText(getContext(), "User cannot be found internal server error", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(response);
                }
            }
            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                Log.e("Search nominat error", t.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nominations, container, false);

        this.nominationScrollView = (ScrollView) v.findViewById(R.id.userScrollView);
        this.nominationPhoto = (ImageView) v.findViewById(R.id.userAvatar);

        this.nominationPhoto.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {

            }
        });

        this.nominationScrollView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
            }
        });

        getRandomUser();

        return v;
        // Inflate the layout for this fragment
    }
}