package com.example.friendsup.fragments.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendsup.API.JSONPlaceHolderApi;
import com.example.friendsup.R;
import com.example.friendsup.models.Chat;
import com.example.friendsup.models.Contact;
import com.example.friendsup.repository.NetworkAction;
import com.example.friendsup.ui.ContactsAdapter;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessangerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessangerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ContactsAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvContacts;

    public MessangerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Messanger.
     */
    // TODO: Rename and change types and number of parameters
    public static MessangerFragment newInstance(String param1, String param2) {
        MessangerFragment fragment = new MessangerFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messanger, container, false);
//
        this.rvContacts = (RecyclerView) v.findViewById(R.id.rvContacts);

        getUserChats();

        return v;
    }

    private void setCurrentUserChatRooms(List<Chat> chats) {

        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(chats, getActivity());
        // Attach the adapter to the recyclerview to populate items
        this.rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        this.rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));

        System.out.println(chats);
    }

    private void getUserChats() {

        Retrofit retrofit = new NetworkAction().initializeRetrofit();

        JSONPlaceHolderApi jsonPlaceHolderApi = new NetworkAction().initializeApi(retrofit);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);

        String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

        Call<List<Chat>> call = jsonPlaceHolderApi.getUsersChatRooms("Bearer " + JWTToken);

        call.enqueue(new Callback<List<Chat>>() {
            private Context context = getActivity().getApplicationContext();

            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                Log.d("Search status code is", String.valueOf(response.code()));
                Log.d("Search response body is", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                if (response.code() == 200) {
                    setCurrentUserChatRooms(response.body());
                } else if (response.code() == 500) {
                    Toast.makeText(context, "User cannot be found internal server error", Toast.LENGTH_SHORT).show();
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Log.e("Search nominat error", t.getMessage());
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}