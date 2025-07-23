package com.example.headphones_ecommerce_store.controller;

import static com.example.headphones_ecommerce_store.database.DBHelper.COLUMN_PRODUCT_AVERAGE_RATING;
import static com.example.headphones_ecommerce_store.database.DBHelper.COLUMN_PRODUCT_BRAND;
import static com.example.headphones_ecommerce_store.database.DBHelper.COLUMN_PRODUCT_DESC;
import static com.example.headphones_ecommerce_store.database.DBHelper.COLUMN_PRODUCT_ID;
import static com.example.headphones_ecommerce_store.database.DBHelper.COLUMN_PRODUCT_NAME;
import static com.example.headphones_ecommerce_store.database.DBHelper.COLUMN_PRODUCT_PRICE;
import static com.example.headphones_ecommerce_store.database.DBHelper.COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.headphones_ecommerce_store.DAO.ProductDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.BannerAdapter;
import com.example.headphones_ecommerce_store.adapters.HeadphoneAdapter;
import com.example.headphones_ecommerce_store.adapters.RankingPagerAdapter;
import com.example.headphones_ecommerce_store.model.Product;
import com.example.headphones_ecommerce_store.ui.product.AddEditProductActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainHomeActivity extends AppCompatActivity {

    private RecyclerView rvHeadphones;
    private HeadphoneAdapter adapter;
    private List<Product> productList;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();
    private ProductDAO productDAO;
    EditText searchEditText;
    //Button btnSony, btnApple, btnJabra, btnAnker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        BottomNavHelper.setupBottomNav(this, R.id.menu_home);
        rvHeadphones = findViewById(R.id.rvHeadphones);
        rvHeadphones.setLayoutManager(new LinearLayoutManager(this));
        searchEditText = findViewById(R.id.searchEditText);
        findViewById(R.id.btnSony).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Sony"));
        findViewById(R.id.btnApple).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Apple"));
        findViewById(R.id.btnJabra).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Jabra"));
        findViewById(R.id.btnAnker).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Anker"));
        findViewById(R.id.btnAll).setOnClickListener(v ->  applyFilter(searchEditText.getText().toString(), null));

        productDAO = new ProductDAO(this);

        this.seedDatabaseIfEmpty();

        Cursor cursor = productDAO.getAllProducts(null);
        productList = new ArrayList<>();

//        test list khi add product ko đủ dữ liệu
//        productList.add(new Product(2, "AirPods Max", "Apple", "Âm thanh không gian, thiết kế cao cấp", 549.00, "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/airpods-max-select-silver-202011?wid=2000&hei=2000", 4.2f));
//        productList.add(new Product(3, "Jabra Elite 85h", "Jabra", "Chống ồn chủ động, kết nối đa thiết bị", 199.99, "https://m.media-amazon.com/images/I/81XQy2L7WXL._AC_SL1500_.jpg", 2.2f));
//        productList.add(new Product(4, "Jabra Elite 85h", "Jabra", "Chống ồn chủ động, kết nối đa thiết bị", 199.99, "https://m.media-amazon.com/images/I/81XQy2L7WXL._AC_SL1500_.jpg", 3.5f));


        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));

                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BRAND)));
//                product.setBrand(Objects.requireNonNullElse(
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BRAND)), "All"));

                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
                product.setThumbnailImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL)));
                Float raking = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_AVERAGE_RATING));
                product.setAverageRating(raking == null ? 1 : raking);

                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }

        allProducts = productList;
        filteredProducts = new ArrayList<>(allProducts);

        // bấm vào sản phẩm (đổi model r)
        adapter = new HeadphoneAdapter(this, filteredProducts, product -> {
            Log.d("MainHomeClick", "Clicked on: " + product.getName() + " with ID: " + product.getId());

            Intent intent = new Intent(MainHomeActivity.this, ProductDetailActivity.class);

            // Chỉ gửi ID của sản phẩm
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
            //intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_OBJECT, product);
            startActivity(intent);
        });
        rvHeadphones.setAdapter(adapter);
        //Check event search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter(s.toString(), null);
            }
        });

        // Ranking
        List<String> rankingCategories = Arrays.asList("Sony", "Apple", "Jabra", "Anker");

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPagerRanking);
        RankingPagerAdapter pagerAdapter = new RankingPagerAdapter(this, rankingCategories, allProducts);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(rankingCategories.get(position));
        }).attach();


        // Banner Slider
        ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);
        List<Integer> bannerList = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        );
        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        new Handler().postDelayed(new Runnable() {
            int index = 0;

            @Override
            public void run() {
                if (bannerViewPager.getAdapter() != null) {
                    index = (bannerViewPager.getCurrentItem() + 1) % bannerList.size();
                    bannerViewPager.setCurrentItem(index, true);
                }
                bannerViewPager.postDelayed(this, 5000);
            }
        }, 5000);
    }
    private void applyFilter(String keyword, String brand) {
        filteredProducts.clear();

        for (Product p : allProducts) {
            boolean matchName = p.getName().toLowerCase().contains(keyword.toLowerCase());
            boolean matchBrand = (brand == null || p.getBrand().equalsIgnoreCase(brand));

            if (matchName && matchBrand) {
                filteredProducts.add(p);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void seedDatabaseIfEmpty() {
        if (productDAO.getProductsCount() == 0) {
            Log.d("DatabaseSeed", "Database is empty. Seeding data...");

            List<Product> productsToSeed = new ArrayList<>();

            // Thêm dữ liệu từ headphoneList
            productsToSeed.add(new Product(1, "Sony WH-1000XM5", "Sony", "Tai nghe chống ồn chủ động hàng đầu.", 399.99, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(2, "Bose QC 45", "Bose", "Thoải mái và yên tĩnh tuyệt đối.", 329.00, "https://m.media-amazon.com/images/I/51Jbs28e2AL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(3, "AirPods Max", "Apple", "Âm thanh Hi-Fi, thiết kế sang trọng.", 549.00, "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/airpods-max-select-silver-202011?wid=2000&hei=2000"));
            productsToSeed.add(new Product(4, "Momentum 4", "Sennheiser", "Chất âm đặc trưng của Sennheiser.", 379.95, "https://m.media-amazon.com/images/I/81yYo8VHFmL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(5, "Beats Studio Pro", "Beats", "Âm bass mạnh mẽ, phong cách.", 349.95, "https://m.media-amazon.com/images/I/71N1njN3dDL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(6, "Jabra Elite 85h", "Jabra", "Chống ồn thông minh, pin bền.", 199.99, "https://m.media-amazon.com/images/I/81XQy2L7WXL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(7, "Anker Q35", "Anker", "Lựa chọn tuyệt vời trong tầm giá.", 129.99, "https://m.media-amazon.com/images/I/61B+R39V2eL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(8, "Marshall Major IV", "Marshall", "Thiết kế cổ điển, pin siêu trâu.", 149.99, "https://m.media-amazon.com/images/I/81QQ7Mi99XL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(9, "AKG N700NC M2", "AKG", "Âm thanh cân bằng, chi tiết.", 299.99, "https://m.media-amazon.com/images/I/81Azc5yCgfL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(10, "Shure AONIC 50", "Shure", "Chất lượng âm thanh phòng thu.", 299.00, "https://m.media-amazon.com/images/I/61lHRD3X1IL._AC_SL1500_.jpg"));

            // Thêm dữ liệu từ rankingList
            productsToSeed.add(new Product(11, "WH-CH720N", "Sony", "Tai nghe không dây chống ồn nhẹ nhất của Sony.", 129.99, "https://m.media-amazon.com/images/I/71YzjlYCHbL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(12, "WH-XB910N", "Sony", "EXTRA BASS cho âm trầm sâu, mạnh mẽ.", 199.00, "https://m.media-amazon.com/images/I/71XCLbF22vL._AC_SL1500_.jpg"));
            // (Các sản phẩm trùng tên nhưng khác hãng/ID sẽ được thêm vào như các sản phẩm riêng biệt)
            productsToSeed.add(new Product(13, "WH-1000XM5 (Apple)", "Apple", "Phiên bản đặc biệt.", 399.99, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(14, "WH-CH720N (Apple)", "Apple", "Phiên bản đặc biệt.", 129.99, "https://m.media-amazon.com/images/I/71YzjlYCHbL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(18, "WH-XB910N (Apple)", "Apple", "Phiên bản đặc biệt.", 199.00, "https://m.media-amazon.com/images/I/71XCLbF22vL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(15, "WH-1000XM5 (Jabra)", "Jabra", "Phiên bản đặc biệt.", 399.99, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(16, "WH-CH720N (Jabra)", "Jabra", "Phiên bản đặc biệt.", 129.99, "https://m.media-amazon.com/images/I/71YzjlYCHbL._AC_SL1500_.jpg"));
            productsToSeed.add(new Product(17, "WH-XB910N (Jabra)", "Jabra", "Phiên bản đặc biệt.", 199.00, "https://m.media-amazon.com/images/I/71XCLbF22vL._AC_SL1500_.jpg"));

            for (Product product : productsToSeed) {
                productDAO.addProduct(product);
            }
            Log.d("DatabaseSeed", "Seeding complete. " + productsToSeed.size() + " products added.");
        } else {
            Log.d("DatabaseSeed", "Database already contains data. No seeding needed.");
        }
    }

}

