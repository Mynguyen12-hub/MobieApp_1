package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView rvProductList, rvCategory;
    private ProductAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private EditText edtSearch;
    private ImageView btnCart;
    private TextView tvCartCount;

    private List<Product> allProducts;
    private List<Category> categoryList;
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Đồng bộ giỏ hàng
        CartManager.init(this);

        // Ánh xạ view
        rvProductList = findViewById(R.id.rvProductList);
        rvCategory = findViewById(R.id.rvCategory);
        edtSearch = findViewById(R.id.edtSearch);
        btnCart = findViewById(R.id.btnCart);
        tvCartCount = findViewById(R.id.tvCartCount);

        // Load sản phẩm chung từ repository
        ProductRepository.initProducts();
        allProducts = ProductRepository.getAllProducts();

        // Cập nhật giỏ hàng ban đầu
        CartUtils.updateCartCount(tvCartCount);

        // Setup RecyclerView sản phẩm
        rvProductList.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(new ArrayList<>(allProducts));
        rvProductList.setAdapter(adapter);

        // Setup RecyclerView danh mục
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Mặt trời"));
        categoryList.add(new Category("Ánh trăng"));
        categoryList.add(new Category("Ngôi sao"));
        categoryList.add(new Category("Mặt trắng"));
        categoryList.add(new Category("Đám mây"));
        categoryList.add(new Category("Tinh tú"));

        categoryAdapter = new CategoryAdapter(categoryList, position -> {
            selectedCategory = categoryList.get(position).getName();
            for (int i = 0; i < categoryList.size(); i++) {
                categoryList.get(i).setSelected(i == position);
            }
            categoryAdapter.notifyDataSetChanged();
            applyFilters();
        });

        rvCategory.setLayoutManager(new GridLayoutManager(this, 4));
        rvCategory.setAdapter(categoryAdapter);

        // Bottom navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_products);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_admin) {
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            }
            return false;
        });

        // Click giỏ hàng
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        // Tìm kiếm sản phẩm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(); // Gọi lọc khi gõ
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CartUtils.updateCartCount(tvCartCount);
    }

    // Kết hợp lọc theo danh mục và từ khóa
    private void applyFilters() {
        String keyword = edtSearch.getText().toString().trim().toLowerCase();
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean matchesKeyword = p.getName().toLowerCase().contains(keyword);
            boolean matchesCategory = selectedCategory.isEmpty() || p.getCategory().equalsIgnoreCase(selectedCategory);
            if (matchesKeyword && matchesCategory) {
                filtered.add(p);
            }
        }
        adapter.updateList(filtered);
    }
}
