package com.example.friendsup.API;

import com.example.friendsup.models.NetworkServiceResponse;
import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.models.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JSONPlaceHolderApi {
    @POST("/auth/registration")
    Call<NetworkServiceResponse> registerUser(@Body User user);

    @POST("/auth/login")
    Call<NetworkServiceResponse> loginUser(@Body User user);
//
    @GET("/user-data/user")
    Call<RegisteredUser> getCurrentUserData(@Header("Authorization") String token);

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
    @Multipart
    @POST("/user-data/upload-image")
    Call<RequestBody> uploadImage(@Part MultipartBody.Part part, @Header("Authorization") String token);
}
