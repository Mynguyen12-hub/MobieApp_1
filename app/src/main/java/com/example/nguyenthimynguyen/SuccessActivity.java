package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        btnBackHome = findViewById(R.id.btnBackHome);

        btnBackHome.setOnClickListener(v -> goToHome());

        // Tự động chuyển về Home sau 3 giây
        new Handler().postDelayed(this::goToHome, 3000);
    }

    private void goToHome() {
        Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Đóng tất cả activity hiện tại để không thể quay lại màn hình trước
        finishAffinity();
    }
}
