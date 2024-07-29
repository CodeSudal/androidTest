package com.example.androidtest.ui;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidtest.R;
import com.example.androidtest.api.ApiClient;
import com.example.androidtest.api.ApiService;
import com.example.androidtest.model.MyResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<MyResponse>> call = apiService.getPosts();

        call.enqueue(new Callback<List<MyResponse>>() {
            @Override
            public void onResponse(Call<List<MyResponse>> call, Response<List<MyResponse>> response) {
                if (response.isSuccessful()) {
                    List<MyResponse> data = response.body();
                    postAdapter = new PostAdapter(data);
                    recyclerView.setAdapter(postAdapter);
                } else {
                    Log.e(TAG, "Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MyResponse>> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, "Request failed: " + t.getMessage());
            }
        });
    }
}
