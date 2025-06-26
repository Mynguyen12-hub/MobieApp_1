package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    BottomNavigationView bottomNavigation;
    String selectedCategory = "";

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
        bottomNavigation = findViewById(R.id.bottomNavigation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = prefs.getString("username", "admin");
        tvAddress.setText("👤 Người dùng: " + username);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_products) {
                startActivity(new Intent(this, ProductListActivity.class));
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_admin) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });

        rvCategory.setLayoutManager(new GridLayoutManager(this, 4));
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Mặt trời"));
        categoryList.add(new Category("Ánh trăng"));
        categoryList.add(new Category("Ngôi sao"));
        categoryList.add(new Category("Xem thêm"));

        categoryAdapter = new CategoryAdapter(categoryList, position -> {
            String name = categoryList.get(position).getName();

            if (name.equals("Xem thêm")) {
                categoryList.remove(position);
                categoryList.add(new Category("Mặt trắng"));
                categoryList.add(new Category("Đám mây"));
                categoryList.add(new Category("Tinh tú"));
                categoryAdapter.notifyDataSetChanged();
            } else {
                selectedCategory = name.equals("Tất cả") ? "" : name;
                filterAndUpdate();
            }
        });
        rvCategory.setAdapter(categoryAdapter);

        rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvNewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        allProducts = getSampleProducts();
        List<Product> featuredList = allProducts.subList(0, 6);
        List<Product> newList = allProducts.subList(6, 12);

        featuredAdapter = new ProductAdapter(featuredList);
        newAdapter = new ProductAdapter(newList);

        rvFeaturedProducts.setAdapter(featuredAdapter);
        rvNewProducts.setAdapter(newAdapter);

        btnSearch.setOnClickListener(v -> filterAndUpdate());

        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
    }

    private void filterAndUpdate() {
        String keyword = edtSearch.getText().toString().trim().toLowerCase();

        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean matchesName = TextUtils.isEmpty(keyword) || p.getName().toLowerCase().contains(keyword);
            boolean matchesCategory = TextUtils.isEmpty(selectedCategory) || p.getCategory().equalsIgnoreCase(selectedCategory);
            if (matchesName && matchesCategory) {
                filtered.add(p);
            }
        }

        List<Product> featuredFiltered = new ArrayList<>();
        List<Product> newFiltered = new ArrayList<>();

        for (int i = 0; i < filtered.size(); i++) {
            if (i < 6) featuredFiltered.add(filtered.get(i));
            else newFiltered.add(filtered.get(i));
        }

        featuredAdapter.updateList(featuredFiltered);
        newAdapter.updateList(newFiltered);
    }

    private List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Hoa Hồng Đỏ", R.drawable.anh1, 150000, 135000, "Biểu tượng của tình yêu", 4.8f, 101, "Mặt trời"));
        products.add(new Product("Hoa Hồng Trắng", R.drawable.anh2, 160000, 145000, "Thanh khiết và trong sáng", 4.6f, 102, "Ánh trăng"));
        products.add(new Product("Hoa Hồng Vàng", R.drawable.anh3, 155000, 140000, "Tình bạn và niềm vui", 4.7f, 103, "Ngôi sao"));
        products.add(new Product("Hoa Cẩm Tú Cầu", R.drawable.anh4, 180000, 165000, "Thành ý và biết ơn", 4.5f, 104, "Mặt trắng"));
        products.add(new Product("Hoa Hướng Dương", R.drawable.anh5, 170000, 155000, "Vươn tới ánh sáng", 4.9f, 105, "Đám mây"));
        products.add(new Product("Hoa Cẩm Chướng", R.drawable.anh6, 140000, 125000, "Sự ngọt ngào", 4.3f, 106, "Tinh tú"));
        products.add(new Product("Hoa Lan Hồ Điệp", R.drawable.anh7, 250000, 230000, "Sang trọng và quý phái", 4.9f, 107, "Mặt trời"));
        products.add(new Product("Hoa Ly", R.drawable.anh8, 200000, 180000, "Sự thanh cao", 4.6f, 108, "Ánh trăng"));
        products.add(new Product("Hoa Tulip", R.drawable.anh9, 220000, 200000, "Lãng mạn và thanh lịch", 4.7f, 109, "Ngôi sao"));
        products.add(new Product("Hoa Baby", R.drawable.anh10, 120000, 110000, "Nhẹ nhàng và thuần khiết", 4.2f, 110, "Mặt trắng"));
        products.add(new Product("Hoa Cúc Mẫu Đơn", R.drawable.anh11, 160000, 145000, "May mắn và phúc lộc", 4.5f, 111, "Đám mây"));
        products.add(new Product("Hoa Cúc Họa Mi", R.drawable.anh12, 130000, 120000, "Trong sáng tuổi trẻ", 4.4f, 112, "Tinh tú"));
        return products;
    }
}