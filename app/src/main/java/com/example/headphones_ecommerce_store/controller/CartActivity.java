package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.DAO.CartDAO;
import com.example.headphones_ecommerce_store.DAO.OrderDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.CartAdapter;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.models.CartItem;
import com.example.headphones_ecommerce_store.ui.auth.LoginActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private Toolbar toolbar;
    private CheckBox cbSelectAll;

    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private long currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        BottomNavHelper.setupBottomNav(this, R.id.menu_cart);

        cartDAO = new CartDAO(this);
        orderDAO = new OrderDAO(this);
        cartItemList = new ArrayList<>();

        currentUserId = getCurrentUserId();
        if (currentUserId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setupViews();
        setupRecyclerView();
        loadCartItems();
        setupListeners();
    }

    private void setupViews() {
        toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Giỏ Hàng");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        cbSelectAll = findViewById(R.id.cbSelectAll);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this, cartItemList, this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartAdapter);
    }

    private void loadCartItems() {
        if (currentUserId == -1) return;

        cartItemList.clear();
        Cursor cursor = cartDAO.getAllCartItems(currentUserId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long productId = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_PRICE));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CART_QUANTITY));
                cartItemList.add(new CartItem(productId, name, price, imageUrl, quantity));
            } while (cursor.moveToNext());
            cursor.close();
        }

        cartAdapter.notifyDataSetChanged();
        onItemSelectionChanged();
    }

    private void setupListeners() {
        btnCheckout.setOnClickListener(v -> {
            List<CartItem> selectedItems = cartItemList.stream()
                    .filter(CartItem::isSelected)
                    .collect(Collectors.toList());

            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
                return;
            }

            double total = 0;
            for (CartItem item : selectedItems) {
                total += item.getPrice() * item.getQuantity();
            }

            boolean success = orderDAO.createOrder(selectedItems, total, currentUserId);

            if (success) {
                for (CartItem item : selectedItems) {
                    cartDAO.deleteItem(item.getProductId(), currentUserId);
                }

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

                StringBuilder summary = new StringBuilder();
                for (CartItem item : selectedItems) {
                    summary.append(item.getName())
                            .append("\nSố lượng: ").append(item.getQuantity())
                            .append("\n\n");
                }

                Intent intent = new Intent(CartActivity.this, PaymentSuccessActivity.class);
                intent.putExtra("EXTRA_ORDER_ID", String.valueOf(System.currentTimeMillis()));
                intent.putExtra("EXTRA_TOTAL_AMOUNT", currencyFormatter.format(total));
                intent.putExtra("EXTRA_ORDER_SUMMARY", summary.toString().trim());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Tạo đơn hàng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                for (CartItem item : cartItemList) {
                    item.setSelected(isChecked);
                }
                cartAdapter.notifyDataSetChanged();
                calculateTotalPrice();
            }
        });
    }

    private void calculateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItemList) {
            if (item.isSelected()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(currencyFormatter.format(total));
    }

    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);
        if (email != null) {
            DBHelper dbHelper = new DBHelper(this);
            return dbHelper.getUserIdByEmail(email);
        }
        return -1;
    }

    @Override
    public void onQuantityChanged(CartItem item) {
        if (currentUserId != -1) {
            cartDAO.updateQuantity(item.getProductId(), item.getQuantity(), currentUserId);
            calculateTotalPrice();
        }
    }

    @Override
    public void onItemDeleted(CartItem item, int position) {
        if (currentUserId != -1) {
            cartDAO.deleteItem(item.getProductId(), currentUserId);
            cartItemList.remove(position);
            cartAdapter.notifyItemRemoved(position);
            cartAdapter.notifyItemRangeChanged(position, cartItemList.size());
            calculateTotalPrice();
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelectionChanged() {
        calculateTotalPrice();
        boolean allChecked = !cartItemList.isEmpty();
        for (CartItem item : cartItemList) {
            if (!item.isSelected()) {
                allChecked = false;
                break;
            }
        }
        if (cbSelectAll.isChecked() != allChecked) {
            cbSelectAll.setChecked(allChecked);
        }
    }
}
