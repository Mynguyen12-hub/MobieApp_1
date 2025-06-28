package com.example.nguyenthimynguyen;

import java.util.Objects;

public class Product {
    private String name;
    private int imageResId;
    private double price;         // Giá gốc
    private double salePrice;     // Giá khuyến mãi
    private String description;
    private float rating;
    private int sold;
    private int quantity = 1;
    private String category;
    private int id;               // Mỗi sản phẩm có ID duy nhất

    // Constructor đầy đủ, chính xác
    public Product(String name, int imageResId, double price, double salePrice,
                   String description, float rating, int sold, int id, String category) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.salePrice = salePrice;
        this.description = description;
        this.rating = rating;
        this.sold = sold;
        this.id = id;
        this.category = category;
    }

    // Phương thức clone() trả về bản sao Product
    public Product clone() {
        Product p = new Product(name, imageResId, price, salePrice, description, rating, sold, id, category);
        p.setQuantity(this.quantity);
        return p;
    }

    // Getter & Setter
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public double getPrice() {
        return price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public int getSold() {
        return sold;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
