package com.example.headphones_ecommerce_store.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.headphones_ecommerce_store.model.User;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ProductManagement.db";
    public static final int DATABASE_VERSION = 2;

    // User Table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_FULLNAME = "fullname";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Product Table

    // Products Table
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id"; // Already defined, but good to keep grouped
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_BRAND = "brand";
    public static final String COLUMN_PRODUCT_DESC = "description";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL = "thumbnail_image_url";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_STOCK_QUANTITY = "stock_quantity";
    public static final String COLUMN_PRODUCT_AVERAGE_RATING = "average_rating";
    public static final String COLUMN_PRODUCT_REVIEW_COUNT = "review_count";

    // Product Image URLs Table
    public static final String TABLE_PRODUCT_IMAGE_URLS = "product_image_urls";
    public static final String COLUMN_IMAGE_URL_ID = "id";
    public static final String COLUMN_IMAGE_URL_PRODUCT_ID = "product_id";
    public static final String COLUMN_IMAGE_URL_URL = "image_url";

    // Product Features Table
    public static final String TABLE_PRODUCT_FEATURES = "product_features";
    public static final String COLUMN_FEATURE_ID = "id";
    public static final String COLUMN_FEATURE_PRODUCT_ID = "product_id";
    public static final String COLUMN_FEATURE_TEXT = "feature";

    // Product Color Options Table
    public static final String TABLE_PRODUCT_COLOR_OPTIONS = "product_color_options";
    public static final String COLUMN_COLOR_OPTION_ID = "id";
    public static final String COLUMN_COLOR_OPTION_PRODUCT_ID = "product_id";
    public static final String COLUMN_COLOR_OPTION_NAME = "color_name";

    // Product Specifications Table
    public static final String TABLE_PRODUCT_SPECIFICATIONS = "product_specifications";
    public static final String COLUMN_SPECIFICATION_ID = "id";
    public static final String COLUMN_SPECIFICATION_PRODUCT_ID = "product_id";
    public static final String COLUMN_SPECIFICATION_KEY = "spec_key";
    public static final String COLUMN_SPECIFICATION_VALUE = "spec_value";

    //Cart Table
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_PRODUCT_ID = "product_id";
    public static final String COLUMN_CART_QUANTITY = "quantity";
    public static final String COLUMN_CART_USER_ID = "user_id";

    // Orders Table
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_TOTAL = "total_amount";
    public static final String COLUMN_ORDER_STATUS = "status";
    public static final String COLUMN_ORDER_USER_ID = "user_id";

    // Order Items Table
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COLUMN_ORDER_ITEM_ID = "item_id";
    public static final String COLUMN_ORDER_ITEM_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_ITEM_PRODUCT_NAME = "product_name";
    public static final String COLUMN_ORDER_ITEM_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_ITEM_PRICE = "price";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Message Table
    public static final String TABLE_MESSAGE = "message";
    public static final String COLUMN_MESSAGE_ID = "id";
    public static final String COLUMN_MESSAGE_SENDER = "sender";
    public static final String COLUMN_MESSAGE_RECEIVER = "receiver";
    public static final String COLUMN_MESSAGE_CONTENT = "content";
    public static final String COLUMN_MESSAGE_TIMESTAMP = "timestamp";
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_FULLNAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        //  Products Table
        String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"
                + COLUMN_PRODUCT_BRAND + " TEXT,"
                + COLUMN_PRODUCT_DESC + " TEXT,"
                + COLUMN_PRODUCT_PRICE + " REAL NOT NULL,"
                + COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL + " TEXT,"
                + COLUMN_PRODUCT_CATEGORY + " TEXT,"
                + COLUMN_PRODUCT_STOCK_QUANTITY + " INTEGER DEFAULT 0,"
                + COLUMN_PRODUCT_AVERAGE_RATING + " REAL DEFAULT 0.0,"
                + COLUMN_PRODUCT_REVIEW_COUNT + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_TABLE_PRODUCTS);

        //  Product Image URLs Table
        String CREATE_TABLE_PRODUCT_IMAGE_URLS = "CREATE TABLE " + TABLE_PRODUCT_IMAGE_URLS + "("
                + COLUMN_IMAGE_URL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_IMAGE_URL_PRODUCT_ID + " INTEGER NOT NULL,"
                + COLUMN_IMAGE_URL_URL + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + COLUMN_IMAGE_URL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_TABLE_PRODUCT_IMAGE_URLS);

        //  Product Features Table
        String CREATE_TABLE_PRODUCT_FEATURES = "CREATE TABLE " + TABLE_PRODUCT_FEATURES + "("
                + COLUMN_FEATURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FEATURE_PRODUCT_ID + " INTEGER NOT NULL,"
                + COLUMN_FEATURE_TEXT + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + COLUMN_FEATURE_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_TABLE_PRODUCT_FEATURES);

        // Product Color Options Table
        String CREATE_TABLE_PRODUCT_COLOR_OPTIONS = "CREATE TABLE " + TABLE_PRODUCT_COLOR_OPTIONS + "("
                + COLUMN_COLOR_OPTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COLOR_OPTION_PRODUCT_ID + " INTEGER NOT NULL,"
                + COLUMN_COLOR_OPTION_NAME + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + COLUMN_COLOR_OPTION_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_TABLE_PRODUCT_COLOR_OPTIONS);

        // Product Specifications Table
        String CREATE_TABLE_PRODUCT_SPECIFICATIONS = "CREATE TABLE " + TABLE_PRODUCT_SPECIFICATIONS + "("
                + COLUMN_SPECIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SPECIFICATION_PRODUCT_ID + " INTEGER NOT NULL,"
                + COLUMN_SPECIFICATION_KEY + " TEXT NOT NULL,"
                + COLUMN_SPECIFICATION_VALUE + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + COLUMN_SPECIFICATION_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_TABLE_PRODUCT_SPECIFICATIONS);

        //Cart Table
        String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CART_PRODUCT_ID + " INTEGER,"
                + COLUMN_CART_QUANTITY + " INTEGER,"
                + COLUMN_CART_USER_ID + " INTEGER," // <-- THÊM DÒNG NÀY
                + "FOREIGN KEY (" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + "),"
                + "FOREIGN KEY (" + COLUMN_CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" // <-- THÊM DÒNG NÀY
                + ")";
        db.execSQL(CREATE_TABLE_CART);

        String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_DATE + " TEXT,"
                + COLUMN_ORDER_TOTAL + " REAL,"
                + COLUMN_ORDER_STATUS + " TEXT,"
                + COLUMN_ORDER_USER_ID + " INTEGER,"
                + "FOREIGN KEY (" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_ORDERS);

        String CREATE_TABLE_ORDER_ITEMS = "CREATE TABLE " + TABLE_ORDER_ITEMS + "("
                + COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_ITEM_ORDER_ID + " INTEGER,"
                + COLUMN_ORDER_ITEM_PRODUCT_NAME + " TEXT,"
                + COLUMN_ORDER_ITEM_QUANTITY + " INTEGER,"
                + COLUMN_ORDER_ITEM_PRICE + " REAL,"
                + "FOREIGN KEY (" + COLUMN_ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_ORDER_ITEMS);

        String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "(" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MESSAGE_SENDER + " TEXT," +
                COLUMN_MESSAGE_RECEIVER + " TEXT," +
                COLUMN_MESSAGE_CONTENT + " TEXT," +
                COLUMN_MESSAGE_TIMESTAMP + " TEXT" +
                ")";
        db.execSQL(CREATE_MESSAGE_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SPECIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_COLOR_OPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_FEATURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_IMAGE_URLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        onCreate(db);
    }

    // Add user to database
    public long addUser(String fullName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FULLNAME, fullName);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password); // Note: In production, hash the password

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    // Check user credentials
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Get user full name
    public String getUserFullName(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_FULLNAME};
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        String fullName = null;
        if (cursor.moveToFirst()) {
            fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULLNAME));
        }
        cursor.close();
        db.close();
        return fullName;
    }
    // In DBHelper.java


    // Check if email exists
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ? COLLATE NOCASE"; // Case-insensitive matching
        String[] selectionArgs = {email.trim()};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        System.out.println("isEmailExists: Email " + email + " found count: " + count); // Debug log
        cursor.close();
        db.close();
        return count > 0;
    }

    // Update user password
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, newPassword);

        String whereClause = COLUMN_USER_EMAIL + " = ?";
        String[] whereArgs = {email};

        int rowsAffected = db.update(TABLE_USERS, values, whereClause, whereArgs);
        db.close();
        return rowsAffected > 0;
    }

    // Get full user details by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID, COLUMN_USER_FULLNAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD};
        String selection = COLUMN_USER_EMAIL + " = ? COLLATE NOCASE";
        String[] selectionArgs = {email.trim()};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))));
            user.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULLNAME))); // Assuming displayName = fullName
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
            // Placeholder for missing fields (to be implemented with additional tables)
            user.setProfileImageUrl(null); // Add table for profile images if needed
            user.setShippingAddresses(null); // Add shipping_addresses table
            user.setPaymentMethods(null); // Add payment_methods table
            user.setOrderHistory(null); // Add orders table
        }
        cursor.close();
        db.close();
        return user;
    }

    public long getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID}, COLUMN_USER_EMAIL + " = ?",
                new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            cursor.close();
            db.close();
            return userId;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return -1; // Trả về -1 nếu không tìm thấy
    }
}