package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> list) {
        this.productList = list;
    }

    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
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

        holder.btnBuyNow.setOnClickListener(v -> {
            Product selectedProduct = productList.get(holder.getAdapterPosition());
            CartManager.addToCart(selectedProduct);
            Toast.makeText(v.getContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(v.getContext(), CartActivity.class);
            v.getContext().startActivity(intent);
        });

        holder.btnDetail.setOnClickListener(v -> {
            Product pDetail = productList.get(holder.getAdapterPosition());
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("name", pDetail.getName());
            intent.putExtra("imageResId", pDetail.getImageResId());
            intent.putExtra("price", pDetail.getPrice());
            intent.putExtra("salePrice", pDetail.getSalePrice());
            intent.putExtra("description", pDetail.getDescription());
            intent.putExtra("rating", pDetail.getRating());
            intent.putExtra("sold", pDetail.getSold());
            intent.putExtra("category", pDetail.getCategory());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductSalePrice, txtProductOriginalPrice, txtProductDescription, txtRating, txtSold;
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
