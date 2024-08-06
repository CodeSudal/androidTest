package com.example.androidtest.api;

import com.example.androidtest.model.AppUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    /*
    @GET("posts")
    Call<List<MyResponse>> getPosts();
     */

    @POST("/api/users/register")
    Call<String> registerUser(@Body AppUser appUser);
}