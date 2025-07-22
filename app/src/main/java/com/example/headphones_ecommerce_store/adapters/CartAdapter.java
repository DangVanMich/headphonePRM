package com.example.headphones_ecommerce_store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.models.CartItem;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;
    private CartItemListener listener;

    // Interface để gửi sự kiện ra Activity
    public interface CartItemListener {
        void onQuantityChanged(CartItem item);
        void onItemDeleted(CartItem item, int position);
        void onItemSelectionChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItemList, CartItemListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelectItem;
        ImageView ivCartProductImage, ivCartMinus, ivCartPlus, ivDeleteItem;
        TextView tvCartProductName, tvCartProductPrice, tvCartQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelectItem = itemView.findViewById(R.id.cbSelectItem);
            ivCartProductImage = itemView.findViewById(R.id.ivCartProductImage);
            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            ivCartMinus = itemView.findViewById(R.id.ivCartMinus);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            ivCartPlus = itemView.findViewById(R.id.ivCartPlus);
            ivDeleteItem = itemView.findViewById(R.id.ivDeleteItem);
        }

        public void bind(final CartItem item) {
            tvCartProductName.setText(item.getName());
            tvCartQuantity.setText(String.valueOf(item.getQuantity()));
            cbSelectItem.setChecked(item.isSelected());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvCartProductPrice.setText(currencyFormatter.format(item.getPrice()));

            Glide.with(context).load(item.getImageUrl()).into(ivCartProductImage);

            // Bỏ listener cũ để tránh gọi 2 lần, chỉ xử lý khi người dùng nhấn
            cbSelectItem.setOnCheckedChangeListener(null);
            cbSelectItem.setChecked(item.isSelected());
            cbSelectItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (buttonView.isPressed()) {
                    item.setSelected(isChecked);
                    listener.onItemSelectionChanged();
                }
            });

            // Sự kiện tăng số lượng
            ivCartPlus.setOnClickListener(v -> {
                int quantity = item.getQuantity();
                quantity++;
                item.setQuantity(quantity);
                tvCartQuantity.setText(String.valueOf(quantity));
                listener.onQuantityChanged(item);
            });

            // Sự kiện giảm số lượng
            ivCartMinus.setOnClickListener(v -> {
                int quantity = item.getQuantity();
                if (quantity > 1) {
                    quantity--;
                    item.setQuantity(quantity);
                    tvCartQuantity.setText(String.valueOf(quantity));
                    listener.onQuantityChanged(item);
                }
            });

            // Sự kiện xóa sản phẩm
            ivDeleteItem.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemDeleted(cartItemList.get(position), position);
                }
            });
        }
    }
}
