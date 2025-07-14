package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.headphones_ecommerce_store.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    LinearLayout optionPersonalInfo, optionOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        setupBottomNav();

        optionPersonalInfo = findViewById(R.id.optionPersonalInfo);
        optionPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileDetailActivity.class);
                startActivity(intent);
            }
        });

        optionOrderHistory = findViewById(R.id.optionOrderHistory);
        optionOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });


    }
    private void setupBottomNav() {
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.menu_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(ProfileActivity.this, MainHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;

            } else if (itemId == R.id.menu_profile) {
                // Đã ở profile
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

