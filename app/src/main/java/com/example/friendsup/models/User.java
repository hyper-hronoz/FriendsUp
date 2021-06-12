package com.example.friendsup.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("gender")
    public String gender;
    @SerializedName("email")
    public String email;

    public User(String username, String password, String gender) {
        this.username = username;
        this.password = password;
        this.gender = gender;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String password, String gender, String email) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
