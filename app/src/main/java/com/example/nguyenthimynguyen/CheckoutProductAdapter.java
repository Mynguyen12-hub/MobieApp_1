package com.example.nguyenthimynguyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;    // thêm import
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ViewHolder> {

    private List<Product> productList;

    public CheckoutProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;   // thêm ImageView
        TextView tvName, tvQuantity, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);  // ánh xạ ImageView
            tvName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }

    @NonNull
    @Override
    public CheckoutProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutProductAdapter.ViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.imgProduct.setImageResource(p.getImageResId());  // set ảnh
        holder.tvName.setText(p.getName());
        holder.tvQuantity.setText("x" + p.getQuantity());
        holder.tvPrice.setText(String.format("%,.0f đ", p.getSalePrice() * p.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
