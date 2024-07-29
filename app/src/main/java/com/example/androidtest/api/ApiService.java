package com.example.androidtest.api;

import com.example.androidtest.model.MyResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("posts")
    Call<List<MyResponse>> getPosts();
}