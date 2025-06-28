package com.example.nguyenthimynguyen;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "cart_data";
    private static final String KEY_CART = "cart_list";
    private static Context appContext;
    private static List<Product> cart = new ArrayList<>();

    public static void init(Context context) {
        appContext = context.getApplicationContext(); // Dùng context toàn app
        loadCart();
    }

    public static List<Product> getCart() {
        return new ArrayList<>(cart); // Trả về bản sao để tránh bị sửa trực tiếp
    }

    public static void addItem(Product product) {
        for (Product p : cart) {
            if (p.getId() == product.getId()) {
                p.setQuantity(p.getQuantity() + product.getQuantity()); // cộng thêm số lượng
                saveCart();
                return;
            }
        }
        Product copy = product.clone(); // dùng clone để không ảnh hưởng danh sách gốc
        cart.add(copy);
        saveCart();
    }

    public static void removeItem(int index) {
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
            saveCart();
        }
    }

    public static void clearCart() {
        cart.clear();
        saveCart();
    }

    public static int getCartSize() {
        int total = 0;
        for (Product p : cart) {
            total += p.getQuantity();
        }
        return total;
    }

    public static void saveCart() {
        if (appContext == null) return;

        SharedPreferences prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        StringBuilder sb = new StringBuilder();
        for (Product p : cart) {
            sb.append(p.getId()).append("|")
                    .append(p.getQuantity()).append(";");
        }

        editor.putString(KEY_CART, sb.toString());
        editor.apply();
    }

    public static void loadCart() {
        if (appContext == null) return;

        SharedPreferences prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String saved = prefs.getString(KEY_CART, "");

        cart.clear();

        if (!saved.isEmpty()) {
            String[] items = saved.split(";");
            for (String item : items) {
                String[] parts = item.split("\\|");
                if (parts.length == 2) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        int quantity = Integer.parseInt(parts[1]);

                        Product product = ProductRepository.getProductById(id);
                        if (product != null) {
                            Product copy = product.clone();
                            copy.setQuantity(quantity);
                            cart.add(copy);
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
    }

    // ✅ Hàm tính tổng tiền trong giỏ hàng
    public static double getTotalAmount() {
        double total = 0;
        for (Product p : cart) {
            total += p.getSalePrice() * p.getQuantity();
        }
        return total;
    }
}
