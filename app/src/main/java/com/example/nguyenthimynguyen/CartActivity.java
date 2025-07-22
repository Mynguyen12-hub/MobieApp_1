package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView txtTotalQuantity, txtTotalPrice;
    private Button btnCheckout, btnClearAll;
    private CartAdapter adapter;
    private List<Product> cartItems;
    private String discountCode = "";

    private LinearLayout layoutTotalDetails, layoutTotalToggle;
    private ImageView imgToggleArrow;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Khởi tạo CartManager
        CartManager.init(this);
        cartItems = CartManager.getCart();

        // Ánh xạ các view
        rvCart = findViewById(R.id.rvCart);
        txtTotal = findViewById(R.id.txtTotal);
        txtDiscountInfo = findViewById(R.id.txtDiscountInfo);
        txtTotalAfterDiscount = findViewById(R.id.txtTotalAfterDiscount);
        txtTotalQuantity = findViewById(R.id.txtTotalQuantity);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnClearAll = findViewById(R.id.btnClearAll);
        layoutTotalDetails = findViewById(R.id.layout_total_details);
        layoutTotalToggle = findViewById(R.id.layout_total_toggle);
        imgToggleArrow = findViewById(R.id.imgToggleArrow);

        // Thiết lập RecyclerView
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, this::updateTotal);
        rvCart.setAdapter(adapter);

        // Toggle chi tiết tổng tiền
        layoutTotalToggle.setOnClickListener(v -> {
            isExpanded = !isExpanded;
            layoutTotalDetails.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            imgToggleArrow.setImageResource(isExpanded ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down);
        });

        // Tải mã giảm giá và cập nhật tổng
        loadDiscountCode();
        updateTotal();

        // Xử lý nút "Thanh toán"
        btnCheckout.setOnClickListener(view -> {
            List<Product> selected = adapter.getSelectedItems();  // ✅ Gọi qua biến adapter đã tạo

            ; // giả sử adapter trả về sản phẩm được tích checkbox
            if (selected.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            CartManager.setSelectedItems(selected); // chỉ ghi lại sản phẩm tích
            startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
        });

        // Xử lý nút "Xóa tất cả"
        btnClearAll.setOnClickListener(v -> {
            CartManager.clearCart();
            cartItems.clear();
            adapter.notifyDataSetChanged();
            updateTotal();
            Toast.makeText(this, "Đã xoá tất cả sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Bottom navigation
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

        int totalQuantity = 0;
        double total = 0;

        for (Product product : selectedItems) {
            totalQuantity += product.getQuantity();
            total += product.getSalePrice() * product.getQuantity();
        }

        txtTotal.setText(String.format("Tổng: %,.0f đ", total));
        txtTotalQuantity.setText("Số lượng: " + totalQuantity);
        txtTotalPrice.setText(String.format("Tổng tiền: %,.0f đ", total));

        double discountPercent = 0;
        if (discountCode.equals("5")) discountPercent = 0.05;
        else if (discountCode.equals("10")) discountPercent = 0.10;
        else if (discountCode.equals("25")) discountPercent = 0.25;

        double finalPrice = total * (1 - discountPercent);

        txtDiscountInfo.setText(discountPercent > 0
                ? "Đã áp dụng mã giảm " + discountCode + "%"
                : "Không có mã giảm giá");

        txtTotalAfterDiscount.setText(String.format("Tổng sau giảm: %,.0f đ", finalPrice));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        updateTotal();
    }
}
