package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso; // For loading profile image, add dependency if not present

public class ProfileDetailActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    private DBHelper dbHelper;
    private TextView tvNameHeader, tvLocationHeader, tvName, tvGender, tvDob, tvAge, tvAddress, tvPhone, tvEmail, tvRegisteredSince;
    private ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        getSupportActionBar().hide();

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize UI elements
        ivProfileImage = findViewById(R.id.ivProfileImage); // Add this ID to ImageView
        tvNameHeader = findViewById(R.id.tvNameHeader);
        tvLocationHeader = findViewById(R.id.tvLocationHeader);
        tvName = findViewById(R.id.tvName);
        tvGender = findViewById(R.id.tvGender);
        tvDob = findViewById(R.id.tvDob);
        tvAge = findViewById(R.id.tvAge);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvRegisteredSince = findViewById(R.id.tvRegisteredSince);

        // Get email from Intent
        String email = getIntent().getStringExtra("user_email");
        if (email != null) {
            // Fetch user details
            User user = dbHelper.getUserByEmail(email);
            if (user != null) {
                // Update UI with user data
                tvNameHeader.setText(user.getDisplayName() != null ? user.getDisplayName() : "Unknown");
                tvLocationHeader.setText("Viet Nam"); // Static for now
                tvName.setText("Name: " + (user.getDisplayName() != null ? user.getDisplayName() : "Unknown"));
                tvEmail.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "Not set"));
                // Load profile image if available
                if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                    Picasso.get().load(user.getProfileImageUrl()).into(ivProfileImage);
                } else {
                    ivProfileImage.setImageResource(R.drawable.ic_placeholder); // Default image
                }
                // Placeholder for other fields (to be fetched or set from DB/model)
                tvGender.setText("Gender: Not set"); // Add to DB if needed
                tvDob.setText("Date of Birth: Not set"); // Add to DB if needed
                tvAge.setText("Age: Not set"); // Calculate or add to DB
                tvAddress.setText("Address: Not set"); // Fetch from shippingAddresses
                tvPhone.setText("Phone: Not set"); // Add to DB if needed
                tvRegisteredSince.setText("Registered since: " + (user.getRegistrationDate() != null ? user.getRegistrationDate() : "Not set"));
                // Note: registrationDate is not a field in User yet; add a getter if needed
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No user email found", Toast.LENGTH_SHORT).show();
        }

        setupBottomNav();
    }

    private void setupBottomNav() {
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.menu_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(ProfileDetailActivity.this, MainHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;

            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(ProfileDetailActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}