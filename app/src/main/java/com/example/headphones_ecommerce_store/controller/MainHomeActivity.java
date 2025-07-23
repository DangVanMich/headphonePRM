package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.headphones_ecommerce_store.models.HeadphoneInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainHomeActivity extends AppCompatActivity {
    private RecyclerView rvHeadphones;
    private HeadphoneAdapter adapter;
    private List<HeadphoneInfo> headphoneList;
    private TextView tvGreeting;

    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        productDAO = new ProductDAO(this);
        seedDatabaseIfEmpty(); // Bây giờ hàm này sẽ chạy đúng

        // Initialize greeting TextView
        tvGreeting = findViewById(R.id.tvGreeting); // Ensure this ID matches the XML
        String userFullName = getIntent().getStringExtra("user_full_name");
        if (userFullName != null && !userFullName.isEmpty()) {
            tvGreeting.setText("Chào, " + userFullName);
        } else {
            tvGreeting.setText("Chào, Khách");
        }

        rvHeadphones = findViewById(R.id.rvHeadphones);
        rvHeadphones.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo dữ liệu mẫu
        headphoneList = new ArrayList<>();
        headphoneList.add(new HeadphoneInfo(1,"Sony WH-1000XM5", "Sony", 399.99, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(2,"Bose QC 45", "Bose", 329.00, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(3,"AirPods Max", "Apple", 549.00, "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/airpods-max-select-silver-202011?wid=2000&hei=2000"));
        headphoneList.add(new HeadphoneInfo(4,"Momentum 4", "Sennheiser", 379.95, "https://m.media-amazon.com/images/I/81yYo8VHFmL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(5,"Beats Studio Pro", "Beats", 349.95, "https://m.media-amazon.com/images/I/71N1njN3dDL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(6,"Jabra Elite 85h", "Jabra", 199.99, "https://m.media-amazon.com/images/I/81XQy2L7WXL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(7,"Anker Q35", "Anker", 129.99, "https://m.media-amazon.com/images/I/61B+R39V2eL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(8,"Marshall Major IV", "Marshall", 149.99, "https://m.media-amazon.com/images/I/81QQ7Mi99XL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(9,"AKG N700NC M2", "AKG", 299.99, "https://m.media-amazon.com/images/I/81Azc5yCgfL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo(10,"Shure AONIC 50", "Shure", 299.00, "https://m.media-amazon.com/images/I/61lHRD3X1IL._AC_SL1500_.jpg"));

        adapter = new HeadphoneAdapter(this, headphoneList, headphone -> {
            Log.d("MainHomeClick", "Clicked on: " + headphone.getName() + " with ID: " + headphone.getId());

            Intent intent = new Intent(MainHomeActivity.this, ProductDetailActivity.class);

            // Chỉ gửi ID của sản phẩm
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, headphone.getId());

            startActivity(intent);
        });
        rvHeadphones.setAdapter(adapter);

        List<String> rankingCategories = Arrays.asList("Sony", "Apple", "Jabra");

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPagerRanking);
        RankingPagerAdapter pagerAdapter = new RankingPagerAdapter(this, rankingCategories);
        viewPager.setAdapter(pagerAdapter);

        // Đồng bộ TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(rankingCategories.get(position));
        }).attach();

        ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);
        List<Integer> bannerList = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        );
        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        // Tự động lướt banner mỗi 3s
        new Handler().postDelayed(new Runnable() {
            int index = 0;
            @Override
            public void run() {
                if (bannerViewPager.getAdapter() != null) {
                    index = (bannerViewPager.getCurrentItem() + 1) % bannerList.size();
                    bannerViewPager.setCurrentItem(index, true);
                }
                bannerViewPager.postDelayed(this, 3000);
            }
        }, 3000);

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.menu_home); // Đánh dấu đang ở tab Home

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                // Đã ở Home, không làm gì
                return true;

            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(MainHomeActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }

            return false;
        });
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