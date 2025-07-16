package com.example.nguyenthimynguyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

        holder.imgProduct.setImageResource(product.getImageResId(holder.itemView.getContext()));
        holder.txtName.setText(product.getName());
        holder.txtDescription.setText(product.getDescription());
        holder.txtQuantity.setText(String.valueOf(product.getQuantity()));

        double itemTotal = product.getSalePrice() * product.getQuantity();
        holder.txtPrice.setText(String.format("%,.0f đ", itemTotal));

        // ✅ Gán trạng thái checkbox theo product
        holder.checkboxSelect.setChecked(product.isSelected());

        // ✅ Khi tick checkbox -> cập nhật trạng thái vào product
        holder.checkboxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setSelected(isChecked);
            CartManager.saveCart(); // Lưu lại nếu cần
            onCartChanged.run();    // Cập nhật tổng tiền
        });

        holder.btnPlus.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            CartManager.saveCart();
            notifyItemChanged(position);
            onCartChanged.run();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                CartManager.saveCart();
                notifyItemChanged(position);
                onCartChanged.run();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            cartItems.remove(position);
            CartManager.removeItemById(product.getId());
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            onCartChanged.run();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ✅ Lấy danh sách sản phẩm đã chọn
    public List<Product> getSelectedItems() {
        List<Product> selected = new java.util.ArrayList<>();
        for (Product p : cartItems) {
            if (p.isSelected()) {
                selected.add(p);
            }
        }
        return selected;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDescription, txtQuantity, txtPrice;
        Button btnPlus, btnMinus;
        ImageButton btnRemove;
        CheckBox checkboxSelect;

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
            checkboxSelect = itemView.findViewById(R.id.checkboxSelect);
        }
    }
}
