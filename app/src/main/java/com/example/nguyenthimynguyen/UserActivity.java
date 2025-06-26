package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail;
    Button btnUpdateProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String savedUsername = prefs.getString("username", "");
        String savedEmail = prefs.getString("email", "");

        edtUsername.setText(savedUsername);
        edtEmail.setText(savedEmail);

        // Xử lý cập nhật
        btnUpdateProfile.setOnClickListener(v -> {
            String newUsername = edtUsername.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newEmail)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", newUsername);
            editor.putString("email", newEmail);
            editor.apply();

            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        });

        // Xử lý đăng xuất
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        prefs.edit().clear().apply();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }
}
