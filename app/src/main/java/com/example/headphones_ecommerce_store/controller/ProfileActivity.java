package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.ui.auth.LoginActivity;
import com.example.headphones_ecommerce_store.ui.support.SupportActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private LinearLayout optionPersonalInfo, optionOrderHistory, optionSupport, optionLogout;
    private TextView tvUserName, tvUserOccupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        setupBottomNav();

        // Initialize UI elements
        optionPersonalInfo = findViewById(R.id.optionPersonalInfo);
        optionOrderHistory = findViewById(R.id.optionOrderHistory);
        optionSupport = findViewById(R.id.optionSupport);
        optionLogout = findViewById(R.id.optionLogout);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserOccupation = findViewById(R.id.tvUserOccupation);

        // Lấy tên người dùng từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userFullName = prefs.getString("userFullName", "Khách");
        tvUserName.setText(userFullName);

        // (Tùy chọn) Lấy nghề nghiệp nếu có
        String userOccupation = prefs.getString("userOccupation", "Người làm tự do");
        tvUserOccupation.setText(userOccupation);


        // Click listeners
        optionPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileDetailActivity.class);
                startActivity(intent);
            }
        });

        optionOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        optionSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SupportActivity.class);
                startActivity(intent);
            }
        });

        optionLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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
                return true;
            }
            return false;
        });
    }
}