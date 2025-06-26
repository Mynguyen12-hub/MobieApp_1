package com.example.nguyenthimynguyen;

public class Category {
    private String name;
    private boolean isSelected;

    public Category(String name) {
        this.name = name;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

