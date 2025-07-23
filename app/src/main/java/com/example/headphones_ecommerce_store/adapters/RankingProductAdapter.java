package com.example.headphones_ecommerce_store.adapters;

import android.content.Context;
import android.util.Log;
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
import com.example.headphones_ecommerce_store.models.RankingFragment;

import java.util.List;

public class RankingProductAdapter extends RecyclerView.Adapter<RankingProductAdapter.ViewHolder> {
    private  List<Product> productList;
    private  Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product item);
    }

    public RankingProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtRating = itemView.findViewById(R.id.txtProductRating);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    Log.d("AdapterClick", "RankingAdapter: Item clicked at position " + position);
                    listener.onItemClick(productList.get(position));
                }
            });
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
