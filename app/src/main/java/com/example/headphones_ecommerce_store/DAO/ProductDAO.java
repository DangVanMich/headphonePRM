package com.example.headphones_ecommerce_store.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.model.Product;


public class ProductDAO extends DBHelper {

    public ProductDAO(Context context) {
        super(context);
    }
    public boolean addProduct(String name, String description, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_DESC, description);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL, imageUrl);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }


    public Cursor getAllProducts(String sortBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        // sortBy can be "price_asc", "price_desc", or null for default
        String orderByClause = null;
        if (sortBy != null) {
            if (sortBy.equals("price_asc")) {
                orderByClause = COLUMN_PRODUCT_PRICE + " ASC";
            } else if (sortBy.equals("price_desc")) {
                orderByClause = COLUMN_PRODUCT_PRICE + " DESC";
            }
        }
        return db.query(TABLE_PRODUCTS, null, null, null, null, null, orderByClause);
    }


    public Cursor searchProducts(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_PRODUCT_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + name + "%"};
        return db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);
    }


    public boolean updateProduct(long id, String name, String description, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_DESC, description);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL, imageUrl);

        int result = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    public boolean deleteProduct(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Product getProductById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        Product product = null;

        if(cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
            product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
            product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
            product.setThumbnailImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL)));
        }
        return product;
    }

}
