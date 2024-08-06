package com.example.androidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidtest.R;

public class MainActivity extends AppCompatActivity {

    private TextView textWelcome, textUserInfo;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textWelcome = findViewById(R.id.textWelcome);
        textUserInfo = findViewById(R.id.textUserInfo);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Intent로 전달받은 사용자 정보
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String nam = intent.getStringExtra("nam");

        // 사용자 정보 표시
        textUserInfo.setText("User ID: " + userId + "\nNam: " + nam);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃: LoginActivity로 이동하고 MainActivity 종료
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
