package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.ViewHolder> {

    private List<Product> relatedList;

    public RelatedProductAdapter(List<Product> list) {
        this.relatedList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_related_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = relatedList.get(position);

        holder.txtProductName.setText(p.getName());
        holder.txtProductPrice.setText(String.format("%,.0f đ", p.getSalePrice()));
        holder.imgProduct.setImageResource(p.getImageResId(holder.itemView.getContext()));

        // 👉 Nút "Chi tiết" - mở lại ProductDetailActivity
        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("product_id", p.getId());  // Key phải trùng với ProductDetailActivity
            v.getContext().startActivity(intent);
        });

        // 👉 Nút "Mua" - thêm vào giỏ và chuyển sang CartActivity
        holder.btnBuyNow.setOnClickListener(v -> {
            CartManager.clearCart(); // Clear giỏ cũ nếu cần
            CartManager.addItem(p);  // Thêm sản phẩm được chọn
            CartUtils.updateCartCount(null);
            Intent intent = new Intent(v.getContext(), CartActivity.class);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return relatedList != null ? relatedList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtProductName, txtProductPrice;
        Button btnViewDetail, btnBuyNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);
        }
    }
}
