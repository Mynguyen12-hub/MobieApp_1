package com.example.nguyenthimynguyen;

import android.view.View;
import android.widget.TextView;

public class CartUtils {
    public static void updateCartCount(TextView tvCartCount) {
        int count = CartManager.getCartSize();
        if (tvCartCount != null) {
            tvCartCount.setText(String.valueOf(count));
            tvCartCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        }
    }
}
