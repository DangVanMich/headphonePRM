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

    /**
     * Thêm sản phẩm vào giỏ hàng.
     * Nếu sản phẩm đã tồn tại, nó sẽ cập nhật số lượng.
     * @param productId ID của sản phẩm.
     * @param quantity Số lượng cần thêm.
     */
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

    /**
     * Lấy tất cả sản phẩm trong giỏ hàng cùng với thông tin chi tiết.
     * @return Một Cursor chứa dữ liệu đã join từ bảng cart và products.
     */
    public Cursor getAllCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                "p." + COLUMN_PRODUCT_ID + ", " +
                "p." + COLUMN_PRODUCT_NAME + ", " +
                "p." + COLUMN_PRODUCT_PRICE + ", " +
                "p." + COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL + ", " +
                "c." + COLUMN_CART_QUANTITY + " " +
                "FROM " + TABLE_PRODUCTS + " p INNER JOIN " + TABLE_CART + " c " +
                "ON p." + COLUMN_PRODUCT_ID + " = c." + COLUMN_CART_PRODUCT_ID;
        return db.rawQuery(query, null);
    }

    /**
     * Cập nhật số lượng của một sản phẩm trong giỏ hàng.
     * @param productId ID của sản phẩm.
     * @param quantity Số lượng mới.
     * @return true nếu cập nhật thành công.
     */
    public boolean updateQuantity(long productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, quantity);
        int result = db.update(TABLE_CART, values, COLUMN_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        return result > 0;
    }

    /**
     * Xóa một sản phẩm khỏi giỏ hàng.
     * @param productId ID của sản phẩm cần xóa.
     * @return true nếu xóa thành công.
     */
    public boolean deleteItem(long productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CART, COLUMN_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        return result > 0;
    }
}
