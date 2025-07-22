package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.headphones_ecommerce_store.DAO.CartDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.model.Product;
import com.example.headphones_ecommerce_store.repository.ProductRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private int quantity = 1;
    private ImageView ivDetailProductImage, ivMinus, ivPlus, ivAddToCart;
    private TextView tvDetailProductName, tvDetailProductPrice, tvDetailProductDescription, tvQuantity;
    private Button btnBuyNow;
    private Toolbar toolbar;
    private CartDAO cartDAO;
    private BottomNavigationView bottomNav;
    private ProductRepository productRepository;

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        cartDAO = new CartDAO(this);
        productRepository = new ProductRepository(this);

        setupViews();
        setupToolbar();
        loadProductDetails();
        setupClickListeners();
        setupBottomNav();
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
        bottomNav = findViewById(R.id.bottomNavigationView);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết Sản phẩm");
        }
    }

    private void loadProductDetails() {
        long productId = getIntent().getLongExtra(EXTRA_PRODUCT_ID, -1);
        if (productId != -1) {
            this.currentProduct = productRepository.getProductById(productId);

            if (currentProduct != null) {
                displayProductInfo(currentProduct);
            } else {
                Toast.makeText(this, "Không tìm thấy sản phẩm trong DB", Toast.LENGTH_SHORT).show();
                Log.e("ProductDetail", "Product not found in DB for ID: " + productId);
                finish();
            }
        }
    }

    private void displayProductInfo(Product product) {
        tvDetailProductName.setText(product.getName());
        tvDetailProductDescription.setText(product.getDescription());

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvDetailProductPrice.setText(currencyFormatter.format(product.getPrice()));

        Glide.with(this)
                .load(product.getThumbnailImageUrl())
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.mipmap.ic_launcher)
                .fitCenter() // Đảm bảo hiển thị toàn bộ ảnh
                .into(ivDetailProductImage);
    }

    private void setupBottomNav() {
        // Bỏ chọn tất cả các mục để không có mục nào được highlight
        bottomNav.getMenu().setGroupCheckable(0, false, true);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(this, MainHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.menu_cart) {
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(this, ProfileActivity.class); // Giả sử có ProfileActivity
                startActivity(intent);
                return true;
            }
            return false;
        });
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

        //long productId = getIntent().getLongExtra(EXTRA_PRODUCT_ID, -1);
        ivAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                cartDAO.addToCart(currentProduct.getId(), quantity);
                Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                String orderId = String.valueOf(System.currentTimeMillis());
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String totalAmount = currencyFormatter.format(currentProduct.getPrice() * quantity);
                String summary = currentProduct.getName() + "\nSố lượng: " + quantity;

                Intent intent = new Intent(ProductDetailActivity.this, PaymentSuccessActivity.class);
                intent.putExtra("EXTRA_ORDER_ID", orderId);
                intent.putExtra("EXTRA_TOTAL_AMOUNT", totalAmount);
                intent.putExtra("EXTRA_ORDER_SUMMARY", summary);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
