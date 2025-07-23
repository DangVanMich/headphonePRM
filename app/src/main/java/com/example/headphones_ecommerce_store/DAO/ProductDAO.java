package com.example.headphones_ecommerce_store.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ProductDAO extends DBHelper {

    public ProductDAO(Context context) {
        super(context);
    }
    public boolean addProduct(Product product) {


        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {


            ContentValues productValues = new ContentValues();
            productValues.put(COLUMN_PRODUCT_NAME, product.getName());
            productValues.put(COLUMN_PRODUCT_DESC, product.getDescription());
            productValues.put(COLUMN_PRODUCT_PRICE, product.getPrice());
            productValues.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
            productValues.put(COLUMN_PRODUCT_STOCK_QUANTITY, product.getStockQuantity());
            productValues.put(COLUMN_PRODUCT_BRAND, product.getBrand());
            productValues.put(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL, product.getThumbnailImageUrl());

            long productId = db.insertOrThrow(TABLE_PRODUCTS, null, productValues);


            List<String> features = product.getFeatures();
            if(features != null && !features.isEmpty()) {

                for(String feature : features) {
                    ContentValues featureValues = new ContentValues();
                    featureValues.put(COLUMN_FEATURE_TEXT, feature);
                    featureValues.put(COLUMN_FEATURE_PRODUCT_ID, productId);
                    if (db.insert(TABLE_PRODUCT_FEATURES, null, featureValues) == -1) {
                        return false;
                    }
                }
            }

            List<String> colorOptions = product.getColorOptions();
            if (colorOptions != null && !colorOptions.isEmpty()) {
                for (String colorOption : colorOptions) {
                    ContentValues colorValues = new ContentValues();
                    colorValues.put(COLUMN_COLOR_OPTION_PRODUCT_ID, productId);
                    colorValues.put(COLUMN_COLOR_OPTION_NAME, colorOption);
                    if (db.insert(TABLE_PRODUCT_COLOR_OPTIONS, null, colorValues) == -1) {
                        return false;
                    }
                }
            }

            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }


    public Cursor getAllProducts(String sortBy) {
        SQLiteDatabase db = this.getReadableDatabase();
//        // sortBy can be "price_asc", "price_desc", or null for default
//        String orderByClause = null;
//        if (sortBy != null) {
//            if (sortBy.equals("price_asc")) {
//                orderByClause = COLUMN_PRODUCT_PRICE + " ASC";
//            } else if (sortBy.equals("price_desc")) {
//                orderByClause = COLUMN_PRODUCT_PRICE + " DESC";
//            }
//        }
        return db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
    }


    public Cursor searchProducts(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_PRODUCT_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + name + "%"};
        return db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);
    }


    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.beginTransaction();

        try {
            long productId = product.getId();


            ContentValues productValues = new ContentValues();
            productValues.put(COLUMN_PRODUCT_NAME, product.getName());
            productValues.put(COLUMN_PRODUCT_BRAND, product.getBrand());
            productValues.put(COLUMN_PRODUCT_DESC, product.getDescription());
            productValues.put(COLUMN_PRODUCT_PRICE, product.getPrice());
            productValues.put(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL, product.getThumbnailImageUrl());
            productValues.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
            productValues.put(COLUMN_PRODUCT_STOCK_QUANTITY, product.getStockQuantity());


            int rowsAffected = db.update(TABLE_PRODUCTS, productValues, COLUMN_PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(productId)});

            if (rowsAffected == 0) {
                return false;
            }


            db.delete(TABLE_PRODUCT_FEATURES, COLUMN_FEATURE_PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(productId)});

            List<String> features = product.getFeatures();
            if (features != null && !features.isEmpty()) {
                for (String feature : features) {
                    ContentValues featureValues = new ContentValues();
                    featureValues.put(COLUMN_FEATURE_PRODUCT_ID, productId);
                    featureValues.put(COLUMN_FEATURE_TEXT, feature);
                    db.insertOrThrow(TABLE_PRODUCT_FEATURES, null, featureValues);
                }
            }


            db.delete(TABLE_PRODUCT_COLOR_OPTIONS, COLUMN_COLOR_OPTION_PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(productId)});

            List<String> colorOptions = product.getColorOptions();
            if (colorOptions != null && !colorOptions.isEmpty()) {
                for (String colorOption : colorOptions) {
                    ContentValues colorValues = new ContentValues();
                    colorValues.put(COLUMN_COLOR_OPTION_PRODUCT_ID, productId);
                    colorValues.put(COLUMN_COLOR_OPTION_NAME, colorOption);
                    db.insertOrThrow(TABLE_PRODUCT_COLOR_OPTIONS, null, colorValues);
                }
            }


            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {

            return false;

        } finally {

            db.endTransaction();
        }
    }


    public boolean deleteProduct(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public List<String> getProductFeatures(long productId) {
        List<String> features = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_PRODUCT_FEATURES,
                    new String[]{COLUMN_FEATURE_TEXT},
                    COLUMN_FEATURE_PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(productId)},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                int featureTextIndex = cursor.getColumnIndex(COLUMN_FEATURE_TEXT);

                do {
                    features.add(cursor.getString(featureTextIndex));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return features;
    }
    public List<String> getProductColorOptions(long productId) {
        List<String> colorOptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_PRODUCT_COLOR_OPTIONS,
                    new String[]{COLUMN_COLOR_OPTION_NAME},
                    COLUMN_COLOR_OPTION_PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(productId)},
                    null, null, null
            );


            if (cursor.moveToFirst()) {
                int colorNameIndex = cursor.getColumnIndex(COLUMN_COLOR_OPTION_NAME);
                do {
                    colorOptions.add(cursor.getString(colorNameIndex));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
           return Collections.emptyList();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return colorOptions;
    }

    public Product getProductById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        Product product = null;

        if(cursor.moveToFirst()) {
            product = new Product();

            long productId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
            product.setId(productId);
            product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
            product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
            product.setThumbnailImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL)));
            product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY)));
            product.setStockQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_STOCK_QUANTITY)));
            product.setAverageRating(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_AVERAGE_RATING)));
            product.setReviewCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_REVIEW_COUNT)));
            product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BRAND)));

            List<String> features = this.getProductFeatures(productId);

            if(!features.isEmpty()) {
                product.setFeatures(features);
            }
            List<String> colorOptions = this.getProductColorOptions(productId);

            product.setColorOptions(colorOptions);


        }
        return product;
    }

    public int getProductsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
