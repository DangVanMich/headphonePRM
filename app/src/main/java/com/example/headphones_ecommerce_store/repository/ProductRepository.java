package com.example.headphones_ecommerce_store.repository;

import androidx.lifecycle.LiveData;
import java.util.List;

public interface ProductRepository {
    LiveData<Result<List<com.google.android.gms.analytics.ecommerce.Product>>> getAllProducts();
    LiveData<Result<com.google.android.gms.analytics.ecommerce.Product>> getProductById(String productId);
    LiveData<Result<List<com.google.android.gms.analytics.ecommerce.Product>>> getProductsByCategory(String category);
    LiveData<Result<List<com.google.android.libraries.places.api.model.Review>>> getReviewsForProduct(String productId);

    // For operations that don't return ongoing LiveData, you might use callbacks
    // or a different return type if you're not using LiveData for one-shot results.
    // For simplicity, we'll try to stick to LiveData where it makes sense.
    // If an operation is a one-time action (e.g., a POST request with no data to observe back),
    // you might have a method like:
    // void searchProducts(String query, DataCallback<List<Product>> callback);
}

// Example Callback (if not using LiveData for everything)
// interface DataCallback<T> {
//    void onSuccess(T data);
//    void onError(Exception e);
// }
