package com.np.ipccontentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * <B>Des</B>
 *
 * @author ningpan 2016/10/10 15:53
 */
public class BookProvider extends ContentProvider {

    private static final String TAG = "Book_Provider";

    public static final String AUTHORITY = "com.np.ipccontentproviderdemo.book.provider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private SQLiteDatabase mDatabase;
    private Context mContext;

    static {
        // 将 Uri 与 Uri_Code 关联起来
        // 相当于 ："content://" + AUTHORITY + "/book"
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDatabase = new DBOpenHelper(mContext).getWritableDatabase();
        execSQLMethod();
        return true;
    }

    private void execSQLMethod() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabase.execSQL("delete from " + DBOpenHelper.BOOK_TABLE_NAME);
                mDatabase.execSQL("delete from " + DBOpenHelper.USER_TABLE_NAME);

                mDatabase.execSQL("insert into book values(3, 'Android');");
                mDatabase.execSQL("insert into book values(4, 'IOS');");
                mDatabase.execSQL("insert into book values(5, 'Html5');");

                mDatabase.execSQL("insert into user values(1, 'Pennan', 1);");
                mDatabase.execSQL("insert into user values(2, 'Cool', 0);");
            }
        }).start();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        return mDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        mDatabase.insert(tableName, null, values);
        // 通知外界当前 ContentProvider 中的数据已经发生了改变
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int count = mDatabase.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int row = mDatabase.update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    /** 根据 Uri 获取查询表的表名 **/
    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) { // 返回的是与 Uri 关联的 Uri_Code
            case BOOK_URI_CODE:
                tableName = DBOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DBOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }
}
