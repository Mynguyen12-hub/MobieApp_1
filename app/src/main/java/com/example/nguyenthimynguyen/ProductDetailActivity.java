package com.example.nguyenthimynguyen;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtProductName, txtProductDescription, txtProductSalePrice,
            txtProductOriginalPrice, txtProductRating, txtProductSold;
    Button btnAddToCart, btnBuyNow;
    RecyclerView rvRelatedProducts;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgProduct);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        txtProductSalePrice = findViewById(R.id.txtProductSalePrice);
        txtProductOriginalPrice = findViewById(R.id.txtProductOriginalPrice);
        txtProductRating = findViewById(R.id.txtProductRating);
        txtProductSold = findViewById(R.id.txtProductSold);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        rvRelatedProducts = findViewById(R.id.rvRelatedProducts);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String name = bundle.getString("name");
            int imageResId = bundle.getInt("imageResId");
            double price = bundle.getDouble("price");
            double salePrice = bundle.getDouble("salePrice");
            String description = bundle.getString("description");
            float rating = bundle.getFloat("rating");
            int sold = bundle.getInt("sold");
            String category = bundle.getString("category"); // ✅

            txtProductName.setText(name);
            imgProduct.setImageResource(imageResId);
            txtProductSalePrice.setText(String.format("%,.0f đ", salePrice));
            txtProductOriginalPrice.setText(price == salePrice ? "" : String.format("%,.0f đ", price));
            txtProductDescription.setText(description);
            txtProductRating.setText("⭐ " + rating);
            txtProductSold.setText("• Đã bán " + sold);

            if (category != null && !category.isEmpty()) {
                ProductRepository.initProducts(); // 👈 Đảm bảo dữ liệu đã sẵn sàng

                List<Product> relatedList = ProductRepository.getProductsByCategory(category);
                relatedList.removeIf(p -> p.getName().equals(name)); // loại trừ sản phẩm hiện tại
            }
        }
    }
}
