package com.np.aidldemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.np.aidldemo.aidl.Book;
import com.np.aidldemo.aidl.IBookManager;

import java.util.ArrayList;
import java.util.List;

public class BookService extends Service {
    public static final String TAG = BookService.class.getSimpleName();

    public BookService() {
    }
    IBookManager.Stub mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            return getBookList();
        }
        @Override
        public void addBook(Book book) throws RemoteException {
            Log.i(TAG, "Book=" + book);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private List<Book> getBookList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Book book = new Book(i + 2, "Java" + i);
            books.add(book);
        }
        return books;
    }


}
