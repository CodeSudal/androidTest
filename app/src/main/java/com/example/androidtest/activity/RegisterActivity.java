package com.example.androidtest.activity;

import android.content.Intent;
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
import com.example.androidtest.model.MyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText userIdEditText, secNumEditText, namEditText;
    private Button registerButton, buttonBackToLogin;
    private ApiService apiService;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userIdEditText = findViewById(R.id.userIdEditText);
        secNumEditText = findViewById(R.id.secNumEditText);
        namEditText = findViewById(R.id.namEditText);
        registerButton = findViewById(R.id.registerButton);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        apiService = ApiClient.getClient().create(ApiService.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String userId = userIdEditText.getText().toString().trim();
        String secNum = secNumEditText.getText().toString().trim();
        String nam = namEditText.getText().toString().trim();

        if (userId.isEmpty() || secNum.isEmpty() || nam.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AppUser appUser = new AppUser();
        appUser.setUserId(userId);
        appUser.setSecNum(secNum);  // secNum 필드에 비밀번호 설정
        appUser.setNam(nam);

        Log.d(TAG, "Sending register request: " + appUser);

        apiService.registerUser(appUser).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    MyResponse myResponse = response.body();
                    if (myResponse != null) {
                        Log.d(TAG, "User registered successfully: " + myResponse.getMessage());
                        Toast.makeText(RegisterActivity.this, "User registered successfully: " + myResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Response body is null");
                        Toast.makeText(RegisterActivity.this, "Registration failed: Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Registration failed: " + response.message());
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
