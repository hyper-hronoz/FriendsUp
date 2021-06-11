package com.example.friendsup.fragments.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.friendsup.R;
import com.example.friendsup.models.User;
import com.example.friendsup.utils.FormValidator;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserNameSernameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserNameSernameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText lastNameEditText;
    private EditText nameEditText;
    private String lastName;
    private String name;
    private Button buttonNext;

    public UserNameSernameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserNameSername.
     */
    // TODO: Rename and change types and number of parameters
    public static UserNameSernameFragment newInstance(String param1, String param2) {
        UserNameSernameFragment fragment = new UserNameSernameFragment();
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

    public boolean isFormValid() {
        boolean isValid = true;
        if (!(new FormValidator(getActivity().getApplicationContext(), this.nameEditText, "name").setMin(2).isValidLength().isNotContainsNumbers().isLetters().commit())) {
            isValid = false;
        }
        if (!(new FormValidator(getActivity().getApplicationContext(), this.lastNameEditText, "lastname")).setMin(2).isValidLength().isContainsNumbers().isLetters().commit()) {
            isValid = false;
        }
        return isValid;
    }

    public void saveNameAndLastName(View v) {
        User user = new User(this.name + " " + this.lastName);
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String userJSON = gson.toJson(user);
        editor.putString(getString(R.string.userRegistration), userJSON);
        editor.commit();
        Navigation.findNavController(v).navigate(R.id.userEmail);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_user_name_sername, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        this.nameEditText = (EditText) v.findViewById(R.id.name_edit_text);

        this.lastNameEditText = (EditText) v.findViewById(R.id.sername_edit_text);

        this.buttonNext = (Button) v.findViewById(R.id.confirm_sername_name);

        this.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    saveNameAndLastName(v);
                }
            }
        });

        return v;
    }
}