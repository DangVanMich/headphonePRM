package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.headphones_ecommerce_store.DAO.CartDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.models.HeadphoneInfo;
import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_OBJECT = "extra_product_object";
    private HeadphoneInfo currentHeadphone;
    private int quantity = 1;
    private ImageView ivDetailProductImage, ivMinus, ivPlus, ivAddToCart;
    private TextView tvDetailProductName, tvDetailProductPrice, tvDetailProductDescription, tvQuantity;
    private Button btnBuyNow;
    private Toolbar toolbar;
    private CartDAO cartDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        cartDAO = new CartDAO(this);
        setupViews();
        setupToolbar();
        loadProductDetails();
        setupClickListeners();
    }

    private void setupViews() {
        //... code setupViews của bạn ...
        toolbar = findViewById(R.id.toolbar);
        ivDetailProductImage = findViewById(R.id.ivDetailProductImage);
        tvDetailProductName = findViewById(R.id.tvDetailProductName);
        tvDetailProductPrice = findViewById(R.id.tvDetailProductPrice);
        tvDetailProductDescription = findViewById(R.id.tvDetailProductDescription);
        ivMinus = findViewById(R.id.ivMinus);
        tvQuantity = findViewById(R.id.tvQuantity);
        ivPlus = findViewById(R.id.ivPlus);
        ivAddToCart = findViewById(R.id.ivAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    private void setupToolbar() {
        //... code setupToolbar của bạn ...
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(toolbar.getTitle());
        }
    }

    private void loadProductDetails() {
        //... code loadProductDetails của bạn ...
        currentHeadphone = (HeadphoneInfo) getIntent().getSerializableExtra(EXTRA_PRODUCT_OBJECT);
        if (currentHeadphone != null) {
            displayProductInfo(currentHeadphone);
        }
    }

    private void displayProductInfo(HeadphoneInfo headphone) {
        //... code displayProductInfo của bạn ...
        tvDetailProductName.setText(headphone.getName());
        tvDetailProductDescription.setText("Thương hiệu: " + headphone.getBrand());
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvDetailProductPrice.setText(currencyFormatter.format(headphone.getPrice()));
        Glide.with(this).load(headphone.getImageUrl()).into(ivDetailProductImage);
    }

    private void setupClickListeners() {
        //... code của các listener khác ...
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ivPlus.setOnClickListener(v -> { /* ... */ });
        ivMinus.setOnClickListener(v -> { /* ... */ });

        //<====BẮt đầu sửa====>
        ivAddToCart.setOnClickListener(v -> {
            if (currentHeadphone != null) {
                // 1. Thêm sản phẩm vào giỏ hàng
                cartDAO.addToCart(currentHeadphone.getId(), quantity);
                Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();

                // 2. Chuyển đến màn hình giỏ hàng
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
        // <====Kết thúc sửa====>

        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến thanh toán...", Toast.LENGTH_SHORT).show();
        });
    }
}
