package com.example.headphones_ecommerce_store.controller;

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
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.CartAdapter;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.models.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private Toolbar toolbar;
    private CheckBox cbSelectAll;

    private CartDAO cartDAO;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartDAO = new CartDAO(this);
        cartItemList = new ArrayList<>();

        setupViews();
        setupRecyclerView();
        loadCartItems();
        setupListeners();
    }

    private void setupViews() {
        toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        // ... logic loadCartItems cũ ...
    }

    private void setupListeners() {
        btnCheckout.setOnClickListener(v -> { /* ... */ });

        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (CartItem item : cartItemList) {
                item.setSelected(isChecked);
            }
            cartAdapter.notifyDataSetChanged();
            calculateTotalPrice();
        });
    }

    private void calculateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItemList) {
            if (item.isSelected()) { // Chỉ tính tổng các item được chọn
                total += item.getPrice() * item.getQuantity();
            }
        }
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(currencyFormatter.format(total));
    }

    @Override
    public void onQuantityChanged(CartItem item) {
        cartDAO.updateQuantity(item.getProductId(), item.getQuantity());
        calculateTotalPrice();
    }

    @Override
    public void onItemDeleted(CartItem item, int position) {
        // ... logic cũ ...
    }

    @Override
    public void onItemSelectionChanged() {
        calculateTotalPrice();
        // Kiểm tra xem có phải tất cả item đều được chọn không
        boolean allChecked = true;
        for (CartItem item : cartItemList) {
            if (!item.isSelected()) {
                allChecked = false;
                break;
            }
        }
        cbSelectAll.setChecked(allChecked);
    }
}
