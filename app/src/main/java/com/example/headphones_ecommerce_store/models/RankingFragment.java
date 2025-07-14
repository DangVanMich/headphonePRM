package com.example.headphones_ecommerce_store.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.RankingItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {
    private static final String ARG_CATEGORY = "category";

    public static RankingFragment newInstance(String category) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvRanking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<RankingItem> items = new ArrayList<>();
        String category = getArguments().getString(ARG_CATEGORY, "");

        // Demo cứng theo category
        if (category.equals("Sony")) {
            items.add(new RankingItem("WH-1000XM5", "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg", 399.99));
            items.add(new RankingItem("WH-CH720N", "https://m.media-amazon.com/images/I/71YzjlYCHbL._AC_SL1500_.jpg", 129.99));
            items.add(new RankingItem("WH-XB910N", "https://m.media-amazon.com/images/I/71XCLbF22vL._AC_SL1500_.jpg", 199.00));
        }
        if (category.equals("Apple")) {
            items.add(new RankingItem("WH-1000XM5", "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg", 399.99));
            items.add(new RankingItem("WH-CH720N", "https://m.media-amazon.com/images/I/71YzjlYCHbL._AC_SL1500_.jpg", 129.99));
            items.add(new RankingItem("WH-XB910N", "https://m.media-amazon.com/images/I/71XCLbF22vL._AC_SL1500_.jpg", 199.00));
        }
        if (category.equals("Jabra")) {
            items.add(new RankingItem("WH-1000XM5", "https://m.media-amazon.com/images/I/61bK6PMOC3L._AC_SL1500_.jpg", 399.99));
            items.add(new RankingItem("WH-CH720N", "https://m.media-amazon.com/images/I/71YzjlYCHbL._AC_SL1500_.jpg", 129.99));
            items.add(new RankingItem("WH-XB910N", "https://m.media-amazon.com/images/I/71XCLbF22vL._AC_SL1500_.jpg", 199.00));
        }
        // ... Apple, Jabra, etc.

        RankingItemAdapter adapter = new RankingItemAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

