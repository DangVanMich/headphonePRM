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
import com.example.headphones_ecommerce_store.model.Product;
import com.example.headphones_ecommerce_store.repository.ProductRepository;
import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private int quantity = 1;

    // Các thành phần UI
    private ImageView ivDetailProductImage, ivMinus, ivPlus;
    private TextView tvDetailProductName, tvDetailProductPrice, tvDetailProductDescription, tvDetailProductCategory, tvQuantity, tvAddToCart;
    private Button btnBuyNow;
    private Toolbar toolbar;

    // Các đối tượng xử lý dữ liệu
    private ProductRepository productRepository;
    private CartDAO cartDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo các đối tượng Repository và DAO
        productRepository = new ProductRepository(this);
        cartDAO = new CartDAO(this);

        // Ánh xạ View và thiết lập
        setupViews();
        setupToolbar();

        // Lấy dữ liệu sản phẩm và hiển thị
        loadProductDetails();

        // Thiết lập sự kiện click
        setupClickListeners();
    }

    /**
     * Ánh xạ tất cả các view từ file layout XML.
     */
    private void setupViews() {
        toolbar = findViewById(R.id.toolbar);
        ivDetailProductImage = findViewById(R.id.ivDetailProductImage);
        tvDetailProductName = findViewById(R.id.tvDetailProductName);
        tvDetailProductCategory = findViewById(R.id.tvDetailProductCategory);
        tvDetailProductPrice = findViewById(R.id.tvDetailProductPrice);
        tvDetailProductDescription = findViewById(R.id.tvDetailProductDescription);
        ivMinus = findViewById(R.id.ivMinus);
        tvQuantity = findViewById(R.id.tvQuantity);
        ivPlus = findViewById(R.id.ivPlus);
        tvAddToCart = findViewById(R.id.tvAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    /**
     * Cấu hình Toolbar với nút quay lại.
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Lấy tiêu đề từ layout thay vì code cứng
            getSupportActionBar().setTitle(toolbar.getTitle());
        }
    }

    /**
     * Lấy ID sản phẩm từ Intent và hiển thị thông tin lên UI.
     */
    private void loadProductDetails() {
        long productId = getIntent().getLongExtra(EXTRA_PRODUCT_ID, -1);
        if (productId != -1) {
            Product product = productRepository.getProductById(productId);
            if (product != null) {
                displayProductInfo(product);
            }
        }
    }

    /**
     * Điền thông tin của sản phẩm vào các view tương ứng.
     * @param product Đối tượng Product chứa thông tin cần hiển thị.
     */
    private void displayProductInfo(Product product) {
        // Điền thông tin cơ bản
        tvDetailProductName.setText(product.getName());
        tvDetailProductDescription.setText(product.getDescription());

        // Cần lấy category từ model Product nếu có
        // tvDetailProductCategory.setText(product.getCategory());

        // Format giá tiền sang dạng tiền tệ Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvDetailProductPrice.setText(currencyFormatter.format(product.getPrice()));

        // Dùng Glide để tải ảnh
        Glide.with(this)
                .load(product.getThumbnailImageUrl())
                .placeholder(R.drawable.ic_menu_gallery) // Ảnh hiển thị trong lúc chờ tải
                .error(R.mipmap.ic_launcher) // Ảnh hiển thị khi lỗi
                .into(ivDetailProductImage);
    }

    /**
     * Gán sự kiện click cho tất cả các thành phần tương tác.
     */
    private void setupClickListeners() {
        // Nút quay lại trên Toolbar
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Tăng số lượng
        ivPlus.setOnClickListener(v -> {
            if (quantity < 10) { // Giới hạn số lượng tối đa là 10
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Giảm số lượng
        ivMinus.setOnClickListener(v -> {
            if (quantity > 1) { // Số lượng tối thiểu là 1
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Lấy ID sản phẩm
        long productId = getIntent().getLongExtra(EXTRA_PRODUCT_ID, -1);

        // Sự kiện click cho "Thêm vào giỏ hàng"
        tvAddToCart.setOnClickListener(v -> {
            if (productId != -1) {
                cartDAO.addToCart(productId, quantity);
                Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện click cho "Mua ngay"
        btnBuyNow.setOnClickListener(v -> {
            // Logic mua ngay sẽ được phát triển ở các bước sau
            Toast.makeText(this, "Chuyển đến thanh toán...", Toast.LENGTH_SHORT).show();
        });
    }
}