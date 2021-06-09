package com.example.friendsup.API;

import com.example.friendsup.models.NetworkServiceResponse;
import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JSONPlaceHolderApi {
//    @POST("/auth/registration")
//    Call<NetworkServiceResponse> registerUser(@Body User user);

    @POST("/auth/login")
    Call<NetworkServiceResponse> loginUser(@Body User user);
//
//    @GET("/auth/user")
//    Call<RegisteredUser> getUserData(@Header("Authorization") String token);

//    @PUT("/auth/user")
//    Call<RegisteredUser> updateUserData(@Header("Authorization") String token, @Body RegisteredUser registeredUser);
//
//    @POST("/auth/upload")
//    Call<UploadImage> postImage(@Header("Authorization") String token, @Body UploadImage uploadImage);
//
    @GET("/find/user")
    Call<RegisteredUser> findUser(@Header("Authorization") String token);
//
//    @POST("/action/like")
//    Call<RegisteredUser> likeUser(@Header("Authorization") String token, @Body RegisteredUser registeredUser);
//
//    @GET("/action/liked")
//    Call<RegisteredUsers> getLikedUsers(@Header("Authorization") String token);
//
//    @Multipart
//    @POST("upload")
//    Call<RequestBody> uploadImage(@Part MultipartBody.Part part, @Part("somedata") RequestBody requestBody);
}
