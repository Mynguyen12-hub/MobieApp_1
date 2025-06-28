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
    Button btnOrders, btnServices, btnSettings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        imgAvatar = findViewById(R.id.imgAvatar);
        imgQRCode = findViewById(R.id.imgQRCode);
        txtUsername = findViewById(R.id.txtUsername);

        btnOrders = findViewById(R.id.btnMyOrder);
        btnServices = findViewById(R.id.btnMyService);
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);

        txtUsername.setText("Admin");

        btnOrders.setOnClickListener(v -> {
            Toast.makeText(this, "Đơn hàng của tôi", Toast.LENGTH_SHORT).show();
        });

        btnServices.setOnClickListener(v -> {
            Toast.makeText(this, "Dịch vụ của tôi", Toast.LENGTH_SHORT).show();
        });

        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng xuất...", Toast.LENGTH_SHORT).show();

            // Xóa dữ liệu đăng nhập trong SharedPreferences "user_data"
            SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear(); // hoặc editor.remove("isLoggedIn");
            boolean success = editor.commit();
            if (!success) {
                Toast.makeText(this, "Lỗi khi xóa dữ liệu đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển về màn hình đăng nhập và xóa hết activity cũ
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
