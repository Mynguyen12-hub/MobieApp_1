package com.example.nguyenthimynguyen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    EditText txtFullName, txtUsername, txtEmail, txtPhone, txtAddress;
    Button btnSave;
    SharedPreferences prefs;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Ánh xạ view
        txtFullName = findViewById(R.id.txtFullName);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        btnSave = findViewById(R.id.btnSave);

        prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        userId = prefs.getString("id", null);

        if (userId == null) {
            Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchUserInfo(userId);

        btnSave.setOnClickListener(v -> updateUserInfo());
    }

    private void fetchUserInfo(String userId) {
        UserApi userApi = ApiClient.getClient().create(UserApi.class);
        userApi.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    txtFullName.setText(user.getFullName());
                    txtUsername.setText(user.getUsername());
                    txtEmail.setText(user.getEmail());
                    txtPhone.setText(user.getPhone());
                    txtAddress.setText(user.getAddress());
                } else {
                    Toast.makeText(UserInfoActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
        String fullName = txtFullName.getText().toString();
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();
        String address = txtAddress.getText().toString();

        // Tạo user mới (dùng để update)
        User updatedUser = new User(fullName, txtUsername.getText().toString(), email, "");
        updatedUser.setPhone(phone);
        updatedUser.setAddress(address);

        UserApi userApi = ApiClient.getClient().create(UserApi.class);
        userApi.updateUser(userId, updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserInfoActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserInfoActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
