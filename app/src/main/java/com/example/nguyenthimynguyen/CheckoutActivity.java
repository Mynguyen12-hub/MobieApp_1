package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtAddress, edtNote;
    TextView tvTotalAmount, tvSelectedPayment;
    Button btnConfirm, btnSelectPayment;
    RecyclerView rvCheckoutProducts;

    CheckoutProductAdapter adapter;
    List<Product> selectedProducts;

    String selectedMethod = "";

    private static final int REQUEST_PAYMENT = 101;

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

        // Lấy sản phẩm đã chọn
        selectedProducts = CartManager.getSelectedItems();

        if (selectedProducts == null || selectedProducts.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm nào được chọn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị danh sách sản phẩm
        adapter = new CheckoutProductAdapter(selectedProducts);
        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutProducts.setAdapter(adapter);

        // Tính tổng tiền
        double total = 0;
        for (Product p : selectedProducts) {
            total += p.getSalePrice() * p.getQuantity();
        }
        tvTotalAmount.setText(String.format("Tổng tiền: %,.0f đ", total));

        // Chọn phương thức thanh toán
        btnSelectPayment.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, PaymentMethodActivity.class);
            startActivityForResult(intent, REQUEST_PAYMENT);
        });

        // Xác nhận đơn hàng
        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(selectedMethod)) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            tvSelectedPayment.setText("Phương thức: " + selectedMethod);

            // Thông báo thành công
            Toast.makeText(this, "🎉 Đặt hàng thành công!", Toast.LENGTH_LONG).show();

            // Xóa các sản phẩm đã chọn khỏi giỏ hàng
            for (Product p : selectedProducts) {
                CartManager.removeItemById(p.getId());
            }

            CartManager.saveCart();
            finish();
        });
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
