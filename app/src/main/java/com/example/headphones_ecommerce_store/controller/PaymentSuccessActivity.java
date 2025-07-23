package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.headphones_ecommerce_store.R;

public class PaymentSuccessActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // --- Cài đặt Toolbar ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thanh toán Thành công");
        }
        // Khi nhấn nút back trên toolbar, quay về trang chủ
        toolbar.setNavigationOnClickListener(v -> navigateToHome());

        // --- Ánh xạ các View ---
        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
        TextView tvOrderSummary = findViewById(R.id.tvOrderSummary);
        Button btnViewOrder = findViewById(R.id.btnViewOrder);
        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);

        // --- Lấy dữ liệu từ Intent ---
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("EXTRA_ORDER_ID");
        String totalAmount = intent.getStringExtra("EXTRA_TOTAL_AMOUNT");
        String orderSummary = intent.getStringExtra("EXTRA_ORDER_SUMMARY");

        // --- Hiển thị dữ liệu lên giao diện ---
        tvOrderId.setText("Mã đơn hàng: " + orderId);
        tvTotalAmount.setText("Tổng tiền: " + totalAmount);
        tvOrderSummary.setText(orderSummary);

        // --- Cài đặt sự kiện cho các nút ---
        btnViewOrder.setOnClickListener(v -> {
            // Chuyển đến màn hình lịch sử đơn hàng (chức năng này sẽ được phát triển sau)
            Intent orderHistoryIntent = new Intent(this, OrderHistoryActivity.class);
            startActivity(orderHistoryIntent);
        });

        btnContinueShopping.setOnClickListener(v -> navigateToHome());
    }

    /**
     * Điều hướng người dùng về màn hình chính và xóa các màn hình trung gian.
     */
    private void navigateToHome() {
        Intent intent = new Intent(this, MainHomeActivity.class);
        // Xóa tất cả các activity phía trên MainHomeActivity trong stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Đóng màn hình hiện tại
    }

    /**
     * Ghi đè nút back của hệ thống để đảm bảo người dùng luôn quay về trang chủ.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Thêm dòng này để tuân thủ cảnh báo của Android Studio
        navigateToHome();
    }
}
