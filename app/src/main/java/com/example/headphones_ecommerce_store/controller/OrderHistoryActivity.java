package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.OrderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerOrder;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        getSupportActionBar().hide();

//        Toolbar toolbar = findViewById(R.id.toolbarOrderHistory);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("OrderHistory");
//
//        toolbar.setNavigationOnClickListener(v -> finish());


        recyclerOrder = findViewById(R.id.recyclerOrder);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));

        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1743144030851", "3/28/2025, 1:40:30 PM", "95000", "completed", "- 100 Truyện Thuyết Đô Thị Kinh Dị Đài Loan (x1): 95000"));
        orders.add(new Order("1743144052406", "3/28/2025, 1:40:52 PM", "130000", "completed", "- Chuyện Con Mèo Dạy Hải Âu Bay (x2): 130000"));
        orders.add(new Order("1743144066272", "3/28/2025, 1:41:06 PM", "97000", "completed", "- Bóng Ma Dưới Đáy Giếng (x1): 97000"));

        recyclerOrder.setAdapter(new OrderAdapter(orders));
        setupBottomNav();
    }
    private void setupBottomNav() {
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.menu_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(OrderHistoryActivity.this, MainHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;

            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(OrderHistoryActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;

//            } else if (itemId == R.id.menu_cart) {
//                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//                return true;
//
//            } else if (itemId == R.id.menu_map) {
//                Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//                return true;
            }

            return false;
        });
    }
}

