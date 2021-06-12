package com.example.friendsup.models;

import java.util.List;

public class RegisteredUser extends User  {
    public int age;
    public int height;

    public String hairColor;
    public String eyesColor;
    public String about;
    public String userPhoto;
    public String instagramProfile;
    public String facebookProfile;
    public String vkProfile;
    public List<String> userLiked;

    public List<String> getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(List<String> userLiked) {
        this.userLiked = userLiked;
    }

    public RegisteredUser(String about, int age, String eyesColor, String hairColor, int height, String userPhoto, String username, String vkProfile, String facebookProfile, String instagramProfile, List<String> userLiked) {
        super(username);
        this.about = about;
        this.age = age;
        this.eyesColor = eyesColor;
        this.hairColor = hairColor;
        this.height = height;
        this.userPhoto = userPhoto;
        this.vkProfile = vkProfile;
        this.facebookProfile = facebookProfile;
        this.instagramProfile = instagramProfile;
        this.userLiked = userLiked;
    }

    public RegisteredUser(String username) {
        super(username);
    }

    public RegisteredUser(String username, String password, String gender) {
        super(username, password, gender);
    }

    public void setInstagramProfile(String instagramProfile) {
        this.instagramProfile = instagramProfile;
    }

    public void setFacebookProfile(String facebookProfile) {
        this.facebookProfile = facebookProfile;
    }

    public void setVkProfile(String vkProfile) {
        this.vkProfile = vkProfile;
    }

    public String getInstagramProfile() {
        return instagramProfile;
    }

    public String getFacebookProfile() {
        return facebookProfile;
    }

    public String getVkProfile() {
        return vkProfile;
    }

    public RegisteredUser(String username, int age, String hairColor, String eyesColor, String about, String userPhoto, String instagramProfile, String facebookProfile, String vkProfile) {
        super(username);
        this.about = about;
        this.hairColor = hairColor;
        this.eyesColor = eyesColor;
        this.userPhoto = userPhoto;
        this.instagramProfile = instagramProfile;
        this.facebookProfile = facebookProfile;
        this.vkProfile = vkProfile;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public void setEyesColor(String eyesColor) {
        this.eyesColor = eyesColor;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public String getHairColor() {
        return hairColor;
    }

    public String getEyesColor() {
        return eyesColor;
    }

    public String getAbout() {
        return about;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
}