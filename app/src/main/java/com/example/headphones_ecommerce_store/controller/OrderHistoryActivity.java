package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.DAO.OrderDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.OrderAdapter;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.ui.auth.LoginActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerOrder;
    private OrderDAO orderDAO;
    private long currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        BottomNavHelper.setupBottomNav(this, R.id.menu_profile);

        orderDAO = new OrderDAO(this);


        currentUserId = getCurrentUserId();
        if (currentUserId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem lịch sử đơn hàng", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        recyclerOrder = findViewById(R.id.recyclerOrder);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));

        List<Order> orders = loadOrdersFromDatabase();
        recyclerOrder.setAdapter(new OrderAdapter(orders));

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
    private List<Order> loadOrdersFromDatabase() {
        List<Order> orderList = new ArrayList<>();
        Cursor orderCursor = orderDAO.getAllOrders(currentUserId);

        if (orderCursor != null && orderCursor.moveToFirst()) {
            do {
                long orderId = orderCursor.getLong(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_ID));
                String date = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_DATE));
                double total = orderCursor.getDouble(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_TOTAL));
                String status = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDER_STATUS));

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

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String formattedTotal = currencyFormatter.format(total);

                orderList.add(new Order(String.valueOf(orderId), date, formattedTotal, status, productsString.toString().trim()));
            } while (orderCursor.moveToNext());
            orderCursor.close();
        }
        return orderList;
    }

}
