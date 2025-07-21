package com.example.headphones_ecommerce_store.controller;

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
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(toolbar.getTitle());
        }
    }

    private void loadProductDetails() {
        currentHeadphone = (HeadphoneInfo) getIntent().getSerializableExtra(EXTRA_PRODUCT_OBJECT);
        if (currentHeadphone != null) {
            displayProductInfo(currentHeadphone);
        }
    }

    private void displayProductInfo(HeadphoneInfo headphone) {
        tvDetailProductName.setText(headphone.getName());
        tvDetailProductDescription.setText("Thương hiệu: " + headphone.getBrand());

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvDetailProductPrice.setText(currencyFormatter.format(headphone.getPrice()));

        //<====BẮt đầu sửa====>
        // Thêm .centerInside() để đảm bảo toàn bộ ảnh được hiển thị
        Glide.with(this)
                .load(headphone.getImageUrl())
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.mipmap.ic_launcher)
                .centerInside() // Thay đổi chế độ hiển thị ảnh
                .into(ivDetailProductImage);
        // <====Kết thúc sửa====>
    }

    private void setupClickListeners() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ivPlus.setOnClickListener(v -> {
            if (quantity < 10) {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        ivMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        ivAddToCart.setOnClickListener(v -> {
            if (currentHeadphone != null) {
                cartDAO.addToCart(currentHeadphone.getId(), quantity);
                Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến thanh toán...", Toast.LENGTH_SHORT).show();
        });
    }
}
