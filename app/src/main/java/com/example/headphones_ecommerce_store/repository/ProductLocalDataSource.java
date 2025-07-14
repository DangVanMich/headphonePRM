package com.example.headphones_ecommerce_store.repository;

import androidx.lifecycle.LiveData;
import java.util.List;


public interface ProductLocalDataSource {
    LiveData<List<com.google.android.gms.analytics.ecommerce.Product>> getAllProductsFlow(); // Room can return LiveData directly
    LiveData<com.google.android.gms.analytics.ecommerce.Product> getProductByIdFlow(String productId); // Room can return LiveData

    com.google.android.gms.analytics.ecommerce.Product getProductByIdSync(String productId); // Synchronous version if needed
    void insertProducts(List<com.google.android.gms.analytics.ecommerce.Product> products);
    // ... methods for reviews, caching logic
}
