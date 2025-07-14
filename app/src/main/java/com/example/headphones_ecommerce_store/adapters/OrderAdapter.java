package com.example.headphones_ecommerce_store.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.controller.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtDate, txtTotal, txtStatus, txtProducts;

        public OrderViewHolder(View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtProducts = itemView.findViewById(R.id.txtProducts);
        }
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order o = orders.get(position);
        holder.txtOrderId.setText("Mã đơn hàng: " + o.orderId);
        holder.txtDate.setText("Ngày đặt: " + o.date);
        holder.txtTotal.setText("Tổng tiền: " + o.total);
        holder.txtStatus.setText("Trạng thái: " + o.status);
        holder.txtProducts.setText("Sản phẩm:\n" + o.products);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}

