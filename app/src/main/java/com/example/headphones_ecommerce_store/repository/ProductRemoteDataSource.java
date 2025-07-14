package com.example.headphones_ecommerce_store.repository;

import java.util.List;

import java.util.List;

public interface ProductRemoteDataSource {
    // These would typically be blocking calls if not using a library like Retrofit with Call<T>
    // Or they would take callbacks. For this example, let's assume they might be blocking
    // and the Repository implementation will handle threading.
    List<com.google.android.gms.analytics.ecommerce.Product> fetchAllProducts() throws Exception; // Throws Exception for error handling

    com.google.android.gms.analytics.ecommerce.Product fetchProductById(String productId) throws Exception;
    List<com.google.android.gms.analytics.ecommerce.Product> fetchProductsByCategory(String category) throws Exception;
    List<com.google.android.libraries.places.api.model.Review> fetchReviewsForProduct(String productId) throws Exception;
}
