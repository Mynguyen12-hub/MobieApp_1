package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtProductName, txtProductDescription, txtProductSalePrice, txtProductOriginalPrice;
    TextView txtQuantity, btnIncrease, btnDecrease;
    Button btnAddToCart, btnBuyNow;
    ImageView btnBack, btnCart, btnChat;

    RecyclerView rvRelated;
    RelatedProductAdapter relatedAdapter;

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
        rvRelated = findViewById(R.id.rvRelatedProducts);

        // Lấy ID sản phẩm được truyền vào
        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentProduct = ProductManager.getProductById(productId);
        if (currentProduct == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Mặc định số lượng = 1
        currentProduct.setQuantity(1);
        txtQuantity.setText("1");

        // Hiển thị ảnh
        int imageResId = getResources().getIdentifier(
                currentProduct.getImageResId(), "drawable", getPackageName());
        if (imageResId != 0) {
            imgProduct.setImageResource(imageResId);
        } else {
            imgProduct.setImageResource(R.drawable.anh1); // fallback ảnh mặc định
        }

        // Hiển thị thông tin sản phẩm
        txtProductName.setText(currentProduct.getName());
        txtProductDescription.setText(currentProduct.getDescription());
        txtProductSalePrice.setText(String.format("%,.0f đ", currentProduct.getSalePrice()));

        if (currentProduct.getPrice() == currentProduct.getSalePrice()) {
            txtProductOriginalPrice.setVisibility(TextView.GONE);
        } else {
            txtProductOriginalPrice.setVisibility(TextView.VISIBLE);
            txtProductOriginalPrice.setText(String.format("%,.0f đ", currentProduct.getPrice()));
        }

        // Tăng giảm số lượng
        btnIncrease.setOnClickListener(v -> {
            int quantity = currentProduct.getQuantity();
            currentProduct.setQuantity(quantity + 1);
            txtQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        });

        btnDecrease.setOnClickListener(v -> {
            int quantity = currentProduct.getQuantity();
            if (quantity > 1) {
                currentProduct.setQuantity(quantity - 1);
                txtQuantity.setText(String.valueOf(currentProduct.getQuantity()));
            }
        });

        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            CartManager.addItem(currentProduct.clone()); // clone để tránh ảnh hưởng đối tượng gốc
            CartUtils.updateCartCount(null); // nếu bạn có icon đếm giỏ
            Toast.makeText(this, "✅ Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Mua ngay
        btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
            intent.putExtra("buy_now", true);
            intent.putExtra("buyNowProduct", currentProduct.clone()); // clone để tránh thay đổi dữ liệu gốc
            startActivity(intent);
        });

        // Điều hướng
        btnBack.setOnClickListener(v -> finish());
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnChat.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));

        // Sản phẩm liên quan
        setupRelatedProducts();
    }

    private void setupRelatedProducts() {
        List<Product> related = ProductManager.getProductsByCategory(currentProduct.getCategory());
        related.removeIf(p -> p.getId() == currentProduct.getId());

        relatedAdapter = new RelatedProductAdapter(related);
        rvRelated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRelated.setAdapter(relatedAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
