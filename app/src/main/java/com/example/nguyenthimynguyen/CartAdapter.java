package com.example.nguyenthimynguyen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> cartItems;
    private Runnable onCartChanged;

    public CartAdapter(List<Product> cartItems, Runnable onCartChanged) {
        this.cartItems = cartItems;
        this.onCartChanged = onCartChanged;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = cartItems.get(position);

        holder.imgProduct.setImageResource(product.getImageResId());
        holder.txtName.setText(product.getName());
        holder.txtDescription.setText(product.getDescription());
        holder.txtQuantity.setText(String.valueOf(product.getQuantity()));

        double itemTotal = product.getSalePrice() * product.getQuantity();
        holder.txtPrice.setText(String.format("%,.0f đ", itemTotal));

        // ✅ Tăng số lượng
        holder.btnPlus.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            CartManager.saveCart(); // Không cần context
            notifyItemChanged(position);
            onCartChanged.run();
        });

        // ✅ Giảm số lượng
        holder.btnMinus.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                CartManager.saveCart();
                notifyItemChanged(position);
                onCartChanged.run();
            }
        });

        // ✅ Xóa sản phẩm
        holder.btnRemove.setOnClickListener(v -> {
            CartManager.removeItem(position); // Chỉ truyền index
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            onCartChanged.run();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDescription, txtQuantity, txtPrice;
        Button btnPlus, btnMinus;
        ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            txtName = itemView.findViewById(R.id.txtCartName);
            txtDescription = itemView.findViewById(R.id.txtCartDescription);
            txtQuantity = itemView.findViewById(R.id.txtCartQuantity);
            txtPrice = itemView.findViewById(R.id.txtCartPrice);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemoveCartItem);
        }
    }
}
