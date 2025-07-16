package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    ImageView imgAvatar, imgQRCode;
    TextView txtUsername;
    Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ View
        imgAvatar = findViewById(R.id.imgAvatar);
        imgQRCode = findViewById(R.id.imgQRCode);
        txtUsername = findViewById(R.id.txtUsername);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Gán tên người dùng (có thể lấy từ SharedPreferences hoặc API)
        txtUsername.setText("Admin");

        // Xử lý sự kiện Đăng xuất
        btnSignOut.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng xuất...", Toast.LENGTH_SHORT).show();

            // Xóa thông tin đăng nhập trong SharedPreferences
            SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear(); // Xóa tất cả
            boolean success = editor.commit();
            if (!success) {
                Toast.makeText(this, "Lỗi khi xóa dữ liệu đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            // Quay lại màn hình đăng nhập và clear toàn bộ activity stack
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
