package com.example.androidtest.api;

import com.example.androidtest.model.AppUser;
import com.example.androidtest.model.MyResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    /*
    @GET("posts")
    Call<List<MyResponse>> getPosts();
     */

    @POST("/api/users/register")
    Call<MyResponse> registerUser(@Body AppUser appUser);
}