package com.example.nguyenthimynguyen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ViewHolder> {

    private final List<Product> productList;
    private Context context;

    public CheckoutProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvQuantity, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_checkout_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = productList.get(position);

        // Load ảnh từ tên tài nguyên
        int imageResId = context.getResources().getIdentifier(p.getImageResId(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.imgProduct.setImageResource(imageResId);
        } else {
            holder.imgProduct.setImageResource(R.drawable.anh1); // fallback ảnh mặc định
        }

        holder.tvName.setText(p.getName());
        holder.tvQuantity.setText("x" + p.getQuantity());
        holder.tvPrice.setText(String.format("%,.0f đ", p.getSalePrice() * p.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
