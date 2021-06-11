package com.example.friendsup.fragments.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.friendsup.R;
import com.example.friendsup.utils.FormValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEmailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText email_edit_text;
    private Button email_confirm_button;

    public UserEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserEmail.
     */
    // TODO: Rename and change types and number of parameters
    public static UserEmailFragment newInstance(String param1, String param2) {
        UserEmailFragment fragment = new UserEmailFragment();
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

    private void validateEmail(View v) {
        if (new FormValidator(getActivity().getApplicationContext(), this.email_edit_text, "email").isValidEmail().commit()) {
            Navigation.findNavController(v).navigate(R.id.userPassword);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_email, container, false);

        this.email_edit_text = (EditText) v.findViewById(R.id.email_edit_text);
        this.email_confirm_button = (Button) v.findViewById(R.id.confirm_email_button);

        this.email_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail(v);
            }
        });

        return v;
    }
}