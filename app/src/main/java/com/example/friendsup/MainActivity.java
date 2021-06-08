package com.example.friendsup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
        String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

        System.out.println("JWT is found " + JWTToken);


        if (JWTToken != "") {
            setContentView(R.layout.activity_main_main);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            NavController navController = Navigation.findNavController(this,  R.id.main_fragment_container);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);


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