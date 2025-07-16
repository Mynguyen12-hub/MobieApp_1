package com.example.nguyenthimynguyen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    TextView txtUsername, txtFullName, txtEmail, txtGender, txtBirthDate;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        txtUsername = findViewById(R.id.txtUsername);
        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtGender = findViewById(R.id.txtGender);
        txtBirthDate = findViewById(R.id.txtBirthDate);

        prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = prefs.getString("id", null);

        if (userId == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchUserInfo(userId);
    }

    private void fetchUserInfo(String userId) {
        UserApi userApi = ApiClient.getClient().create(UserApi.class);
        userApi.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    txtUsername.setText(user.getUsername());
                    txtFullName.setText(user.getFullName());
                    txtEmail.setText(user.getEmail());
                } else {
                    Toast.makeText(UserInfoActivity.this, "Không thể tải thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
