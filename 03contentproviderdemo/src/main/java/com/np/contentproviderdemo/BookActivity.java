package com.np.contentproviderdemo;

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
        Uri uri = Uri.parse("content://com.np.ipccontentproviderdemo.book.provider");
        getContentResolver().query(uri, null, null, null, null);
    }
}
