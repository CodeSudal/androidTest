package com.example.androidtest.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.androidtest.util.RSAUtil;
import java.security.PublicKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Map;

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
                new EncryptAndRegisterTask().execute();
            }
        });

        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class EncryptAndRegisterTask extends AsyncTask<Void, Void, PublicKey> {

        @Override
        protected PublicKey doInBackground(Void... voids) {
            try {
                return RSAUtil.getServerPublicKey();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(PublicKey publicKey) {
            if (publicKey != null) {
                registerUser(publicKey);
            } else {
                Toast.makeText(RegisterActivity.this, "Failed to get public key from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(PublicKey publicKey) {
        try {
            // 비밀번호를 암호화합니다.
            String secNum = secNumEditText.getText().toString().trim();
            byte[] encryptedSecNum = RSAUtil.encrypt(secNum, publicKey);
            String encryptedSecNumString = Base64.encodeToString(encryptedSecNum, Base64.NO_WRAP);

            // AppUser 객체 생성
            AppUser appUser = new AppUser();
            appUser.setUserId(userIdEditText.getText().toString().trim());
            appUser.setSecNum(encryptedSecNumString);
            appUser.setNam(namEditText.getText().toString().trim());

            // 서버로 전송
            apiService.registerUser(appUser).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Map<String, Object> result = response.body();
                        if (result != null && "User registered successfully".equals(result.get("message"))) {
                            Log.d(TAG, "Registration successful");
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            // 로그인 페이지로 이동
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "Registration failed");
                            Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Registration failed: " + response.message());
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Log.e(TAG, "Error: " + t.getMessage(), t);
                    Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
