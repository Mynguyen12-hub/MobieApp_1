package com.example.nguyenthimynguyen;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Product> cartItems = new ArrayList<>();

    public static void addToCart(Product product) {
        boolean exists = false;
        for (Product p : cartItems) {
            if (p.getName().equals(product.getName())) {
                p.setQuantity(p.getQuantity() + product.getQuantity());
                exists = true;
                break;
            }
        }
        if (!exists) {
            cartItems.add(product);
        }
    }

    public static List<Product> getCartItems() {
        return cartItems;
    }

    public static int getCartSize() {
        int count = 0;
        for (Product p : cartItems) {
            count += p.getQuantity();
        }
        return count;
    }

    public static void clearCart() {
        cartItems.clear();
    }

    public static void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
        }
    }
}
