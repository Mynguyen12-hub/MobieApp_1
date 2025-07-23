package com.example.nguyenthimynguyen;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserApi {

    @GET("user")
    Call<List<User>> getAllUsers();

    @GET("user/{id}")
    Call<User> getUserById(@Path("id") String id);

    @POST("user")
    Call<User> registerUser(@Body User user);

    @PUT("user/{id}")
    Call<User> updateUser(@Path("id") String userId, @Body User user);
}
