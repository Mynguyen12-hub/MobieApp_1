package com.example.nguyenthimynguyen;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final String PREF_NAME = "cart_prefs";
    private static final String CART_KEY = "cart_items";
    private static SharedPreferences sharedPreferences;

    // Danh sách giỏ hàng chính
    private static List<Product> cartList = new ArrayList<>();

    // Danh sách sản phẩm được chọn (khi thanh toán hoặc "mua ngay")
    private static List<Product> selectedItems = new ArrayList<>();

    // Khởi tạo SharedPreferences và load dữ liệu
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadCart();
    }

    // Lấy toàn bộ sản phẩm trong giỏ
    public static List<Product> getCart() {
        return new ArrayList<>(cartList); // Trả bản sao để tránh thay đổi ngoài ý muốn
    }

    // Thêm sản phẩm vào giỏ
    public static void addItem(Product product) {
        for (Product p : cartList) {
            if (p.getId() == product.getId()) {
                p.setQuantity(p.getQuantity() + product.getQuantity());
                saveCart();
                return;
            }
        }
        cartList.add(product);
        saveCart();
    }

    // Xóa sản phẩm theo ID
    public static void removeItemById(int productId) {
        cartList.removeIf(p -> p.getId() == productId);
        saveCart();
    }

    // Xóa toàn bộ giỏ hàng
    public static void clearCart() {
        cartList.clear();
        saveCart();
    }

    // Trả danh sách hiện tại
    public static List<Product> getCartItems() {
        return cartList;
    }
    public static int getCartSize() {
        return cartList.size();
    }

    // Lưu dữ liệu giỏ hàng vào SharedPreferences (thủ công, không dùng Gson)
    public static void saveCart() {
        StringBuilder builder = new StringBuilder();
        for (Product p : cartList) {
            builder.append(p.getId()).append(",").append(p.getQuantity()).append(";");
        }
        sharedPreferences.edit().putString(CART_KEY, builder.toString()).apply();
    }

    // Load dữ liệu từ SharedPreferences
    public static void loadCart() {
        cartList.clear();
        String saved = sharedPreferences.getString(CART_KEY, "");
        if (!saved.isEmpty()) {
            String[] items = saved.split(";");
            for (String item : items) {
                String[] parts = item.split(",");
                if (parts.length == 2) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        int quantity = Integer.parseInt(parts[1]);
                        Product product = ProductManager.getProductById(id);
                        if (product != null) {
                            product.setQuantity(quantity);
                            cartList.add(product);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    // Set sản phẩm được chọn (ví dụ từ giỏ hoặc từ "mua ngay")
    public static void setSelectedItems(List<Product> items) {
        selectedItems = items;
    }

    // Lấy sản phẩm đã chọn (dùng ở CheckoutActivity)
    public static List<Product> getSelectedItems() {
        return selectedItems;
    }
}
