package com.example.nguyenthimynguyen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtUsername;
    private EditText edtFullName, edtEmail, edtPhone, edtAddress, edtSkinType;
    private Button btnUpdateProfile;
    private UserApi userApi;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ các view từ layout
        txtUsername = findViewById(R.id.txtUsername);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtSkinType = findViewById(R.id.edtSkinType);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        userApi = ApiClient.getClient().create(UserApi.class);

        // Load thông tin người dùng từ API
        loadUserInfoFromApi();

        // Xử lý sự kiện nhấn nút cập nhật thông tin
        btnUpdateProfile.setOnClickListener(v -> updateUserInfo());
    }

    private void loadUserInfoFromApi() {
        // Lấy username đang đăng nhập từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String loggedUsername = prefs.getString("username", null);

        if (loggedUsername == null) {
            Log.e("ProfileActivity", "Không tìm thấy username trong SharedPreferences");
            return;
        }

        Log.d("ProfileActivity", "Đang đăng nhập với username: " + loggedUsername);

        // Gọi API lấy danh sách user
        userApi.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (User user : response.body()) {
                        if (user.getUsername().equals(loggedUsername)) {
                            userId = user.getId();

                            // Hiển thị thông tin user lên các view
                            txtUsername.setText(user.getUsername());
                            edtFullName.setText(user.getFullName());
                            edtEmail.setText(user.getEmail());
                            edtPhone.setText(user.getPhone());
                            edtAddress.setText(user.getAddress());
                            edtSkinType.setText(user.getSkinType());

                            Log.d("ProfileActivity", "Tìm thấy user: " + user.getFullName());
                            break;
                        }
                    }
                } else {
                    Log.e("ProfileActivity", "API trả về rỗng hoặc lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("ProfileActivity", "Lỗi khi gọi API getAllUsers", t);
                Toast.makeText(ProfileActivity.this, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
        if (userId == null) {
            Toast.makeText(this, "Không thể cập nhật: thiếu userId", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng User mới chứa dữ liệu cập nhật
        User user = new User();
        user.setId(userId); // gửi id trong body đề phòng backend cần
        user.setUsername(txtUsername.getText().toString()); // gửi username
        user.setFullName(edtFullName.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setAddress(edtAddress.getText().toString());
        user.setSkinType(edtSkinType.getText().toString());

        // Gọi API cập nhật user
        userApi.updateUser(userId, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ProfileActivity", "Cập nhật thất bại. Mã lỗi: " + response.code());
                    Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "Lỗi khi gọi updateUser", t);
            }
        });
    }
}
