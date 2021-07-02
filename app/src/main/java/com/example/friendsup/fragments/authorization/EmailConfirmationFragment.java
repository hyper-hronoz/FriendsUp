package com.example.friendsup.fragments.authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendsup.MainActivity;
import com.example.friendsup.R;
import com.example.friendsup.models.JwtToken;
import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.models.User;
import com.example.friendsup.repository.Network;
import com.example.friendsup.repository.NetworkConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmailConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailConfirmationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView confirmationMessage;
    private Button confirmedButton;
    private Button notReceive;

    public EmailConfirmationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmailConfirmationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmailConfirmationFragment newInstance(String param1, String param2) {
        EmailConfirmationFragment fragment = new EmailConfirmationFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_email_confirmation, container, false);

        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.userRegistration), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String jsonUser = sharedPref.getString(getString(R.string.userRegistration), "");

        User user = new Gson().fromJson(jsonUser, User.class);

        this.confirmationMessage = (TextView) v.findViewById(R.id.confirmation_message);

        this.confirmationMessage.setText("We've sent you a confirmation email " + user.getEmail() + " \nplease confirm it to continue");

        checkIsConfirmed(v, user);

        this.confirmedButton = (Button) v.findViewById(R.id.email_confirmed);

        this.notReceive = (Button) v.findViewById(R.id.do_not_receive);

        this.notReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNotReceive(v, user);
            }

        });

        this.confirmedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsConfirmed(v, user);
            }
        });

        return v;
    }

    private void doNotReceive(View v, User user) {
        Call<JwtToken> call = Network.getJSONPalaceHolderAPI().registerUser(user);

        call.enqueue(new Callback<JwtToken>() {
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                System.out.println(response);
                if (response.code() == 409) {
                } else if (response.code() == 401) {
                } else if (response.code() == 200) {
                    Toast.makeText(getActivity().getApplicationContext(), "We have sent you an email!", Toast.LENGTH_LONG).show();
                } else {
                }
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.e("Sign up failre", t.getMessage());
            }
        });
    }

    private void checkIsConfirmed(View v, User user) {

        Call<JwtToken> call = Network.getJSONPalaceHolderAPI().loginUser(user);

        System.out.println(NetworkConfig.BASE_URL);

        call.enqueue(new Callback<JwtToken>() {
            Context context = getActivity().getApplicationContext();
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                Log.d("Login status code is", String.valueOf(response.code()));
                Log.d("Login response body is", String.valueOf(response.body()));

                System.out.println("Message is: " + response.message());

                if (response.code() == 400) {
                    Toast.makeText(context, "Что то пошло не так попробуй заново", Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.code() == 403) {
                    Toast.makeText(context, "Email не подтвержден, confirm it please to continue", Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.code() == 404) {
                    Toast.makeText(context, "User is not exists try register again", Toast.LENGTH_LONG).show();
                    return;
                }
                String jwt = new GsonBuilder().setPrettyPrinting().create().toJson(response.body().getToken()).replaceAll("^.|.$", "");
                if (jwt == "" || jwt == null) {
                    return;
                }

                Network.setJWT(context, jwt);

                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Connection error try again leter", Toast.LENGTH_LONG).show();
                Log.e("Login error:", t.getMessage());
            }
        });

    }
}