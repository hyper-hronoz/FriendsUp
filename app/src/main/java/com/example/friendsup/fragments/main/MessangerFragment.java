package com.example.friendsup.fragments.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendsup.API.JSONPlaceHolderApi;
import com.example.friendsup.R;
import com.example.friendsup.ViewModel.ChatsViewModel;
import com.example.friendsup.models.Chat;
import com.example.friendsup.repository.Network;
import com.example.friendsup.ui.ContactsAdapter;
import com.google.gson.GsonBuilder;

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

    private TextView haveNoMessages;

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
        View v = inflater.inflate(R.layout.fragment_messanger, container, false);

        this.rvContacts = (RecyclerView) v.findViewById(R.id.rvContacts);

        this.haveNoMessages = (TextView) v.findViewById(R.id.noMessages);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChatsViewModel model = new ViewModelProvider(getActivity()).get(ChatsViewModel.class);

        model.getChats().observe(getActivity(), chats -> {
            if (chats.size() == 0) {
                return;
            }
            setCurrentUserChatRooms(chats);
        });
    }

    private void setCurrentUserChatRooms(List<Chat> chats) {
        if (chats.size() != 0) {
            this.haveNoMessages.setVisibility(View.GONE);
        }

        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(chats, getActivity());
        // Attach the adapter to the recyclerview to populate items
        this.rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        this.rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));

        System.out.println(chats);
    }
}