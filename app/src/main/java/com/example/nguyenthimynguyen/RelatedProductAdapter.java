package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = relatedList.get(position);

        holder.txtProductName.setText(p.getName());
        holder.txtProductPrice.setText(String.format("%,.0f đ", p.getSalePrice()));
        holder.imgProduct.setImageResource(p.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("name", p.getName());
            intent.putExtra("imageResId", p.getImageResId());
            intent.putExtra("price", p.getPrice());
            intent.putExtra("salePrice", p.getSalePrice());
            intent.putExtra("description", p.getDescription());
            intent.putExtra("rating", p.getRating());
            intent.putExtra("sold", p.getSold());
            intent.putExtra("category", p.getCategory());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return relatedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtProductName, txtProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
        }
    }
}
