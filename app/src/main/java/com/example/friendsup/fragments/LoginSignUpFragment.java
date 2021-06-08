package com.example.friendsup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.friendsup.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginSignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginSignUpFragment extends Fragment {

    private View v;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button signUpButton;
    private Button loginButton;

    public LoginSignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginSignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginSignUpFragment newInstance(String param1, String param2) {
        LoginSignUpFragment fragment = new LoginSignUpFragment();
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


    private void clickListener() {
        this.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginSignUpFragment_to_userNameSername);
            }
        });

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginSignUpFragment_to_loginFragment);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_sign_up, container, false);

        this.v = v;

        this.signUpButton = v.findViewById(R.id.signUpButton);
        this.loginButton = v.findViewById(R.id.loginButton);

        clickListener();


        return v;
    }
}