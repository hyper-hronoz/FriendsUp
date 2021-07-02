package com.example.friendsup.api;

import com.example.friendsup.models.Chat;
import com.example.friendsup.models.JwtToken;
import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface JSONPlaceHolderApi {
    @POST("/auth/registration")
    Call<JwtToken> registerUser(@Body User user);

    @POST("/auth/login")
    Call<JwtToken> loginUser(@Body User user);
//
    @GET("/user-data/user")
    Call<RegisteredUser> getCurrentUserData(@Header("Authorization") String token);

    @PUT("/user-data/user")
    Call<RegisteredUser> updateCurrentUserData(@Header("Authorization") String token, @Body RegisteredUser registeredUser);
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

//    @POST("/auth/confirm")
//    Call<User> confirmEmail(User user);

    @GET("/messages/rooms")
    Call<List<Chat>> getUsersChatRooms(@Header("Authorization") String token);

    @POST("/messages/room")
    Call<JwtToken> createChatRoom(@Header("Authorization") String token, @Body RegisteredUser registeredUser);

    @Multipart
    @POST("/user-data/upload-image")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part part, @Header("Authorization") String token);
}
