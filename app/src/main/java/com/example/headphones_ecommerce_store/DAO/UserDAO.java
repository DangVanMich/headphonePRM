package com.example.headphones_ecommerce_store.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.model.User;

public class UserDAO extends DBHelper {
    public UserDAO(Context context) {
        super(context);
    }
    public boolean updateUserBasicInfo(String userId, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FULLNAME, newName);
        values.put(COLUMN_USER_EMAIL, newEmail);

        int rows = db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{userId});
        db.close();
        return rows > 0;
    }

}
