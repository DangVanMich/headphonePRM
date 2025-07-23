package com.example.headphones_ecommerce_store.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public MessageDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        dbHelper.close();
    }

    public long addMessage(String sender, String receiver, String content, String timestamp) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_MESSAGE_SENDER, sender);
        values.put(DBHelper.COLUMN_MESSAGE_RECEIVER, receiver);
        values.put(DBHelper.COLUMN_MESSAGE_CONTENT, content);
        values.put(DBHelper.COLUMN_MESSAGE_TIMESTAMP, timestamp);
        long result = db.insert(DBHelper.TABLE_MESSAGE, null, values);
        close();
        return result;
    }

    public List<Message> getMessages(String sender, String receiver) {
        open();
        List<Message> messages = new ArrayList<>();
        String[] columns = {DBHelper.COLUMN_MESSAGE_ID, DBHelper.COLUMN_MESSAGE_SENDER, DBHelper.COLUMN_MESSAGE_RECEIVER,
                DBHelper.COLUMN_MESSAGE_CONTENT, DBHelper.COLUMN_MESSAGE_TIMESTAMP};
        String selection = "(" + DBHelper.COLUMN_MESSAGE_SENDER + " = ? AND " + DBHelper.COLUMN_MESSAGE_RECEIVER + " = ?) OR (" +
                DBHelper.COLUMN_MESSAGE_SENDER + " = ? AND " + DBHelper.COLUMN_MESSAGE_RECEIVER + " = ?)";
        String[] selectionArgs = {sender, receiver, receiver, sender};
        Cursor cursor = db.query(DBHelper.TABLE_MESSAGE, columns, selection, selectionArgs, null, null,
                DBHelper.COLUMN_MESSAGE_TIMESTAMP + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_MESSAGE_ID)));
                message.setSender(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_MESSAGE_SENDER)));
                message.setReceiver(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_MESSAGE_RECEIVER)));
                message.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_MESSAGE_CONTENT)));
                message.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_MESSAGE_TIMESTAMP)));
                messages.add(message);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return messages;
    }
}