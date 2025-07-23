package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private EditText txtFullName, txtPhone, txtAddress, edtNote, edtVoucher;
    private TextView tvTotalAmount, tvSelectedPayment;
    private RecyclerView rvCheckoutProducts;
    private Button btnSelectPayment, btnConfirm;

    private List<Product> checkoutList = new ArrayList<>();
    private CheckoutProductAdapter adapter;

    private String selectedPayment = "";

    private static final int REQUEST_PAYMENT_METHOD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Ánh xạ view
        txtFullName = findViewById(R.id.txtFullName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        edtNote = findViewById(R.id.edtNote);
        edtVoucher = findViewById(R.id.edtVoucher);
        tvTotalAmount = findViewById(R.id.txtTotal);
        tvSelectedPayment = findViewById(R.id.tvSelectedPayment);
        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts);
        btnSelectPayment = findViewById(R.id.btnSelectPayment);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Lấy sản phẩm từ intent nếu là mua ngay
        Product buyNowProduct = (Product) getIntent().getSerializableExtra("buyNowProduct");

        if (buyNowProduct != null) {
            checkoutList.add(buyNowProduct);
        } else {
            checkoutList.addAll(CartManager.getSelectedItems());
        }

        // Hiển thị danh sách sản phẩm
        adapter = new CheckoutProductAdapter(checkoutList);
        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutProducts.setAdapter(adapter);

        // Tính tổng tiền
        updateTotalAmount();

        // Tải thông tin người dùng từ API
        loadUserInfoFromApi();

        // Chọn phương thức thanh toán
        btnSelectPayment.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, PaymentMethodActivity.class);
            startActivityForResult(intent, REQUEST_PAYMENT_METHOD);
        });

        // Xác nhận đặt hàng
        btnConfirm.setOnClickListener(v -> {
            if (validateInput()) {
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                if (buyNowProduct == null) {
                    CartManager.removeSelectedItems();
                }
                finish();
            }
        });
    }

    private void updateTotalAmount() {
        double total = 0;
        for (Product p : checkoutList) {
            total += p.getSalePrice() * p.getQuantity();
        }
        tvTotalAmount.setText(String.format("Tổng tiền: %,.0f đ", total));
    }

    private void loadUserInfoFromApi() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String loggedUsername = prefs.getString("username", null);

        Log.d("CheckoutActivity", "Username từ SharedPreferences: " + loggedUsername);

        if (loggedUsername == null) {
            Log.e("CheckoutActivity", "Không tìm thấy username trong SharedPreferences");
            return;
        }

        UserApi userApi = ApiClient.getClient().create(UserApi.class);
        userApi.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> userList = response.body();
                    Log.d("CheckoutActivity", "Số lượng user trả về: " + userList.size());

                    for (User user : userList) {
                        Log.d("CheckoutActivity", "User từ API: " + user.getUsername() + ", fullName: " + user.getFullName());

                        if (user.getUsername() != null && loggedUsername != null &&
                                user.getUsername().trim().equalsIgnoreCase(loggedUsername.trim())) {

                            Log.d("CheckoutActivity", "Tìm thấy user phù hợp: " + user.getFullName());

                            txtFullName.setText(user.getFullName());
                            txtPhone.setText(user.getPhone());
                            txtAddress.setText(user.getAddress());
                            return;
                        }
                    }

                    Log.e("CheckoutActivity", "Không tìm thấy user phù hợp với username: " + loggedUsername);
                } else {
                    Log.e("CheckoutActivity", "API trả về lỗi hoặc rỗng: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("CheckoutActivity", "Lỗi khi gọi API getAllUsers", t);
                Toast.makeText(CheckoutActivity.this, "Lỗi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput() {
        if (txtFullName.getText().toString().trim().isEmpty()) {
            txtFullName.setError("Vui lòng nhập họ tên");
            return false;
        }
        if (txtPhone.getText().toString().trim().isEmpty()) {
            txtPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        }
        if (txtAddress.getText().toString().trim().isEmpty()) {
            txtAddress.setError("Vui lòng nhập địa chỉ");
            return false;
        }
        if (selectedPayment.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PAYMENT_METHOD && resultCode == RESULT_OK && data != null) {
            selectedPayment = data.getStringExtra("selected_method");
            tvSelectedPayment.setText("Phương thức: " + selectedPayment);
        }
    }
}
