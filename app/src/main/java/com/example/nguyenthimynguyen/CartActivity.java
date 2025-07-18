package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private TextView txtTotal, txtDiscountInfo, txtTotalAfterDiscount;
    private Button btnCheckout, btnClearAll;
    private CartAdapter adapter;
    private List<Product> cartItems;
    private String discountCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Khởi tạo cart
        CartManager.init(this);
        cartItems = CartManager.getCart();

        // Ánh xạ view
        rvCart = findViewById(R.id.rvCart);
        txtTotal = findViewById(R.id.txtTotal);
        txtDiscountInfo = findViewById(R.id.txtDiscountInfo);
        txtTotalAfterDiscount = findViewById(R.id.txtTotalAfterDiscount);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnClearAll = findViewById(R.id.btnClearAll);

        // RecyclerView setup
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, this::updateTotal);
        rvCart.setAdapter(adapter);

        // Load mã giảm giá
        loadDiscountCode();
        updateTotal();

        // Xử lý nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            List<Product> selectedItems = adapter.getSelectedItems();

            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu muốn, bạn có thể lưu selectedItems vào Singleton hoặc tạm thời vào CartManager
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        });

        // Xóa tất cả sản phẩm
        btnClearAll.setOnClickListener(v -> {
            CartManager.clearCart();
            cartItems.clear();
            adapter.notifyDataSetChanged();
            updateTotal();
            Toast.makeText(this, "Đã xoá tất cả sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Bottom navigation xử lý điều hướng
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (id == R.id.nav_products) {
                startActivity(new Intent(this, ProductListActivity.class));
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_admin) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadDiscountCode() {
        SharedPreferences prefs = getSharedPreferences("discount_prefs", MODE_PRIVATE);
        discountCode = prefs.getString("code", "");
    }

    private void updateTotal() {
        List<Product> selectedItems = adapter.getSelectedItems();

        double total = 0;
        for (Product product : selectedItems) {
            total += product.getSalePrice() * product.getQuantity();
        }
        txtTotal.setText(String.format("Tổng: %,.0f đ", total));

        double discountPercent = 0;
        if (discountCode.equals("5")) discountPercent = 0.05;
        else if (discountCode.equals("10")) discountPercent = 0.10;
        else if (discountCode.equals("25")) discountPercent = 0.25;

        double discounted = total * (1 - discountPercent);

        txtDiscountInfo.setText(discountPercent > 0 ? "Đã áp dụng mã giảm " + discountCode + "%" : "Không có mã giảm giá");
        txtTotalAfterDiscount.setText(String.format("Tổng sau giảm: %,.0f đ", discounted));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        updateTotal();
    }
}
