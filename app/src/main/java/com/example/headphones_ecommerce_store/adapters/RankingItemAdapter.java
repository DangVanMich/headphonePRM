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
import com.example.headphones_ecommerce_store.models.RankingItem;

import java.util.List;

public class RankingItemAdapter extends RecyclerView.Adapter<RankingItemAdapter.ViewHolder> {
    private final List<RankingItem> itemList;
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(RankingItem item);
    }

    public RankingItemAdapter(Context context, List<RankingItem> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    Log.d("AdapterClick", "RankingAdapter: Item clicked at position " + position);
                    listener.onItemClick(itemList.get(position));
                }
            });

        }
    }

    @NonNull
    @Override
    public RankingItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingItemAdapter.ViewHolder holder, int position) {
        RankingItem item = itemList.get(position);
        holder.txtName.setText((position + 1) + ". " + item.getName());
        holder.txtPrice.setText("$" + item.getPrice());

        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

