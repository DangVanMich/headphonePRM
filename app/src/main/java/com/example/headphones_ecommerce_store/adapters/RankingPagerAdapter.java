package com.example.headphones_ecommerce_store.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.headphones_ecommerce_store.models.RankingFragment;

import java.util.List;

public class RankingPagerAdapter extends FragmentStateAdapter {
    private final List<String> categories;

    public RankingPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> categories) {
        super(fragmentActivity);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return RankingFragment.newInstance(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}

