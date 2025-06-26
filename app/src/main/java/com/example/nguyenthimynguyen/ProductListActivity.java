package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
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

    private List<Product> productList;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rvProductList = findViewById(R.id.rvProductList);
        rvCategory = findViewById(R.id.rvCategory);
        edtSearch = findViewById(R.id.edtSearch);
        btnCart = findViewById(R.id.btnCart);
        tvCartCount = findViewById(R.id.tvCartCount);

        rvProductList.setHasFixedSize(true);
        rvProductList.setLayoutManager(new GridLayoutManager(this, 2));

        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(this, 4));

        // [1] Khởi tạo danh sách sản phẩm
        productList = new ArrayList<>();
        productList.add(new Product("Hoa Hồng Đỏ", R.drawable.anh1, 150000, 135000, "Biểu tượng của tình yêu", 4.8f, 101, "Mặt trời"));
        productList.add(new Product("Hoa Hồng Trắng", R.drawable.anh2, 160000, 145000, "Thanh khiết và trong sáng", 4.6f, 102, "Ánh trăng"));
        productList.add(new Product("Hoa Hồng Vàng", R.drawable.anh3, 155000, 140000, "Tình bạn và niềm vui", 4.7f, 103, "Ngôi sao"));
        productList.add(new Product("Hoa Cẩm Tú Cầu", R.drawable.anh4, 180000, 165000, "Thành ý và biết ơn", 4.5f, 104, "Mặt trắng"));
        productList.add(new Product("Hoa Hướng Dương", R.drawable.anh5, 170000, 155000, "Vươn tới ánh sáng", 4.9f, 105, "Đám mây"));
        productList.add(new Product("Hoa Cẩm Chướng", R.drawable.anh6, 140000, 125000, "Sự ngọt ngào", 4.3f, 106, "Tinh tú"));
        productList.add(new Product("Hoa Lan Hồ Điệp", R.drawable.anh7, 250000, 230000, "Sang trọng và quý phái", 4.9f, 107, "Mặt trời"));
        productList.add(new Product("Hoa Ly", R.drawable.anh8, 200000, 180000, "Sự thanh cao", 4.6f, 108, "Ánh trăng"));
        productList.add(new Product("Hoa Tulip", R.drawable.anh9, 220000, 200000, "Lãng mạn và thanh lịch", 4.7f, 109, "Ngôi sao"));
        productList.add(new Product("Hoa Baby", R.drawable.anh10, 120000, 110000, "Nhẹ nhàng và thuần khiết", 4.2f, 110, "Mặt trắng"));
        productList.add(new Product("Hoa Cúc Mẫu Đơn", R.drawable.anh11, 160000, 145000, "May mắn và phúc lộc", 4.5f, 111, "Đám mây"));
        productList.add(new Product("Hoa Cúc Họa Mi", R.drawable.anh12, 130000, 120000, "Trong sáng tuổi trẻ", 4.4f, 112, "Tinh tú"));
        productList.add(new Product("Hoa Thược Dược", R.drawable.anh13, 145000, 130000, "Ngọt ngào và bền bỉ", 4.5f, 113, "Mặt trời"));
        productList.add(new Product("Hoa Oải Hương", R.drawable.anh14, 160000, 145000, "Hương thơm dịu nhẹ", 4.8f, 114, "Ánh trăng"));
        productList.add(new Product("Hoa Dạ Lan", R.drawable.anh15, 155000, 140000, "Quyến rũ trong đêm", 4.6f, 115, "Ngôi sao"));
        productList.add(new Product("Hoa Cát Tường", R.drawable.anh16, 150000, 135000, "May mắn và bình an", 4.5f, 116, "Mặt trắng"));
        productList.add(new Product("Hoa Bồ Công Anh", R.drawable.anh17, 125000, 115000, "Mong manh và nhẹ nhàng", 4.3f, 117, "Đám mây"));
        productList.add(new Product("Hoa Mai", R.drawable.anh18, 200000, 185000, "Tươi thắm ngày Tết", 4.7f, 118, "Tinh tú"));
        productList.add(new Product("Hoa Đào", R.drawable.anh19, 190000, 175000, "Sắc xuân miền Bắc", 4.8f, 119, "Mặt trời"));
        productList.add(new Product("Hoa Lưu Ly", R.drawable.anh20, 110000, 99000, "Nhớ mãi không quên", 4.4f, 120, "Ánh trăng"));
        productList.add(new Product("Hoa Đồng Tiền", R.drawable.anh21, 145000, 130000, "Tài lộc và thịnh vượng", 4.5f, 121, "Ngôi sao"));
        productList.add(new Product("Hoa Thiên Điểu", R.drawable.anh22, 210000, 195000, "Mạnh mẽ và độc đáo", 4.6f, 122, "Mặt trắng"));
        productList.add(new Product("Hoa Cúc Vạn Thọ", R.drawable.anh24, 135000, 120000, "Trường thọ và viên mãn", 4.2f, 124, "Đám mây"));
        productList.add(new Product("Hoa Dừa Cạn", R.drawable.anh25, 125000, 115000, "Giản dị và kiên cường", 4.4f, 125, "Tinh tú"));

        adapter = new ProductAdapter(productList);
        rvProductList.setAdapter(adapter);

        // [2] Khởi tạo danh mục
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Mặt trời"));
        categoryList.add(new Category("Ánh trăng"));
        categoryList.add(new Category("Ngôi sao"));
        categoryList.add(new Category("Mặt trắng"));
        categoryList.add(new Category("Đám mây"));
        categoryList.add(new Category("Tinh tú"));

        categoryAdapter = new CategoryAdapter(categoryList, position -> {
            String selectedCategory = categoryList.get(position).getName();
            for (int i = 0; i < categoryList.size(); i++) {
                categoryList.get(i).setSelected(i == position);
            }
            categoryAdapter.notifyDataSetChanged();
            filterProductsByCategory(selectedCategory);
        });
        rvCategory.setAdapter(categoryAdapter);

        // [3] BottomNavigationView xử lý chuyển màn
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_products);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_products) {
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

        // [4] Xử lý giỏ hàng
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        updateCartCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    private void updateCartCount() {
        int count = CartManager.getCartSize();
        if (count > 0) {
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(String.valueOf(count));
        } else {
            tvCartCount.setVisibility(View.GONE);
        }
    }

    private void filterProductsByCategory(String category) {
        List<Product> filteredList = new ArrayList<>();
        for (Product p : productList) {
            if (p.getCategory().equals(category)) {
                filteredList.add(p);
            }
        }
        adapter.updateList(filteredList);
    }
}