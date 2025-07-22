package com.example.headphones_ecommerce_store.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.headphones_ecommerce_store.model.Product;
import com.example.headphones_ecommerce_store.models.RankingFragment;

import java.util.List;

public class RankingPagerAdapter extends FragmentStateAdapter {
    private final List<String> categories;
    private final List<Product> allProducts;

    public RankingPagerAdapter(@NonNull FragmentActivity activity, List<String> categories, List<Product> allProducts) {
        super(activity);
        this.categories = categories;
        this.allProducts = allProducts;
    }

    @Override
    public Fragment createFragment(int position) {
        return RankingFragment.newInstance(categories.get(position), allProducts);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}

