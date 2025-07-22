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

    /**
     * Lấy thông tin chi tiết của một sản phẩm dựa vào ID.
     * @param id ID của sản phẩm.
     * @return Đối tượng Product hoặc null nếu không tìm thấy.
     */
    public Product getProductById(long id) {
        // Hàm này đơn giản là gọi thẳng tới DAO vì DAO đã trả về đúng đối tượng Product
        return productDAO.getProductById(id);
    }

    /**
     * Lấy tất cả sản phẩm từ cơ sở dữ liệu.
     * @return Một danh sách các đối tượng Product.
     */
    public List<Product> getAllProducts() {
        Cursor cursor = productDAO.getAllProducts(null); // Lấy mặc định không sắp xếp
        return convertCursorToList(cursor);
    }

    /**
     * Tìm kiếm sản phẩm theo tên.
     * @param name Tên sản phẩm cần tìm.
     * @return Một danh sách các đối tượng Product khớp với tìm kiếm.
     */
    public List<Product> searchProducts(String name) {
        Cursor cursor = productDAO.searchProducts(name);
        return convertCursorToList(cursor);
    }

    /**
     * Hàm tiện ích để chuyển đổi dữ liệu từ Cursor sang List<Product>.
     * Đây là nhiệm vụ chính của Repository: cung cấp dữ liệu sạch cho lớp UI.
     * @param cursor Dữ liệu thô từ DAO.
     * @return Danh sách các sản phẩm đã được xử lý.
     */
    private List<Product> convertCursorToList(Cursor cursor) {
        List<Product> productList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                // Lấy dữ liệu từ Cursor và set cho đối tượng product
                product.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_DESC)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_PRICE)));
                product.setThumbnailImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL)));

                // Bạn có thể lấy thêm các trường khác ở đây nếu cần
                // product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_BRAND)));

                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return productList;
    }
}