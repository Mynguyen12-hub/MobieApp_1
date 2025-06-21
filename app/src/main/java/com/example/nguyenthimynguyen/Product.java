package com.example.nguyenthimynguyen;

public class Product {
    private String name;
    private int imageResId;
    private double price;
    private String description;
    private float rating;
    private int sold;
    private int quantity = 1; // Mặc định mỗi sản phẩm có 1

    public Product(String name, int imageResId, double price, String description, float rating, int sold) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.sold = sold;
        this.quantity = 1;
    }

    // Getter và Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Các getter hiện tại
    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public double getPrice() {
        return price;
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
}
