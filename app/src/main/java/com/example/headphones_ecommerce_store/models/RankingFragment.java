package com.example.headphones_ecommerce_store.models;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.HeadphoneAdapter;
import com.example.headphones_ecommerce_store.adapters.RankingProductAdapter;
import com.example.headphones_ecommerce_store.controller.MainHomeActivity;
import com.example.headphones_ecommerce_store.controller.ProductDetailActivity;
import com.example.headphones_ecommerce_store.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankingFragment extends Fragment {
    private static final String ARG_CATEGORY = "category";

    public static RankingFragment newInstance(String category, List<Product> productList) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        args.putSerializable("allProducts", new ArrayList<>(productList));
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

        List<Product> allProducts = (List<Product>) getArguments().getSerializable("allProducts");
        String category = getArguments().getString(ARG_CATEGORY, "");

        List<Product> filteredTop3 = allProducts.stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(category))
                .sorted(Comparator.comparingDouble(Product::getAverageRating).reversed())
                .limit(3)
                .collect(Collectors.toList());

            RankingProductAdapter adapter = new RankingProductAdapter(getContext(), filteredTop3, product -> {

            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            // Chỉ gửi ID của sản phẩm
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());

            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);

        return view;
    }
}

