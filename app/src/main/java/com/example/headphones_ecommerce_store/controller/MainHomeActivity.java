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

public class MainHomeActivity extends AppCompatActivity {

    private RecyclerView rvHeadphones;
    private HeadphoneAdapter adapter;
    private List<Product> productList;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();
    EditText searchEditText;
    //Button btnSony, btnApple, btnJabra, btnAnker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        rvHeadphones = findViewById(R.id.rvHeadphones);
        rvHeadphones.setLayoutManager(new LinearLayoutManager(this));
        searchEditText = findViewById(R.id.searchEditText);
        findViewById(R.id.btnSony).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Sony"));
        findViewById(R.id.btnApple).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Apple"));
        findViewById(R.id.btnJabra).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Jabra"));
        findViewById(R.id.btnAnker).setOnClickListener(v -> applyFilter(searchEditText.getText().toString(), "Anker"));
        findViewById(R.id.btnAll).setOnClickListener(v ->  applyFilter(searchEditText.getText().toString(), null));

        ProductDAO productDAO = new ProductDAO(this);
        Cursor cursor = productDAO.getAllProducts(null);
        productList = new ArrayList<>();

        //test list khi add product ko đủ dữ liệu
        productList.add(new Product(
                1, "Sony WH-1000XM5", "Sony", "Tai nghe chống ồn đỉnh cao, pin 30 giờ", 399.99, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg", 4.5f
        ));

        productList.add(new Product(
                2, "AirPods Max", "Apple", "Âm thanh không gian, thiết kế cao cấp", 549.00, "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/airpods-max-select-silver-202011?wid=2000&hei=2000", 4.2f
        ));

        productList.add(new Product(
                3, "Jabra Elite 85h", "Jabra", "Chống ồn chủ động, kết nối đa thiết bị", 199.99, "https://m.media-amazon.com/images/I/81XQy2L7WXL._AC_SL1500_.jpg", 2.2f
        ));
        productList.add(new Product(
                4, "Jabra Elite 85h", "Jabra", "Chống ồn chủ động, kết nối đa thiết bị", 199.99, "https://m.media-amazon.com/images/I/81XQy2L7WXL._AC_SL1500_.jpg", 3.5f
        ));
        // if chạy đúng nhưng 1 hàng bị null là lỗi
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                Product product = new Product();
//                product.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
//                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
//                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BRAND)));
//                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC)));
//                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
//                product.setThumbnailImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL)));
//                product.setAverageRating(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_AVERAGE_RATING)));
//                productList.add(product);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }

        allProducts = productList;
        filteredProducts = new ArrayList<>(allProducts);

        // bấm vào sản phẩm (đổi model r)
        adapter = new HeadphoneAdapter(this, filteredProducts, product -> {
            Log.d("MainHomeClick", "Clicked on: " + product.getName());
            Intent intent = new Intent(MainHomeActivity.this, ProductDetailActivity.class);
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

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.menu_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(MainHomeActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }else if (itemId == R.id.menu_map) {
                Intent intent = new Intent(MainHomeActivity.this, AddEditProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
            return false;
        });
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

}
