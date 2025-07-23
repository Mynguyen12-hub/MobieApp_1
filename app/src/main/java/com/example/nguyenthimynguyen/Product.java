package com.example.nguyenthimynguyen;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;
import java.io.Serializable;

public class Product implements Serializable {

    private String name;

    @SerializedName("imageResId") // ✅ ánh xạ đúng với tên trường trong JSON
    private String imageResId;

    private double price;
    private double salePrice;
    private String description;
    private float rating;
    private int sold;
    private int quantity = 1;
    private boolean selected = false;
    private String category;
    private int id;

    // ✅ Constructor đầy đủ
    public Product(String name, String imageResId, double price, double salePrice, String description,
                   float rating, int sold, int id, int quantity, String category) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.salePrice = salePrice;
        this.description = description;
        this.rating = rating;
        this.sold = sold;
        this.id = id;
        this.quantity = quantity;
        this.category = category;
    }

    // ✅ Lấy resource ID ảnh từ tên ảnh
    public int getImageResId(Context context) {
        return context.getResources().getIdentifier(imageResId, "drawable", context.getPackageName());
    }

    // ✅ Clone để tạo bản sao độc lập (dùng khi thêm vào giỏ hàng)
    @Override
    public Product clone() {
        Product p = new Product(name, imageResId, price, salePrice, description, rating, sold, id, quantity, category);
        p.setSelected(this.selected);
        return p;
    }

    // ✅ Getter & Setter
    public String getName() {
        return name;
    }

    public String getImageResId() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    // ✅ equals & hashCode dựa vào id (để so sánh sản phẩm)
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
