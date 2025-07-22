package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private BottomNavigationView bottomNav;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartDAO = new CartDAO(this);
        orderDAO = new OrderDAO(this);
        cartItemList = new ArrayList<>();

        setupViews();
        setupRecyclerView();
        loadCartItems();
        setupListeners();
        setupBottomNav();
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
        bottomNav = findViewById(R.id.bottomNavigationView);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this, cartItemList, this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartAdapter);
    }

    private void loadCartItems() {
        cartItemList.clear();
        Cursor cursor = cartDAO.getAllCartItems();

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
        onItemSelectionChanged(); // Cập nhật lại trạng thái checkbox và tổng tiền
    }

    private void setupListeners() {
        btnCheckout.setOnClickListener(v -> {
            // Lấy danh sách các sản phẩm được chọn
            List<CartItem> selectedItems = cartItemList.stream()
                    .filter(CartItem::isSelected)
                    .collect(Collectors.toList());

            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tính tổng tiền
            double total = 0;
            for (CartItem item : selectedItems) {
                total += item.getPrice() * item.getQuantity();
            }

            // Lưu đơn hàng vào DB
            boolean success = orderDAO.createOrder(selectedItems, total);

            if (success) {
                // Xóa các sản phẩm đã thanh toán khỏi giỏ hàng
                for (CartItem item : selectedItems) {
                    cartDAO.deleteItem(item.getProductId());
                }

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

                // Tạo tóm tắt đơn hàng để gửi đi
                StringBuilder summary = new StringBuilder();
                for (CartItem item : selectedItems) {
                    summary.append(item.getName())
                            .append("\nSố lượng: ").append(item.getQuantity())
                            .append("\n\n");
                }

                // Chuyển đến màn hình thành công
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
        cartDAO.deleteItem(item.getProductId());
        cartItemList.remove(position);
        cartAdapter.notifyItemRemoved(position);
        cartAdapter.notifyItemRangeChanged(position, cartItemList.size());
        calculateTotalPrice();
        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelectionChanged() {
        calculateTotalPrice();
        // Kiểm tra xem có phải tất cả item đều được chọn không
        boolean allChecked = !cartItemList.isEmpty();
        for (CartItem item : cartItemList) {
            if (!item.isSelected()) {
                allChecked = false;
                break;
            }
        }
        // Cập nhật checkbox "Chọn tất cả" mà không kích hoạt lại listener của nó
        if (cbSelectAll.isChecked() != allChecked) {
            cbSelectAll.setChecked(allChecked);
        }
    }
    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.menu_cart); // Đánh dấu mục Giỏ hàng là đang được chọn

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(this, MainHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.menu_cart) {
                // Đã ở màn hình Giỏ hàng, không làm gì
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(this, ProfileActivity.class); // Giả sử có ProfileActivity
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
