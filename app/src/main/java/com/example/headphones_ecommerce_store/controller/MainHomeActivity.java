package com.example.headphones_ecommerce_store.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.BannerAdapter;
import com.example.headphones_ecommerce_store.adapters.HeadphoneAdapter;
import com.example.headphones_ecommerce_store.adapters.RankingPagerAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        rvHeadphones = findViewById(R.id.rvHeadphones);
        rvHeadphones.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo dữ liệu mẫu
        headphoneList = new ArrayList<>();
        headphoneList.add(new HeadphoneInfo("Sony WH-1000XM5", "Sony", 399.99, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("Bose QC 45", "Bose", 329.00, "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("AirPods Max", "Apple", 549.00, "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/airpods-max-select-silver-202011?wid=2000&hei=2000"));
        headphoneList.add(new HeadphoneInfo("Momentum 4", "Sennheiser", 379.95, "https://m.media-amazon.com/images/I/81yYo8VHFmL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("Beats Studio Pro", "Beats", 349.95, "https://m.media-amazon.com/images/I/71N1njN3dDL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("Jabra Elite 85h", "Jabra", 199.99, "https://m.media-amazon.com/images/I/81XQy2L7WXL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("Anker Q35", "Anker", 129.99, "https://m.media-amazon.com/images/I/61B+R39V2eL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("Marshall Major IV", "Marshall", 149.99, "https://m.media-amazon.com/images/I/81QQ7Mi99XL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("AKG N700NC M2", "AKG", 299.99, "https://m.media-amazon.com/images/I/81Azc5yCgfL._AC_SL1500_.jpg"));
        headphoneList.add(new HeadphoneInfo("Shure AONIC 50", "Shure", 299.00, "https://m.media-amazon.com/images/I/61lHRD3X1IL._AC_SL1500_.jpg"));

        adapter = new HeadphoneAdapter(this, headphoneList);
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

//            } else if (itemId == R.id.menu_cart) {
//                Intent intent = new Intent(MainHomeActivity.this, CartActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//                return true;
//
//            } else if (itemId == R.id.menu_map) {
//                Intent intent = new Intent(MainHomeActivity.this, MapActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//                return true;
            }

            return false;
        });

    }
}

