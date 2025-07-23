package com.example.headphones_ecommerce_store.repository;

import android.content.Context;
import android.database.Cursor;
import com.example.headphones_ecommerce_store.DAO.ProductDAO;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private ProductDAO productDAO;

    public ProductRepository(Context context) {
        productDAO = new ProductDAO(context);
    }

    public Product getProductById(long id) {
        return productDAO.getProductById(id);
    }
    public List<Product> getAllProducts() {
        Cursor cursor = productDAO.getAllProducts(null);
        return convertCursorToList(cursor);
    }

    public List<Product> searchProducts(String name) {
        Cursor cursor = productDAO.searchProducts(name);
        return convertCursorToList(cursor);
    }

    private List<Product> convertCursorToList(Cursor cursor) {
        List<Product> productList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_DESC)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_PRICE)));
                product.setThumbnailImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL)));
                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return productList;
    }
}