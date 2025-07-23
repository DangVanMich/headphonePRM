package com.example.headphones_ecommerce_store.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.headphones_ecommerce_store.database.DBHelper;

public class CartDAO extends DBHelper {

    private static final String TAG = "CartDAO";

    public CartDAO(Context context) {
        super(context);
    }
    public void addToCart(long productId, int quantity, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(TAG, "Attempting to add to cart. ProductID: " + productId + ", Quantity: " + quantity + ", UserID: " + userId);

        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_PRODUCT_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId), String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            Log.d(TAG, "Product already in user's cart. Updating quantity.");

            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
            int newQuantity = currentQuantity + quantity;

            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QUANTITY, newQuantity);
            int rowsAffected = db.update(TABLE_CART, values, COLUMN_CART_PRODUCT_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?", new String[]{String.valueOf(productId), String.valueOf(userId)});

            Log.d(TAG, "Update result: " + rowsAffected + " rows affected.");

        } else {
            Log.d(TAG, "Product not in user's cart. Inserting new item.");

            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_PRODUCT_ID, productId);
            values.put(COLUMN_CART_QUANTITY, quantity);
            values.put(COLUMN_CART_USER_ID, userId); // Thêm userId vào
            long newRowId = db.insert(TABLE_CART, null, values);

            Log.d(TAG, "Insert result: New row ID is " + newRowId);
        }
        cursor.close();
        db.close();
    }

    public Cursor getAllCartItems(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                "p." + COLUMN_PRODUCT_ID + ", " +
                "p." + COLUMN_PRODUCT_NAME + ", " +
                "p." + COLUMN_PRODUCT_PRICE + ", " +
                "p." + COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL + ", " +
                "c." + COLUMN_CART_QUANTITY + " " +
                "FROM " + TABLE_PRODUCTS + " p INNER JOIN " + TABLE_CART + " c " +
                "ON p." + COLUMN_PRODUCT_ID + " = c." + COLUMN_CART_PRODUCT_ID + " " +
                "WHERE c." + COLUMN_CART_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public boolean updateQuantity(long productId, int quantity, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, quantity);
        int result = db.update(TABLE_CART, values, COLUMN_CART_PRODUCT_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?", new String[]{String.valueOf(productId), String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    public boolean deleteItem(long productId, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CART, COLUMN_CART_PRODUCT_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?", new String[]{String.valueOf(productId), String.valueOf(userId)});
        db.close();
        return result > 0;
    }


    public void clearCart(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
