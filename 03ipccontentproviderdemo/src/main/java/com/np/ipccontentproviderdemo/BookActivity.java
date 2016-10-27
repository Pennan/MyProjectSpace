package com.np.ipccontentproviderdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BookActivity extends AppCompatActivity {

    public static final String TAG = "Book_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Log.i(TAG, "BookActivity, current thread:" + Thread.currentThread().getName());
        // 查询所有 book 表信息
        Uri bookUri = Uri.parse("content://com.np.ipccontentproviderdemo.book.provider/book");
        Cursor bookCursor = getContentResolver().query(bookUri, new String[] {"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            int bookId = bookCursor.getInt(0);
            String bookName = bookCursor.getString(1);
            Log.i(TAG, "query book: bookId = " + bookId + ",bookName = " + bookName);
        }
        bookCursor.close();

        // 查询所有 user 表信息
        Uri userUri = Uri.parse("content://com.np.ipccontentproviderdemo.book.provider/user");
        ContentValues values = new ContentValues();
        values.put("_id", 3);
        values.put("name", "GG");
        values.put("sex", 1);
        getContentResolver().insert(userUri, values); // 添加一条数据
        Cursor userCursor = getContentResolver().query(userUri, new String[] {"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            int userId = userCursor.getInt(0);
            String userName = userCursor.getString(1);
            int userSex = userCursor.getInt(2);
            Log.i(TAG, "query user: userId = " + userId + ",userName = " + userName + ",userSex = " + userSex);
        }
        bookCursor.close();
    }
}
