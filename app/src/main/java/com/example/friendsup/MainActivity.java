package com.example.friendsup;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendsup.fragments.main.HomeFragment;
import com.example.friendsup.fragments.main.NominationsFragment;
import com.example.friendsup.fragments.main.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {
    private BottomNavigationView bottomNavigationView;
    private Notification.Action.Builder NavOptions;

    private void logout() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.main_top_menu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.top_menu_profile_settings:

                            case R.id.top_menu_profile_logout:
                                logout();
                        }
                        return true;
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
        String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

        System.out.println("JWT is found " + JWTToken);


        if (JWTToken != "") {
            setContentView(R.layout.activity_main_main);

            ImageView imageView = findViewById(R.id.toolbar_navigation_profile);

            imageView.setOnClickListener(viewClickListener);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit();

            BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
                private String currentFragment = "HOME";

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


                    switch (item.getItemId()) {
                        case R.id.bottom_navigation_home:
                            System.out.println(currentFragment);
                            fragmentTransaction.setCustomAnimations(
                                    R.anim.fragment_enter_animation_from_left_to_right,  // enter
                                    R.anim.fragment_exit_animation_from_left_to_right,
                                    R.anim.fragment_exit_animation_from_left_to_right,
                                    R.anim.fragment_exit_animation_from_left_to_right
                            );
                            this.currentFragment = "HOME";
                            fragment = new HomeFragment();
                            break;

                        case R.id.bottom_navigation_search:
                            System.out.println(currentFragment);
                            if (currentFragment == "NOTIFICATIONS") {
                                fragmentTransaction.setCustomAnimations(
                                        R.anim.fragment_enter_animation_from_left_to_right,  // enter
                                        R.anim.fragment_exit_animation_from_right_to_left,
                                        R.anim.fragment_exit_animation_from_right_to_left,
                                        R.anim.fragment_exit_animation_from_right_to_left
                                );
                            }
                            if (currentFragment == "HOME") {
                                fragmentTransaction.setCustomAnimations(
                                        R.anim.fragment_enter_animation_from_right_to_left,  // enter
                                        R.anim.fragment_exit_animation_from_left_to_right,
                                        R.anim.fragment_exit_animation_from_left_to_right,
                                        R.anim.fragment_exit_animation_from_left_to_right
                                );
                            }
                            this.currentFragment = "NOMINATIONS";
                            fragment = new NominationsFragment();
                            break;

                        case R.id.bottom_navigation_notifications:
                            System.out.println(currentFragment);
                            fragmentTransaction.setCustomAnimations(
                                    R.anim.fragment_enter_animation_from_right_to_left,  // enter
                                    R.anim.fragment_exit_animation_from_left_to_right,
                                    R.anim.fragment_exit_animation_from_left_to_right,
                                    R.anim.fragment_exit_animation_from_left_to_right
                            );
                            this.currentFragment = "NOTIFICATIONS";
                            fragment = new NotificationsFragment();
                            break;
                    }

                    fragmentTransaction.replace(R.id.main_fragment_container, fragment).commit();
                    return true;
                }
            };

            bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);


//            NavController navController = Navigation.findNavController(this,  R.id.main_fragment_container);

//            androidx.navigation.NavOptions.Builder navOptions = new NavOptions.Builder().setLaunchSingleTop(true).setEnterAnim(R.anim.android_animation_from_right_to_left);


//            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//                    when(item.itemId) {
//                R.id.fragmentFirst -> {
//                    navController.navigate(R.id.fragmentFirst,null,options)
//                }
//                R.id.fragmentSecond -> {
//                    navController.navigate(R.id.fragmentSecond,null,options)
//                }
//                R.id.fragmentThird -> {
//                    navController.navigate(R.id.fragmentThird,null,options)
//                }
//            }
//                true
//            }


//            NavController navController = Navigation.findNavController(this,  R.id.main_fragment_container);
//            NavigationUI.setupWithNavController(bottomNavigationView, navController);


//            this.bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//            BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment fragment = null;
//                    switch (item.getItemId()) {
//                        case R.id.bottom_navigation_home:
//                            fragment = new Home();
//                            break;
//
//                        case R.id.bottom_navigation_search:
//                            fragment = new Nominations();
//                            break;
//
//                        case R.id.bottom_navigation_notifications:
//                            fragment = new Notifications();
//                            break;
//                    }
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,fragment).commit();
//
//                    return true;
//                }
//            };
//
//            this.bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        } else {
            setContentView(R.layout.activity_main_authorization);
        }
    }

}