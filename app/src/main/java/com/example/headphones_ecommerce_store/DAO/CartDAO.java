package com.example.headphones_ecommerce_store.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.headphones_ecommerce_store.database.DBHelper;

public class CartDAO extends DBHelper {

    public CartDAO(Context context) {
        super(context);
    }

    // Thêm sản phẩm vào giỏ hoặc cập nhật số lượng nếu đã tồn tại
    public void addToCart(long productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_PRODUCT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            // Nếu đã tồn tại, cập nhật số lượng
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
            int newQuantity = currentQuantity + quantity;

            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QUANTITY, newQuantity);
            db.update(TABLE_CART, values, COLUMN_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});

        } else {
            // Nếu chưa tồn tại, thêm mới
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_PRODUCT_ID, productId);
            values.put(COLUMN_CART_QUANTITY, quantity);
            db.insert(TABLE_CART, null, values);
        }
        cursor.close();
        db.close();
    }
}