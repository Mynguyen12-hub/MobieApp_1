package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView txtUsername, txtSkinType;
    Button btnSignOut;
    LinearLayout itemOrder, itemPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUsername = findViewById(R.id.txtUsername);
        txtSkinType = findViewById(R.id.txtSkinType);
        btnSignOut = findViewById(R.id.btnSignOut);

        // ⚠️ Quan trọng: khởi tạo view chức năng
//        itemOrder = findViewById(R.id.itemOrder);
//        itemPrivacy = findViewById(R.id.itemPrivacy);

        // Lấy dữ liệu người dùng từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = prefs.getString("username", "Chưa đăng nhập");
        txtUsername.setText(username);

        // 👉 Mở trang đơn hàng
        itemOrder.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
        });

        // 👉 Mở trang thông tin người dùng
        itemPrivacy.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, UserInfoActivity.class));
        });

        // 👉 Đăng xuất
        btnSignOut.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }
}
