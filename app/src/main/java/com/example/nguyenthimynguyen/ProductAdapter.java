package com.example.nguyenthimynguyen;

import android.content.Context;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> list) {
        this.productList = list;
    }

    // Cập nhật danh sách sản phẩm (dùng khi tìm kiếm, lọc)
    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);

        holder.txtProductName.setText(p.getName());
        holder.imgProduct.setImageResource(p.getImageResId());
        holder.txtProductDescription.setText(p.getDescription());
        holder.txtRating.setText("⭐ " + p.getRating());
        holder.txtSold.setText("• Đã bán " + p.getSold());
        holder.txtProductSalePrice.setText(String.format("%,.0f đ", p.getSalePrice()));

        if (p.getPrice() == p.getSalePrice()) {
            holder.txtProductOriginalPrice.setVisibility(View.GONE);
        } else {
            holder.txtProductOriginalPrice.setVisibility(View.VISIBLE);
            holder.txtProductOriginalPrice.setText(String.format("%,.0f đ", p.getPrice()));
        }

        // ✅ Mua ngay: Xóa giỏ cũ, thêm sản phẩm mới, mở CheckoutActivity
        holder.btnBuyNow.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Context context = v.getContext();
                Product selectedProduct = productList.get(pos).clone();
                selectedProduct.setQuantity(1);
                CartManager.clearCart();               // Xóa giỏ cũ
                CartManager.addItem(selectedProduct);  // Thêm sản phẩm mới

                Intent intent = new Intent(context, CheckoutActivity.class);
                context.startActivity(intent);         // Mở giao diện thanh toán
            }
        });

        // ✅ Xem chi tiết sản phẩm
        holder.btnDetail.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Context context = v.getContext();
                Product pDetail = productList.get(pos);

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("id", pDetail.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductSalePrice, txtProductOriginalPrice,
                txtProductDescription, txtRating, txtSold;
        ImageView imgProduct;
        Button btnBuyNow, btnDetail;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductSalePrice = itemView.findViewById(R.id.txtProductSalePrice);
            txtProductOriginalPrice = itemView.findViewById(R.id.txtProductOriginalPrice);
            txtProductDescription = itemView.findViewById(R.id.txtProductDescription);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtSold = itemView.findViewById(R.id.txtSold);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
