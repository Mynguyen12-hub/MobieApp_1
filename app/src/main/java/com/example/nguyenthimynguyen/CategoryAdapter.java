package com.example.nguyenthimynguyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }

    public CategoryAdapter(List<Category> list, OnCategoryClickListener listener) {
        this.categoryList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtName.setText(category.getName());

        // Đổi màu theo trạng thái chọn
        if (category.isSelected()) {
            holder.txtName.setBackgroundResource(R.drawable.bg_category_selected); // bo góc + màu hồng
            holder.txtName.setTextColor(Color.WHITE);
        } else {
            holder.txtName.setBackgroundResource(R.drawable.bg_category_unselected); // bo góc + xanh nhạt
            holder.txtName.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            // Cập nhật trạng thái chọn duy nhất 1 danh mục
            for (int i = 0; i < categoryList.size(); i++) {
                categoryList.get(i).setSelected(i == position);
            }
            notifyDataSetChanged();
            listener.onCategoryClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}
