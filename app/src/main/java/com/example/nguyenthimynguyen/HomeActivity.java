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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private EditText edtSearch;
    private ImageView btnSearch, btnCart;
    private TextView tvCartCount, tvAddress;
    private TextView tvDiscount5, tvDiscount10, tvDiscount25;

    private RecyclerView rvNewProducts, rvFeaturedProducts, rvCategory;
    private ProductAdapter featuredAdapter, newAdapter;
    private CategoryAdapter categoryAdapter;

    private List<Product> allProducts = new ArrayList<>();
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CartManager.init(this);

        mapViews();
        setupBanner();
        setupDiscountListeners();
        setupBottomNavigation();
        setupCategoryRecycler();
        setupProductRecycler();
        loadProductsFromApi();

        btnSearch.setOnClickListener(v -> filterAndUpdate());
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        CartUtils.updateCartCount(tvCartCount);
    }

    private void mapViews() {
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnCart = findViewById(R.id.btnCart);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvAddress = findViewById(R.id.tvAddress);
        tvDiscount5 = findViewById(R.id.tvDiscount5);
        tvDiscount10 = findViewById(R.id.tvDiscount10);
        tvDiscount25 = findViewById(R.id.tvDiscount25);
        rvNewProducts = findViewById(R.id.rvNewProduct);
        rvFeaturedProducts = findViewById(R.id.rvFeaturedProducts);
        rvCategory = findViewById(R.id.rvCategory);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = prefs.getString("username", "admin");
        tvAddress.setText("👤 Người dùng: " + username);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CartUtils.updateCartCount(tvCartCount);
    }

    private void setupDiscountListeners() {
        tvDiscount5.setOnClickListener(v -> selectDiscount("5"));
        tvDiscount10.setOnClickListener(v -> selectDiscount("10"));
        tvDiscount25.setOnClickListener(v -> selectDiscount("25"));
    }

    private void selectDiscount(String code) {
        SharedPreferences prefs = getSharedPreferences("discount_prefs", MODE_PRIVATE);
        prefs.edit().putString("code", code).apply();
        Toast.makeText(this, "🎉 Đã chọn mã giảm " + code + "%", Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_products) {
                startActivity(new Intent(this, ProductListActivity.class));
                return true;
            }
            if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            }
            if (id == R.id.nav_admin) {
                startActivity(new Intent(this, UserActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupBanner() {
        ViewPager2 viewPager = findViewById(R.id.viewPagerBanner);
        TabLayout tabIndicator = findViewById(R.id.tabIndicator);

        List<Integer> bannerImages = Arrays.asList(
                R.drawable.banner,
                R.drawable.banner1,
                R.drawable.banner2
        );

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        viewPager.setAdapter(bannerAdapter);

        new TabLayoutMediator(tabIndicator, viewPager, (tab, position) -> {
        }).attach();

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
    }

    private void setupCategoryRecycler() {
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
    }

    private void setupProductRecycler() {
        rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvNewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredAdapter = new ProductAdapter(new ArrayList<>());
        newAdapter = new ProductAdapter(new ArrayList<>());

        rvFeaturedProducts.setAdapter(featuredAdapter);
        rvNewProducts.setAdapter(newAdapter);
    }

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

    private void loadProductsFromApi() {
        ProductApi apiService = ApiClient.getClient().create(ProductApi.class);
        Call<List<Product>> call = apiService.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    allProducts = response.body();

                    // ✅ Ghi lại danh sách sản phẩm để ProductDetailActivity có thể dùng
                    ProductManager.setProductList(allProducts);

                    filterAndUpdate();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}