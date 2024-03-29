package com.example.friendsup.fragments.authorization.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.friendsup.R;
import com.example.friendsup.models.JwtToken;
import com.example.friendsup.models.User;
import com.example.friendsup.repository.Network;
import com.example.friendsup.utils.FormValidator;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText passwordEditText1;
    private Button confirmPasswordsbutton;
    private EditText passwordEditText2;

    public UserPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPasswordFragment newInstance(String param1, String param2) {
        UserPasswordFragment fragment = new UserPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void checkPassword(View v) {
        if (new FormValidator(getActivity().getApplicationContext(), this.passwordEditText1, "password").isStrokeIncorrect(false).isContainsNumbers().isLetters().commit()) {
            System.out.println(this.passwordEditText1.getText().toString() + " " + this.passwordEditText2.getText().toString());
            if (this.passwordEditText1.getText().toString().equals(this.passwordEditText2.getText().toString())) {
                this.passwordEditText1.setBackgroundResource(R.drawable.edit_text_login_sign_up);
                this.passwordEditText2.setBackgroundResource(R.drawable.edit_text_login_sign_up);
                registrateUser(v);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                this.passwordEditText1.setBackgroundResource(R.drawable.edit_text_error);
                this.passwordEditText2.setBackgroundResource(R.drawable.edit_text_error);
            }
        } else {
            this.passwordEditText1.setBackgroundResource(R.drawable.edit_text_error);
            this.passwordEditText2.setBackgroundResource(R.drawable.edit_text_error);
        }
    }

    private void registrateUser(View v) {
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.userRegistration), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String jsonUser = sharedPref.getString(getString(R.string.userRegistration), "");
        if (jsonUser != "") {
            Gson gson = new Gson();
            User user = gson.fromJson(jsonUser, User.class);
            user.setPassword(this.passwordEditText2.getText().toString());
            System.out.println(gson.toJson(user));
            editor.putString(getString(R.string.userRegistration), gson.toJson(user));
            editor.commit();
//            Navigation.findNavController(v).navigate(R.id.userPassword);

            Call<JwtToken> call = Network.getJSONPalaceHolderAPI().registerUser(user);

            call.enqueue(new Callback<JwtToken>() {
                @Override
                public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                    System.out.println(response);
                    if (response.code() == 409) {
                    } else if (response.code() == 422) {
                        Toast.makeText(getActivity().getApplicationContext(), "Пользователь с таким email уже существует", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 200) {

                        Toast.makeText(getActivity().getApplicationContext(), response.message().toString(), Toast.LENGTH_LONG).show();

                        Navigation.findNavController(v).navigate(R.id.action_userPassword_to_emailConfirmationFragment);

                        if (Build.VERSION.SDK_INT >= 11) {
                            getActivity().recreate();
                        } else {
                            getActivity().finish();
                            getActivity().startActivity(getActivity().getIntent());
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<JwtToken> call, Throwable t) {
                    Log.e("Sign up failre", t.getMessage());
                }
            });
        } else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_password, container, false);

        this.passwordEditText1 = (EditText) v.findViewById(R.id.password_edit_text_1);
        this.passwordEditText2 = (EditText) v.findViewById(R.id.password_edit_text_2);

        this.confirmPasswordsbutton  = (Button) v.findViewById(R.id.confirm_password_button);

        this.confirmPasswordsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword(v);
            }
        });

        return v;
    }
}