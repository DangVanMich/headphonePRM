package com.example.headphones_ecommerce_store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.model.Product;

import java.util.List;

public class RankingProductAdapter extends RecyclerView.Adapter<RankingProductAdapter.ViewHolder> {
    private final List<Product> productList;
    private final Context context;

    public RankingProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtRating = itemView.findViewById(R.id.txtProductRating);
        }
    }

    @NonNull
    @Override
    public RankingProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingProductAdapter.ViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.txtName.setText((position + 1) + ". " + p.getName());
        holder.txtPrice.setText(String.format("$%.2f", p.getPrice()));
        holder.txtRating.setText(String.format("★ %.1f", p.getAverageRating()));

        Glide.with(context)
                .load(p.getThumbnailImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
