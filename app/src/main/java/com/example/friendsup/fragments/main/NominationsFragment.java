package com.example.friendsup.fragments.main;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.friendsup.ChatActivity;
import com.example.friendsup.MainActivity;
import com.example.friendsup.R;
import com.example.friendsup.api.JSONPlaceHolderApi;
import com.example.friendsup.models.JwtToken;
import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.repository.Network;
import com.example.friendsup.utils.OnSwipeTouchListener;
import com.example.friendsup.viewmodel.NominationViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.example.friendsup.utils.OnSwipeTouchListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NominationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NominationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ScrollView nominationScrollView;
    private ImageView nominationPhoto;
    private TextView nominationHeadingTextView;
    private TextView nominationAboutTextView;
    private LinearLayout profileButtonLayout;
    private ConstraintLayout constraintLayout;
    private Toolbar toolbar;

    private boolean isAnimated = false;
    private int screenHeight;
    private int screenWidth;
    private ImageButton chatButton;


    public NominationsFragment() {
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
    public static NominationsFragment newInstance(String param1, String param2) {
        NominationsFragment fragment = new NominationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRandomUser();
    }

    private void getRandomUser() {
        NominationViewModel model = new ViewModelProvider(getActivity()).get(NominationViewModel.class);

        model.getUser().observe(getActivity(), user -> {
            setCurrentUserData(user);
        });
    }

    private void setCurrentUserData(RegisteredUser registeredUser) {
        System.out.println(registeredUser);
        this.nominationHeadingTextView.setText(registeredUser.getUsername());
        this.nominationAboutTextView.setText(registeredUser.getAbout());

        try {
            Glide.with(getActivity().getApplicationContext()).load(registeredUser.getUserPhoto()).into(this.nominationPhoto);
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }

        this.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChat(registeredUser);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nominations, container, false);

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        this.nominationScrollView = (ScrollView) v.findViewById(R.id.userScrollView);
        this.nominationPhoto = (ImageView) v.findViewById(R.id.userAvatar);

        this.nominationHeadingTextView = (TextView) v.findViewById(R.id.profile_heading);
        this.nominationAboutTextView = (TextView) v.findViewById(R.id.profile_about);

        this.nominationPhoto.setBackgroundResource(R.drawable.default_photo);
//        this.profileButtonLayout = (LinearLayout) v.findViewById(R.id.profile_button_layout);
        this.chatButton = (ImageButton) v.findViewById(R.id.fragment_nominations__message_button);


//        compressScrollViewHeight();
//        this.constraintLayout = (ConstraintLayout) getActivity().findViewById(R.id.nomination_constraint);

        v.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {
            public void onSwipeLeft() {
                getRandomUser();
            }
        });

        this.nominationPhoto.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {
            public void onSwipeTop() {
//                hideProfileImage();
            }

            public void onSwipeBottom() {
                 // showProfileImage();
            }
        });
////
        this.nominationScrollView.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {
            public void onSwipeLeft() {
                getRandomUser();
            }

            public void onSwipeTop() {
//                hideProfileImage();
            }
        });

        return v;
        // Inflate the layout for this fragment
    }

    private void startChat(RegisteredUser registeredUser) {

        JSONPlaceHolderApi jsonPlaceHolderApi = Network.getJSONPalaceHolderAPI();

        Call<JwtToken> call = jsonPlaceHolderApi.createChatRoom("Bearer " + Network.getJWT(getActivity().getApplicationContext()), registeredUser);

        call.enqueue(new Callback<JwtToken>() {
            private Context context = getActivity().getApplicationContext();

            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                Toast.makeText(getActivity().getApplicationContext(), "All done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
                System.out.println("id is: " + registeredUser.getId());
                intent.putExtra(getActivity().getApplicationContext().getString(R.string.chatActivity), registeredUser.getId());
                getActivity().startActivity(intent);
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.e("Search nominat error", t.getMessage());
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hideProfileImage() {
        if (!this.isAnimated) {
            System.out.println("Hiding");

            Animation profileImageAnimation = new AnimationUtils().loadAnimation(getActivity().getApplicationContext(), R.anim.hide_profile_image_animation);
            Animation profileButtonLayoutAnimation = new AnimationUtils().loadAnimation(getActivity().getApplicationContext(), R.anim.button_hide_layout_profile_animation);
            Animation profileScrollViewLayoutAnimation = new AnimationUtils().loadAnimation(getActivity().getApplicationContext(), R.anim.scrollview_profile_animation);
            this.nominationPhoto.startAnimation(profileImageAnimation);
            this.profileButtonLayout.startAnimation(profileButtonLayoutAnimation);
            this.nominationScrollView.setAnimation(profileScrollViewLayoutAnimation);

            increaseScrollViewHeight();
            this.isAnimated = !this.isAnimated;
        }
    }

    private void showProfileImage() {
        if (this.isAnimated) {
            System.out.println("SHowing");
            Animation profileImageAnimation = new AnimationUtils().loadAnimation(getActivity().getApplicationContext(), R.anim.show_profile_image_animation);
            Animation profileButtonLayoutAnimation = new AnimationUtils().loadAnimation(getActivity().getApplicationContext(), R.anim.button_show_layout_profile_animation);
            this.nominationPhoto.startAnimation(profileImageAnimation);
            this.profileButtonLayout.startAnimation(profileButtonLayoutAnimation);
            compressScrollViewHeight();
        }
    }

    private void increaseHeight(ValueAnimator valueAnimator) {
        int val = (Integer) valueAnimator.getAnimatedValue();
        ViewGroup.LayoutParams layoutParams = this.nominationScrollView.getLayoutParams();
        layoutParams.height = val;
        this.nominationScrollView.setLayoutParams(layoutParams);
    }

    private void compressHeight(ValueAnimator valueAnimator)  {
        int val = (Integer) valueAnimator.getAnimatedValue();
        ViewGroup.LayoutParams layoutParams = this.nominationScrollView.getLayoutParams();
        layoutParams.height = val;
        this.nominationScrollView.setLayoutParams(layoutParams);
    }

    private void compressScrollViewHeight() {
        int number = (int) this.screenHeight - this.screenHeight / 2 - 20 - 63;
        ValueAnimator anim = ValueAnimator.ofInt(this.nominationScrollView.getMeasuredHeight(), number);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                compressHeight(valueAnimator);
            }
        });
        anim.setDuration(1000);
        anim.start();
        this.isAnimated = false;
    }

    private void increaseScrollViewHeight() {

        ValueAnimator anim = ValueAnimator.ofInt(this.nominationScrollView.getMeasuredHeight(), this.screenHeight - 56 * 8);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                increaseHeight(valueAnimator);
            }
        });
        anim.setDuration(1000);
        anim.start();
    }
}