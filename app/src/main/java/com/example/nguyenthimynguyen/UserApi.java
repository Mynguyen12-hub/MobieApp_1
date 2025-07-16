package com.example.nguyenthimynguyen;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("user")
    Call<List<User>> getAllUsers();

    @GET("user/{id}")
    Call<User> getUserById(@Path("id") String userId);

    @POST("user")
    Call<User> registerUser(@Body User user);
}
