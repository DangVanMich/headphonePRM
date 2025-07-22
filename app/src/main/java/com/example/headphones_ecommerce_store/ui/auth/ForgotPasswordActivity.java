package com.example.headphones_ecommerce_store.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.example.headphones_ecommerce_store.R;

import java.util.UUID;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputEditText etEmail;
    private Button btnResetLink;
    private TextView tvBackToLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        btnResetLink = findViewById(R.id.btnResetLink);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        dbHelper = new DBHelper(this);

        // Reset link button click listener
        btnResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                // Validate input
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email is required");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Invalid email format");
                    return;
                }

                // Check if email exists
                boolean emailExists = dbHelper.isEmailExists(email);
                Toast.makeText(ForgotPasswordActivity.this, "Email check result: " + (emailExists ? "Found" : "Not Found"), Toast.LENGTH_SHORT).show();
                if (emailExists) {
                    // Generate a mock reset token
                    String resetToken = UUID.randomUUID().toString();
                    Toast.makeText(ForgotPasswordActivity.this, "Reset link generated for " + email, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("reset_token", resetToken);
                    startActivity(intent);
                    // Temporarily comment out finish() to diagnose
                    // finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back to login click listener
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class); // Corrected to LoginActivity
                startActivity(intent);
                finish();
            }
        });
    }
}