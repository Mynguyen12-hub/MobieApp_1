package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView rvProductList, rvCategory;
    private ProductAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private EditText edtSearch;
    private ImageView btnCart;
    private TextView tvCartCount;

    private List<Product> allProducts = new ArrayList<>();
    private List<Category> categoryList;
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        CartManager.init(this);

        rvProductList = findViewById(R.id.rvProductList);
        rvCategory = findViewById(R.id.rvCategory);
        edtSearch = findViewById(R.id.edtSearch);
        btnCart = findViewById(R.id.btnCart);
        tvCartCount = findViewById(R.id.tvCartCount);

        CartUtils.updateCartCount(tvCartCount);

        rvProductList.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(new ArrayList<>());
        rvProductList.setAdapter(adapter);

        // Danh sách danh mục
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Mặt trời"));
        categoryList.add(new Category("Ánh trăng"));
        categoryList.add(new Category("Ngôi sao"));
        categoryList.add(new Category("Mặt trắng"));
        categoryList.add(new Category("Đám mây"));
        categoryList.add(new Category("Tinh tú"));

        // Adapter danh mục
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

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

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

        loadProductsFromApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CartUtils.updateCartCount(tvCartCount);
    }

    private void loadProductsFromApi() {
        ProductApi api = ApiClient.getClient().create(ProductApi.class);
        api.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allProducts = response.body();

                    // ✅ THÊM DÒNG NÀY ĐỂ ĐỒNG BỘ DỮ LIỆU VỚI ProductDetailActivity
                    ProductManager.setProductList(allProducts);

                    applyFilters();
                } else {
                    Toast.makeText(ProductListActivity.this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilters() {
        String keyword = edtSearch.getText().toString().trim().toLowerCase();
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean matchKeyword = p.getName().toLowerCase().contains(keyword);
            boolean matchCategory = selectedCategory.isEmpty() || p.getCategory().equalsIgnoreCase(selectedCategory);
            if (matchKeyword && matchCategory) {
                filtered.add(p.clone());
            }
        }
        adapter.updateList(filtered);
    }
}
