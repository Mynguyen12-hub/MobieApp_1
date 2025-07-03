package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnSearch, btnCart;
    RecyclerView rvNewProducts, rvFeaturedProducts, rvCategory;
    ProductAdapter featuredAdapter, newAdapter;
    CategoryAdapter categoryAdapter;
    List<Product> allProducts;
    TextView tvAddress, tvCartCount;
    TextView tvDiscount5, tvDiscount10, tvDiscount25;
    BottomNavigationView bottomNavigation;
    String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo giỏ hàng và sản phẩm
        CartManager.init(this);
        ProductRepository.initProducts();
        allProducts = ProductRepository.getAllProducts();

        // Ánh xạ view
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnCart = findViewById(R.id.btnCart);
        tvCartCount = findViewById(R.id.tvCartCount);
        rvFeaturedProducts = findViewById(R.id.rvFeaturedProducts);
        rvNewProducts = findViewById(R.id.rvNewProduct);
        rvCategory = findViewById(R.id.rvCategory);
        tvAddress = findViewById(R.id.tvAddress);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Ánh xạ mã giảm giá
        tvDiscount5 = findViewById(R.id.tvDiscount5);
        tvDiscount10 = findViewById(R.id.tvDiscount10);
        tvDiscount25 = findViewById(R.id.tvDiscount25);

        // Sự kiện click mã giảm giá
        tvDiscount5.setOnClickListener(v -> {
            saveDiscountCode("5");
            Toast.makeText(this, "Đã chọn mã giảm 5%", Toast.LENGTH_SHORT).show();
        });

        tvDiscount10.setOnClickListener(v -> {
            saveDiscountCode("10");
            Toast.makeText(this, "Đã chọn mã giảm 10%", Toast.LENGTH_SHORT).show();
        });

        tvDiscount25.setOnClickListener(v -> {
            saveDiscountCode("25");
            Toast.makeText(this, "Đã chọn mã giảm 25%", Toast.LENGTH_SHORT).show();
        });

        // Cập nhật giỏ hàng ban đầu
        CartUtils.updateCartCount(tvCartCount);

        // Xử lý click giỏ hàng
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        // Lấy tên người dùng
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = prefs.getString("username", "admin");
        tvAddress.setText("\uD83D\uDC64 Người dùng: " + username);

        // Xử lý bottom navigation
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
                startActivity(new Intent(this, UserActivity.class));
                return true;
            }
            return false;
        });

        // Cập nhật padding hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Banner
        ViewPager2 viewPager = findViewById(R.id.viewPagerBanner);
        TabLayout tabIndicator = findViewById(R.id.tabIndicator);

        List<Integer> bannerImages = Arrays.asList(
                R.drawable.banner,
                R.drawable.banner1,
                R.drawable.banner2
        );

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        viewPager.setAdapter(bannerAdapter);
        new TabLayoutMediator(tabIndicator, viewPager, (tab, position) -> {}).attach();

        // Tự động cuộn banner
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int currentPage = 0;
            @Override
            public void run() {
                if (currentPage >= bannerImages.size()) currentPage = 0;
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        // Danh mục
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

        // Sản phẩm nổi bật & mới
        rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvNewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredAdapter = new ProductAdapter(new ArrayList<>());
        newAdapter = new ProductAdapter(new ArrayList<>());
        rvFeaturedProducts.setAdapter(featuredAdapter);
        rvNewProducts.setAdapter(newAdapter);

        // Hiển thị sản phẩm
        filterAndUpdate();

        // Tìm kiếm
        btnSearch.setOnClickListener(v -> filterAndUpdate());
    }

    @Override
    protected void onResume() {
        super.onResume();
        CartUtils.updateCartCount(tvCartCount);
    }

    // Lưu mã giảm giá vào SharedPreferences
    private void saveDiscountCode(String code) {
        SharedPreferences prefs = getSharedPreferences("discount_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("code", code);
        editor.apply();
    }

    // Lọc sản phẩm theo từ khóa hoặc danh mục
    private void filterAndUpdate() {
        String keyword = edtSearch.getText().toString().trim().toLowerCase();
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean matchName = TextUtils.isEmpty(keyword) || p.getName().toLowerCase().contains(keyword);
            boolean matchCat = TextUtils.isEmpty(selectedCategory) || p.getCategory().equalsIgnoreCase(selectedCategory);
            if (matchName && matchCat) {
                filtered.add(p.clone());
            }
        }

        List<Product> featuredList = new ArrayList<>();
        List<Product> newList = new ArrayList<>();
        for (int i = 0; i < filtered.size(); i++) {
            if (i < 6) featuredList.add(filtered.get(i));
            else newList.add(filtered.get(i));
        }

        featuredAdapter.updateList(featuredList);
        newAdapter.updateList(newList);
    }
}
