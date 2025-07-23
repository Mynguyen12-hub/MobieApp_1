package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtFullName, edtNewUsername, edtEmail, edtNewPassword, edtPhone, edtAddress;
    CheckBox cbAgreeTerms;
    Button btnSignUp, btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các EditText
        edtFullName = findViewById(R.id.edtFullName);
        edtNewUsername = findViewById(R.id.edtNewUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        cbAgreeTerms = findViewById(R.id.cbAgreeTerms);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnSignUp.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String username = edtNewUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtNewPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            // Kiểm tra dữ liệu đầu vào
            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!cbAgreeTerms.isChecked()) {
                Toast.makeText(this, "Bạn cần đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSignUp.setEnabled(false);

            // Tạo đối tượng User
            User user = new User(fullName, username, email, password);
            user.setPhone(phone);
            user.setAddress(address);

            // Gửi dữ liệu lên MockAPI
            UserApi userApi = ApiClient.getClient().create(UserApi.class);
            Call<User> call = userApi.registerUser(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    btnSignUp.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "🎉 Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "❌ Lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                        Log.e("RegisterError", "Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    btnSignUp.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "⚠ Không thể kết nối server", Toast.LENGTH_LONG).show();
                    Log.e("RegisterFail", t.getMessage(), t);
                }
            });
        });

        btnBackToLogin.setOnClickListener(v -> finish());
    }
}
