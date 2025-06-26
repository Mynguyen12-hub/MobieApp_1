package com.example.nguyenthimynguyen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private TextView txtSubtotal, txtShipping, txtTotal;
    private EditText edtPromo;
    private Button btnApply, btnNext;
    private CartAdapter cartAdapter;
    private List<Product> cartList;

    private final double SHIPPING_FEE = 15000;
    private double discount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCart = findViewById(R.id.rvCart);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtShipping = findViewById(R.id.txtShipping);
        txtTotal = findViewById(R.id.txtTotal);
        edtPromo = findViewById(R.id.edtPromo);
        btnApply = findViewById(R.id.btnApply);
        btnNext = findViewById(R.id.btnNext);

        // ✅ Sửa ở đây
        cartList = CartManager.getCartItems();

        cartAdapter = new CartAdapter(cartList, new CartAdapter.OnCartChangeListener() {
            @Override
            public void onCartChanged() {
                updatePrices();
            }
        });

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        updatePrices();

        btnApply.setOnClickListener(v -> applyPromo());

        btnNext.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển sang thanh toán...", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, PaymentActivity.class));
        });
    }

    private void applyPromo() {
        String promoCode = edtPromo.getText().toString().trim().toUpperCase();

        if (promoCode.equals("GIAM10")) {
            discount = 0.1;
            Toast.makeText(this, "Áp dụng mã giảm 10%", Toast.LENGTH_SHORT).show();
        } else if (promoCode.equals("FREESHIP")) {
            discount = 0;
            Toast.makeText(this, "Miễn phí vận chuyển", Toast.LENGTH_SHORT).show();
        } else {
            discount = 0;
            Toast.makeText(this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        updatePrices();
    }

    private void updatePrices() {
        double subtotal = 0;
        for (Product p : cartList) {
            subtotal += p.getSalePrice() * p.getQuantity();
        }

        double shipping = edtPromo.getText().toString().equalsIgnoreCase("FREESHIP") ? 0 : SHIPPING_FEE;
        double discounted = subtotal * (1 - discount);
        double total = discounted + shipping;

        txtSubtotal.setText(String.format("Subtotal: %,.0f đ", subtotal));
        txtShipping.setText(String.format("Shipping: %,.0f đ", shipping));
        txtTotal.setText(String.format("Total: %,.0f đ", total));
    }
}
