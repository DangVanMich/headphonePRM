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

public class HeadphoneAdapter extends RecyclerView.Adapter<HeadphoneAdapter.HeadphoneViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public HeadphoneAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HeadphoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_headphone, parent, false);
        return new HeadphoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeadphoneViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class HeadphoneViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHeadphone;
        TextView txtName, txtBrand, txtPrice;

        public HeadphoneViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHeadphone = itemView.findViewById(R.id.imgHeadphone);
            txtName = itemView.findViewById(R.id.txtName);
            txtBrand = itemView.findViewById(R.id.txtBrand);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }

        public void bind(Product product, OnItemClickListener listener) {
            txtName.setText(product.getName());
            txtBrand.setText(product.getBrand() != null ? product.getBrand() : "No brand");
            txtPrice.setText(String.format("$%.2f", product.getPrice()));

            Glide.with(itemView.getContext())
                    .load(product.getThumbnailImageUrl())
                    .into(imgHeadphone);

            itemView.setOnClickListener(v -> listener.onItemClick(product));
        }
    }
}
