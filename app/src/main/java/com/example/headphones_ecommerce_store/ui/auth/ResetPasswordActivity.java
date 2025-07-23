package com.example.headphones_ecommerce_store.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.example.headphones_ecommerce_store.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputEditText etNewPassword, etConfirmPassword;
    private Button btnSaveNewPassword;
    private TextView tvBackToLogin;
    private DBHelper dbHelper;
    private String email;
    private String resetToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSaveNewPassword = findViewById(R.id.btnSaveNewPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        dbHelper = new DBHelper(this);

        // Get email and token from Intent
        email = getIntent().getStringExtra("email");
        resetToken = getIntent().getStringExtra("reset_token");

        // Save new password button click listener
        btnSaveNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // Validate input
                if (TextUtils.isEmpty(newPassword)) {
                    etNewPassword.setError("New password is required");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    etConfirmPassword.setError("Confirm password is required");
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match");
                    return;
                }
                if (newPassword.length() < 6) {
                    etNewPassword.setError("Password must be at least 6 characters");
                    return;
                }

                // Simulate token validation (in a real app, validate token with server)
                if (resetToken != null) {
                    // Update password in database
                    if (dbHelper.updatePassword(email, newPassword)) {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Invalid reset token", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back to login click listener
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}