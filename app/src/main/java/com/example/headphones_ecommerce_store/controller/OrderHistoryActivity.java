package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.DAO.OrderDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.OrderAdapter;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerOrder;
    BottomNavigationView bottomNav;
    private OrderDAO orderDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Khởi tạo DAO
        orderDAO = new OrderDAO(this);

        // Ánh xạ RecyclerView
        recyclerOrder = findViewById(R.id.recyclerOrder);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));

        // Tải dữ liệu từ database và hiển thị
        List<Order> orders = loadOrdersFromDatabase();
        recyclerOrder.setAdapter(new OrderAdapter(orders));

        // Cài đặt thanh điều hướng dưới cùng
        setupBottomNav();
    }

    /**
     * Tải tất cả đơn hàng từ cơ sở dữ liệu và chuyển đổi thành danh sách các đối tượng Order.
     * @return Danh sách các đơn hàng.
     */
    private List<Order> loadOrdersFromDatabase() {
        List<Order> orderList = new ArrayList<>();
        Cursor orderCursor = orderDAO.getAllOrders();

        if (orderCursor != null && orderCursor.moveToFirst()) {
            do {
                long orderId = orderCursor.getLong(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_ID));
                String date = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_DATE));
                double total = orderCursor.getDouble(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_TOTAL));
                String status = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_STATUS));

                // Lấy chi tiết sản phẩm cho mỗi đơn hàng
                StringBuilder productsString = new StringBuilder();
                Cursor itemCursor = orderDAO.getOrderItems(orderId);
                if (itemCursor != null && itemCursor.moveToFirst()) {
                    do {
                        String name = itemCursor.getString(itemCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_ITEM_PRODUCT_NAME));
                        int quantity = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_ITEM_QUANTITY));
                        productsString.append("- ").append(name).append(" (x").append(quantity).append(")\n");
                    } while (itemCursor.moveToNext());
                    itemCursor.close();
                }

                // Định dạng lại tổng tiền
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String formattedTotal = currencyFormatter.format(total);

                // Thêm đơn hàng vào danh sách
                orderList.add(new Order(String.valueOf(orderId), date, formattedTotal, status, productsString.toString().trim()));
            } while (orderCursor.moveToNext());
            orderCursor.close();
        }
        return orderList;
    }

    /**
     * Cài đặt logic cho thanh điều hướng dưới cùng (BottomNavigationView).
     */
    private void setupBottomNav() {
        bottomNav = findViewById(R.id.bottomNavigationView);
        // Đánh dấu mục "Cá nhân" là đang được chọn
        bottomNav.setSelectedItemId(R.id.menu_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(OrderHistoryActivity.this, MainHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;

            } else if (itemId == R.id.menu_profile) {
                // Đã ở màn hình Lịch sử (thuộc mục Cá nhân), không làm gì
                return true;

            } else if (itemId == R.id.menu_cart) {
                Intent intent = new Intent(OrderHistoryActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}
