package com.example.headphones_ecommerce_store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messageList;
    private final String currentUserEmail;
    private final Context context;

    public MessageAdapter(Context context, List<Message> messageList, String currentUserEmail) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserEmail = currentUserEmail;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == 1) ? R.layout.item_message_sent : R.layout.item_message_received;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder.tvContent != null && holder.tvTimestamp != null) {
            holder.tvContent.setText(message.getContent());
            holder.tvTimestamp.setText(message.getTimestamp());
        } else {
            android.util.Log.e("MessageAdapter", "ViewHolder components not found at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        // Tin nhắn từ người dùng (currentUserEmail) hiển thị bên phải (type 1)
        // Tin nhắn từ hệ thống (support@example.com) hiển thị bên trái (type 0)
        return message.getSender().equals(currentUserEmail) ? 1 : 0;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            if (tvContent == null || tvTimestamp == null) {
                android.util.Log.e("MessageViewHolder", "One or both TextViews not found in layout");
            }
        }
    }
}