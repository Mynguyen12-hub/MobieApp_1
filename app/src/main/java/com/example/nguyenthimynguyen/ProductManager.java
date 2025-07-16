package com.example.nguyenthimynguyen;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static List<Product> productList = new ArrayList<>();

    public static void setProductList(List<Product> list) {
        if (list != null) {
            productList = new ArrayList<>(list);
        }
    }

    public static List<Product> getAllProducts() {
        return productList;
    }

    public static Product getProductById(int id) {
        for (Product p : productList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static List<Product> getProductsByCategory(String category) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : productList) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                filtered.add(p);
            }
        }
        return filtered;
    }
}
