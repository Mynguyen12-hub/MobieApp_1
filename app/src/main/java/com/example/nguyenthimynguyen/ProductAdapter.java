package com.example.nguyenthimynguyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> list) {
        this.productList = list;
    }

    // ✅ Thêm hàm này để cập nhật dữ liệu khi tìm kiếm
    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged(); // Cập nhật RecyclerView
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.txtProductName.setText(p.getName());
        holder.txtProductPrice.setText(String.format("%,.0f đ", p.getPrice()));
        holder.imgProduct.setImageResource(p.getImageResId());
        holder.txtProductDescription.setText(p.getDescription());
        holder.txtRating.setText("⭐ " + p.getRating());
        holder.txtSold.setText("• Đã bán " + p.getSold());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtProductDescription, txtRating, txtSold;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductDescription = itemView.findViewById(R.id.txtProductDescription);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtSold = itemView.findViewById(R.id.txtSold);
        }
    }
}
