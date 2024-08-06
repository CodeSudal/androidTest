package com.example.androidtest.api;

import com.example.androidtest.model.AppUser;
import com.example.androidtest.model.LoginRequest;
import com.example.androidtest.model.MyResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.Map;

public interface ApiService {

    @POST("/api/users/register")
    Call<Map<String, Object>> registerUser(@Body AppUser appUser);

    @POST("/api/users/login")
    Call<Map<String, Object>> loginUser(@Body AppUser user);

    @GET("/api/publicKey")
    Call<String> getPublicKey();
}