package com.example.friendsup.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.friendsup.API.JSONPlaceHolderApi;
import com.example.friendsup.MainActivity;
import com.example.friendsup.R;
import com.example.friendsup.fragments.other.FragmentLoader;
import com.example.friendsup.models.NetworkServiceResponse;
import com.example.friendsup.models.User;
import com.example.friendsup.repository.NetworkAction;
import com.example.friendsup.repository.NetworkConfig;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText loginEditText;
    private EditText passwordEditText;
    private String email;
    private String password;
    private Button loginButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

    private void login(View v) {

        this.email = this.loginEditText.getText().toString();
        this.password = this.passwordEditText.getText().toString();

        User user = new User(this.email, this.password);

        Retrofit retrofit = new NetworkAction().initializeRetrofit();

        JSONPlaceHolderApi jsonPlaceHolderApi = new NetworkAction().initializeApi(retrofit);

        Call<NetworkServiceResponse> call = jsonPlaceHolderApi.loginUser(user);

        System.out.println(NetworkConfig.BASE_URL);



        call.enqueue(new Callback<NetworkServiceResponse>() {
            @Override
            public void onResponse(Call<NetworkServiceResponse> call, Response<NetworkServiceResponse> response) {
                Log.d("Login status code is", String.valueOf(response.code()));
                Log.d("Login response body is", String.valueOf(response.body()));
                if (response.code() == 400) {
                    Toast.makeText(getActivity().getApplicationContext(), "Hei incorrect login or password", Toast.LENGTH_LONG).show();
                } else {
                    String jwt = new GsonBuilder().setPrettyPrinting().create().toJson(response.body().getResponse()).replaceAll("^.|.$", "");
                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    Log.d("JWT toke from login", jwt);
                    editor.putString(getString(R.string.JWTToken), jwt);
                    editor.commit();
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<NetworkServiceResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Connection error try again leter", Toast.LENGTH_LONG).show();
                Log.e("Login error:", t.getMessage());
            }
        });

    }

    private void listeners() {
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        this.loginEditText = v.findViewById(R.id.email_edit_text);
        this.passwordEditText = v.findViewById(R.id.password_edit_text);
        this.loginButton = v.findViewById(R.id.loginButton);

        listeners();

        return v;
    }
}