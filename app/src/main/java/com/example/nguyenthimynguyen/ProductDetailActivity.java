package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtProductName, txtProductDescription, txtProductSalePrice, txtProductOriginalPrice;
    TextView txtQuantity, btnIncrease, btnDecrease;
    Button btnAddToCart, btnBuyNow;
    ImageView btnBack, btnCart, btnChat;

    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ view
        imgProduct = findViewById(R.id.imgProduct);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        txtProductSalePrice = findViewById(R.id.txtProductSalePrice);
        txtProductOriginalPrice = findViewById(R.id.txtProductOriginalPrice);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        txtQuantity = findViewById(R.id.txtQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        btnBack = findViewById(R.id.btnBack);
        btnCart = findViewById(R.id.btnCart);
        btnChat = findViewById(R.id.btnChat);

        // Nhận ID sản phẩm từ Intent
        int id = getIntent().getIntExtra("id", -1);
        currentProduct = ProductRepository.getProductById(id);

        if (currentProduct == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentProduct.setQuantity(1);
        txtQuantity.setText("1");

        // Hiển thị dữ liệu sản phẩm
        imgProduct.setImageResource(currentProduct.getImageResId());
        txtProductName.setText(currentProduct.getName());
        txtProductDescription.setText(currentProduct.getDescription());
        txtProductSalePrice.setText(String.format("%,.0f đ", currentProduct.getSalePrice()));

        if (currentProduct.getPrice() == currentProduct.getSalePrice()) {
            txtProductOriginalPrice.setVisibility(TextView.GONE);
        } else {
            txtProductOriginalPrice.setVisibility(TextView.VISIBLE);
            txtProductOriginalPrice.setText(String.format("%,.0f đ", currentProduct.getPrice()));
        }

        // Tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            int quantity = currentProduct.getQuantity();
            currentProduct.setQuantity(quantity + 1);
            txtQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        });

        // Giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            int quantity = currentProduct.getQuantity();
            if (quantity > 1) {
                currentProduct.setQuantity(quantity - 1);
                txtQuantity.setText(String.valueOf(currentProduct.getQuantity()));
            }
        });

        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            CartManager.addItem(currentProduct);
            CartUtils.updateCartCount(null);
            Toast.makeText(this, "✅ Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Mua ngay
        btnBuyNow.setOnClickListener(v -> {
            CartManager.addItem(currentProduct);
            CartUtils.updateCartCount(null);
            startActivity(new Intent(this, CartActivity.class));
        });

        // Quay lại
        btnBack.setOnClickListener(v -> finish());

        // Mở giỏ hàng
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        // Mở chat
        btnChat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
