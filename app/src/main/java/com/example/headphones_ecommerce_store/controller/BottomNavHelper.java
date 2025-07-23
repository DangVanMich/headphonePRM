package com.example.headphones_ecommerce_store.controller;

import android.app.Activity;
import android.content.Intent;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.ui.product.ProductListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {

    public static void setupBottomNav(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(selectedItemId);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.menu_home && selectedItemId != R.id.menu_home) {
                intent = new Intent(activity, MainHomeActivity.class);
            } else if (itemId == R.id.menu_profile && selectedItemId != R.id.menu_profile) {
                intent = new Intent(activity, ProfileActivity.class);
            } else if (itemId == R.id.menu_cart && selectedItemId != R.id.menu_cart) {
                intent = new Intent(activity, CartActivity.class);
            }
            else if (itemId == R.id.manager_product && selectedItemId != R.id.manager_product) {
                intent = new Intent(activity, ProductListActivity.class);
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0); // Không animation
                return true;
            }

            return true;
        });
    }
}

