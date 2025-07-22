package com.example.headphones_ecommerce_store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.models.HeadphoneInfo;

import java.util.List;

public class HeadphoneAdapter extends RecyclerView.Adapter<HeadphoneAdapter.ViewHolder> {
    private List<HeadphoneInfo> headphoneList;
    private Context context;

    public HeadphoneAdapter(Context context, List<HeadphoneInfo> headphoneList) {
        this.context = context;
        this.headphoneList = headphoneList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, brand, price;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgHeadphone);
            name = itemView.findViewById(R.id.txtName);
            brand = itemView.findViewById(R.id.txtBrand);
            price = itemView.findViewById(R.id.txtPrice);
        }
    }

    @Override
    public HeadphoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_headphone, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeadphoneAdapter.ViewHolder holder, int position) {
        HeadphoneInfo item = headphoneList.get(position);
        holder.name.setText(item.getName());
        holder.brand.setText(item.getBrand());
        holder.price.setText("$" + item.getPrice());

        Glide.with(context)
                .load(item.getImageUrl())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return headphoneList.size();
    }
}

