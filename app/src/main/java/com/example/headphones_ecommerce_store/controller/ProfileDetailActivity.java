package com.example.headphones_ecommerce_store.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.headphones_ecommerce_store.DAO.UserDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso; // For loading profile image, add dependency if not present

public class ProfileDetailActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    private DBHelper dbHelper;
    private TextView tvNameHeader, tvLocationHeader, tvName, tvEmail;
    private ImageView ivProfileImage;
    private Button btnUpdate;
    private UserDAO userDAO = new UserDAO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        getSupportActionBar().hide();

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        ivProfileImage = findViewById(R.id.ivProfileImage); // Add this ID to ImageView
        tvNameHeader = findViewById(R.id.tvNameHeader);
        tvLocationHeader = findViewById(R.id.tvLocationHeader);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnUpdate = findViewById(R.id.btnUpdateInfo);

        // Lấy tên từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = prefs.getString("userEmail", "email@unknown.com");
        if (email != null) {
            // Fetch user details
            User user = dbHelper.getUserByEmail(email);
            if (user != null) {
                // Update UI with user data
                tvNameHeader.setText(user.getDisplayName() != null ? user.getDisplayName() : "Unknown");
                tvLocationHeader.setText("Viet Nam");
                tvName.setText("Họ và tên: " + (user.getDisplayName() != null ? user.getDisplayName() : "Unknown"));
                tvEmail.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "Not set"));
                // Load profile image if available
                if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                    Picasso.get().load(user.getProfileImageUrl()).into(ivProfileImage);
                } else {
                    ivProfileImage.setImageResource(R.drawable.ic_placeholder); // Default image
                }

            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No user email found", Toast.LENGTH_SHORT).show();
        }


        //check sk nút update
        btnUpdate.setOnClickListener(v -> {

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_info, null);
            EditText edtName = dialogView.findViewById(R.id.edtName);
            EditText edtEmail = dialogView.findViewById(R.id.edtEmail);

            edtName.setText(prefs.getString("userFullName", ""));
            edtEmail.setText(prefs.getString("userEmail", ""));

            new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setPositiveButton("Lưu", (dialog, which) -> {
                        String newName = edtName.getText().toString().trim();
                        String newEmail = edtEmail.getText().toString().trim();

                        User user = dbHelper.getUserByEmail(email);
                        Log.d("DEBUG_USER", "User not found for email: " + email);
                        boolean success = userDAO.updateUserBasicInfo(user.getId(), newName, newEmail);
                        if (success) {
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }

                        // Lưu lại vào SharedPreferences
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("userFullName", newName);
                        editor.putString("userEmail", newEmail);
                        editor.apply();

                        TextView tvName = findViewById(R.id.tvName);
                        TextView tvEmail = findViewById(R.id.tvEmail);
                        TextView tvNameHeader = findViewById(R.id.tvNameHeader);

                        tvName.setText("Tên: " + newName);
                        tvEmail.setText("Email: " + newEmail);
                        tvNameHeader.setText(newName);
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });



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