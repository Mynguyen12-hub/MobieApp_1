package com.example.nguyenthimynguyen;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtAddress, edtNote;
    TextView tvTotalAmount;
    Button btnConfirm;
    RecyclerView rvCheckoutProducts;
    CheckoutProductAdapter adapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtNote = findViewById(R.id.edtNote);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnConfirm = findViewById(R.id.btnConfirm);
        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts);

        // Lấy danh sách sản phẩm từ giỏ hàng
        productList = CartManager.getCart();

        // Setup RecyclerView
        adapter = new CheckoutProductAdapter(productList);
        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutProducts.setAdapter(adapter);

        // Hiển thị tổng tiền
        double total = CartManager.getTotalAmount();
        tvTotalAmount.setText(String.format("Tổng tiền: %,.0f đ", total));

        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "🎉 Đặt hàng thành công!", Toast.LENGTH_LONG).show();
            CartManager.clearCart();
            finish();
        });
    }
}
