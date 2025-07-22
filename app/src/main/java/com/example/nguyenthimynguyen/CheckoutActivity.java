package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;

import com.example.nguyenthimynguyen.CartManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

import okhttp3.*;

public class CheckoutActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtAddress, edtNote;
    TextView tvTotalAmount, tvSelectedPayment;
    Button btnConfirm, btnSelectPayment;
    RecyclerView rvCheckoutProducts;

    CheckoutProductAdapter adapter;
    List<Product> selectedProducts;
    String selectedMethod = "";

    private static final int REQUEST_PAYMENT = 101;

    // THAY URL CỦA GOOGLE SCRIPT CỦA BẠN TẠI ĐÂY:
    private static final String GOOGLE_SHEET_URL = "https://script.google.com/macros/s/AKfycbxZNkEceyINX5eyjbO2rWGsQrk-ex54_U2OJZKHaM-xXQ8zznZMzi3OQJHkzwBRJqAY2Q/exec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtNote = findViewById(R.id.edtNote);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvSelectedPayment = findViewById(R.id.tvSelectedPayment);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnSelectPayment = findViewById(R.id.btnSelectPayment);
        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts);

        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));

        // Lấy sản phẩm từ giỏ hàng hoặc "Mua ngay"
        selectedProducts = new ArrayList<>();
        Intent intent = getIntent();
        Product buyNowProduct = (Product) intent.getSerializableExtra("BUY_NOW_PRODUCT");

        if (buyNowProduct != null) {
            selectedProducts.add(buyNowProduct);
        } else {
            selectedProducts = CartManager.getSelectedItems();
        }

        if (selectedProducts == null || selectedProducts.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new CheckoutProductAdapter(selectedProducts);
        rvCheckoutProducts.setAdapter(adapter);

        // Tính tổng tiền
        double total = 0;
        for (Product p : selectedProducts) {
            total += p.getSalePrice() * p.getQuantity();
        }
        double finalTotal = total;
        tvTotalAmount.setText(String.format("Tổng tiền: %,.0f đ", total));

        // Chọn phương thức thanh toán
        btnSelectPayment.setOnClickListener(v -> {
            Intent i = new Intent(CheckoutActivity.this, PaymentMethodActivity.class);
            startActivityForResult(i, REQUEST_PAYMENT);
        });

        // Xác nhận đơn hàng
        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (!validateFields()) return;

            if (TextUtils.isEmpty(selectedMethod)) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gửi lên Google Sheet
            sendOrderToGoogleSheet(name, phone, address, selectedProducts, finalTotal);

            tvSelectedPayment.setText("Phương thức: " + selectedMethod);
        });
    }

    private boolean validateFields() {
        if (edtName.getText().toString().isEmpty() ||
                edtPhone.getText().toString().isEmpty() ||
                edtAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendOrderToGoogleSheet(String name, String phone, String address,
                                        List<Product> productList, double totalAmount) {
        try {
            JSONArray productsArray = new JSONArray();
            for (Product p : productList) {
                JSONObject item = new JSONObject();
                item.put("tenSanPham", p.getName());
                item.put("soLuong", p.getQuantity());
                item.put("gia", p.getSalePrice());
                productsArray.put(item);
            }

            JSONObject data = new JSONObject();
            data.put("tenKhachHang", name);
            data.put("soDienThoai", phone);
            data.put("diaChi", address);
            data.put("sanPham", productsArray.toString());  // convert mảng thành chuỗi
            data.put("tongTien", totalAmount);
            data.put("thoiGian", new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
            data.put("phuongThuc", selectedMethod);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());

            Request request = new Request.Builder()
                    .url(GOOGLE_SHEET_URL)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(CheckoutActivity.this, "Lỗi gửi dữ liệu", Toast.LENGTH_SHORT).show()
                    );
                }

                @Override public void onResponse(Call call, Response response) {
                    runOnUiThread(() -> {
                        Toast.makeText(CheckoutActivity.this, "🎉 Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                        CartManager.clearCart();
                        finish();
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi xử lý đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PAYMENT && resultCode == RESULT_OK && data != null) {
            selectedMethod = data.getStringExtra("selected_method");
            tvSelectedPayment.setText("Phương thức: " + selectedMethod);
        }
    }
}
