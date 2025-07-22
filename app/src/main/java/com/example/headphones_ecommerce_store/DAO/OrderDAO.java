package com.example.headphones_ecommerce_store.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.models.CartItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDAO extends DBHelper {

    public OrderDAO(Context context) {
        super(context);
    }

    public boolean createOrder(List<CartItem> items, double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Thêm vào bảng orders
            ContentValues orderValues = new ContentValues();
            String currentDate = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(new Date());
            orderValues.put(COLUMN_ORDER_DATE, currentDate);
            orderValues.put(COLUMN_ORDER_TOTAL, total);
            orderValues.put(COLUMN_ORDER_STATUS, "Hoàn thành");

            long orderId = db.insertOrThrow(TABLE_ORDERS, null, orderValues);
            if (orderId == -1) return false;

            // 2. Thêm vào bảng order_items
            for (CartItem item : items) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(COLUMN_ORDER_ITEM_ORDER_ID, orderId);
                itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_NAME, item.getName());
                itemValues.put(COLUMN_ORDER_ITEM_QUANTITY, item.getQuantity());
                itemValues.put(COLUMN_ORDER_ITEM_PRICE, item.getPrice());
                if (db.insert(TABLE_ORDER_ITEMS, null, itemValues) == -1) {
                    return false;
                }
            }

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Sắp xếp để đơn hàng mới nhất lên đầu
        return db.query(TABLE_ORDERS, null, null, null, null, null, COLUMN_ORDER_ID + " DESC");
    }

    public Cursor getOrderItems(long orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ORDER_ITEMS, null, COLUMN_ORDER_ITEM_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)}, null, null, null);
    }
}
