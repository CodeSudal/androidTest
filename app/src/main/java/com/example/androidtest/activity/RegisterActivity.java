package com.example.androidtest.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidtest.R;
import com.example.androidtest.api.ApiClient;
import com.example.androidtest.api.ApiService;
import com.example.androidtest.model.AppUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText userIdEditText, secNumEditText, nameEditText;
    private Button registerButton;
    private ApiService apiService;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userIdEditText = findViewById(R.id.userIdEditText);
        secNumEditText = findViewById(R.id.secNumEditText);
        nameEditText = findViewById(R.id.nameEditText);
        registerButton = findViewById(R.id.registerButton);

        apiService = ApiClient.getClient().create(ApiService.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String userId = userIdEditText.getText().toString().trim();
        String secNum = secNumEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();

        if (userId.isEmpty() || secNum.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AppUser appUser = new AppUser();
        appUser.setUserId(userId);
        appUser.setSecNum(secNum);
        appUser.setName(name);

        Log.d(TAG, "Sending register request: " + appUser);

        apiService.registerUser(appUser).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "User registered successfully: " + response.body());
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Registration failed: " + response.message());
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
