package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnSearch, btnCart;
    RecyclerView rvNewProducts, rvFeaturedProducts, rvCategory;
    ProductAdapter featuredAdapter, newAdapter;
    CategoryAdapter categoryAdapter;
    List<Product> allProducts;
    TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnCart = findViewById(R.id.btnCart);
        rvFeaturedProducts = findViewById(R.id.rvFeaturedProducts);
        rvNewProducts = findViewById(R.id.rvNewProduct);
        rvCategory = findViewById(R.id.rvCategory);
        tvAddress = findViewById(R.id.tvAddress);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Địa chỉ người dùng (nếu cần lấy từ SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = prefs.getString("username", "admin");
        tvAddress.setText("👤 Người dùng: " + username);

        // Bottom Navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_chat:
                    return true;
                case R.id.nav_products:
                    startActivity(new Intent(this, ProductListActivity.class));
                    return true;
                case R.id.nav_chat:
                    startActivity(new Intent(this, ChatActivity.class));
                    return true;
                case R.id.btnAdmin:
                    startActivity(new Intent(this, UserActivity.class));
                    return true;
            }
            return false;
        });

        // Danh mục
        rvCategory.setLayoutManager(new GridLayoutManager(this, 4));

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Mặt trời"));
        categoryList.add(new Category("Ánh trăng"));
        categoryList.add(new Category("Ngôi sao"));
        categoryList.add(new Category("Xem thêm"));

        categoryAdapter = new CategoryAdapter(categoryList, position -> {
            if (categoryList.get(position).getName().equals("Xem thêm")) {
                categoryList.remove(position);
                categoryList.add(new Category("Mặt trắng"));
                categoryList.add(new Category("Đám mây"));
                categoryList.add(new Category("Tinh tú"));
                categoryAdapter.notifyDataSetChanged();
            }
        });

        rvCategory.setAdapter(categoryAdapter);

        // Hiển thị sản phẩm
        rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvNewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        allProducts = getSampleProducts();

        List<Product> featuredList = allProducts.subList(0, 6);
        List<Product> newList = allProducts.subList(6, 12);

        featuredAdapter = new ProductAdapter(featuredList);
        newAdapter = new ProductAdapter(newList);

        rvFeaturedProducts.setAdapter(featuredAdapter);
        rvNewProducts.setAdapter(newAdapter);

        // Tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString().trim().toLowerCase();

            if (!TextUtils.isEmpty(keyword)) {
                List<Product> filtered = new ArrayList<>();
                for (Product p : allProducts) {
                    if (p.getName().toLowerCase().contains(keyword)) {
                        filtered.add(p);
                    }
                }
                featuredAdapter.updateList(filtered);
            } else {
                featuredAdapter.updateList(featuredList);
            }
        });

        // Giỏ hàng
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
    }

    private List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Hoa Hồng Đỏ", R.drawable.anh1, 150000, "Biểu tượng của tình yêu", 4.8f, 101));
        products.add(new Product("Hoa Hồng Trắng", R.drawable.anh2, 160000, "Thanh khiết và trong sáng", 4.6f, 102));
        products.add(new Product("Hoa Hồng Vàng", R.drawable.anh3, 155000, "Tình bạn và niềm vui", 4.7f, 103));
        products.add(new Product("Hoa Cẩm Tú Cầu", R.drawable.anh4, 180000, "Thành ý và biết ơn", 4.5f, 104));
        products.add(new Product("Hoa Hướng Dương", R.drawable.anh5, 170000, "Vươn tới ánh sáng", 4.9f, 105));
        products.add(new Product("Hoa Cẩm Chướng", R.drawable.anh6, 140000, "Sự ngọt ngào", 4.3f, 106));
        products.add(new Product("Hoa Lan Hồ Điệp", R.drawable.anh7, 250000, "Sang trọng và quý phái", 4.9f, 107));
        products.add(new Product("Hoa Ly", R.drawable.anh8, 200000, "Sự thanh cao", 4.6f, 108));
        products.add(new Product("Hoa Tulip", R.drawable.anh9, 220000, "Lãng mạn và thanh lịch", 4.7f, 109));
        products.add(new Product("Hoa Baby", R.drawable.anh10, 120000, "Nhẹ nhàng và thuần khiết", 4.2f, 110));
        products.add(new Product("Hoa Cúc Mẫu Đơn", R.drawable.anh11, 160000, "May mắn và phúc lộc", 4.5f, 111));
        products.add(new Product("Hoa Cúc Họa Mi", R.drawable.anh12, 130000, "Trong sáng tuổi trẻ", 4.4f, 112));
        return products;
    }
}