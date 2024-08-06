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

public class LoginActivity extends AppCompatActivity {

    private EditText loginUserIdEditText, loginPasswordEditText;
    private Button buttonLogin, buttonGoToRegister;
    private ApiService apiService;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUserIdEditText = findViewById(R.id.loginUserIdEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonGoToRegister = findViewById(R.id.buttonGoToRegister);

        apiService = ApiClient.getClient().create(ApiService.class);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EncryptAndLoginTask().execute();
            }
        });

        buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private class EncryptAndLoginTask extends AsyncTask<Void, Void, PublicKey> {

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
                loginUser(publicKey);
            } else {
                Toast.makeText(LoginActivity.this, "Failed to get public key from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginUser(PublicKey publicKey) {
        try {
            // 비밀번호를 암호화합니다.
            String secNum = loginPasswordEditText.getText().toString().trim();
            byte[] encryptedSecNum = RSAUtil.encrypt(secNum, publicKey);
            String encryptedSecNumString = Base64.encodeToString(encryptedSecNum, Base64.NO_WRAP);

            // AppUser 객체 생성
            AppUser appUser = new AppUser();
            appUser.setUserId(loginUserIdEditText.getText().toString().trim());
            appUser.setSecNum(encryptedSecNumString);

            // 서버로 전송
            apiService.loginUser(appUser).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Map<String, Object> result = response.body();
                        if (result != null && "Login successful".equals(result.get("message"))) {
                            Log.d(TAG, "Login successful");
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            // 메인 페이지로 이동
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userId", result.get("userId").toString());
                            intent.putExtra("nam", result.get("nam").toString());
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "Login failed");
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Login failed: " + response.message());
                        Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Log.e(TAG, "Error: " + t.getMessage(), t);
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
