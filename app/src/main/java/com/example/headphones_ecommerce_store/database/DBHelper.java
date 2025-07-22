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
    public static final int DATABASE_VERSION = 1;

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

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

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
                + "FOREIGN KEY (" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_CART);

    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SPECIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_COLOR_OPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_FEATURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_IMAGE_URLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }
}