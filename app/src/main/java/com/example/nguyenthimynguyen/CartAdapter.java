package com.example.nguyenthimynguyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartList;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(List<Product> list, OnCartChangeListener listener) {
        this.cartList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product p = cartList.get(position);

        holder.imgProduct.setImageResource(p.getImageResId());
        holder.txtProductName.setText(p.getName());
        holder.txtProductSalePrice.setText(String.format("%,.0f đ", p.getSalePrice()));
        holder.txtProductQuantity.setText(String.valueOf(p.getQuantity()));

        if (p.getPrice() == p.getSalePrice()) {
            holder.txtProductOriginalPrice.setVisibility(View.GONE);
        } else {
            holder.txtProductOriginalPrice.setVisibility(View.VISIBLE);
            holder.txtProductOriginalPrice.setText(String.format("%,.0f đ", p.getPrice()));
        }

        holder.btnPlus.setOnClickListener(v -> {
            p.setQuantity(p.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (p.getQuantity() > 1) {
                p.setQuantity(p.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onCartChanged();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            cartList.remove(position);
            notifyItemRemoved(position);
            listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnDelete;
        TextView txtProductName, txtProductSalePrice, txtProductOriginalPrice;
        TextView btnPlus, btnMinus, txtProductQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductSalePrice = itemView.findViewById(R.id.txtProductSalePrice);
            txtProductOriginalPrice = itemView.findViewById(R.id.txtProductOriginalPrice);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
