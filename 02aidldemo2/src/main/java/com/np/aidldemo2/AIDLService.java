package com.np.aidldemo2;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class AIDLService extends Service {
    public static final String TAG = "AIDL_Service";
    private AtomicBoolean mServiceIsDestroy = new AtomicBoolean(false); // 记录 Service 是否销毁
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>(); // 支持并发读/写
//    private CopyOnWriteArrayList<INewBookArrivedListener> mListeners = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<INewBookArrivedListener> mListeners = new RemoteCallbackList<>();

    private IBinder mBinder = new IBookManager.Stub() {

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission("com.np.aidldemo2.permission.ACCESS_BOOK_SERVICE");
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (packageName != null && !packageName.startsWith("com.np")) {
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public List<Book> getBookList() throws RemoteException { return mBooks;   }
        @Override
        public void addBook(Book book) throws RemoteException {
            Log.i(TAG, "接收到客户端传过来的数据 Book:" + book.toString());
            mBooks.add(book);
        }
        @Override
        public void registerListener(INewBookArrivedListener listener) throws RemoteException {
            /*if (listener != null && !mListeners.contains(listener)) {
                mListeners.add(listener);
                Log.i(TAG, "注册了监听~~" + listener);
            }*/
            if (listener != null) {
                mListeners.register(listener);
                Log.i(TAG, "注册了监听~~" + listener);
            }
        }
        @Override
        public void unregisterListener(INewBookArrivedListener listener) throws RemoteException {
            /*if (listener != null && mListeners.contains(listener)) {
                mListeners.remove(listener);
                Log.i(TAG, "解除了监听~~");
            } else {
                Log.i(TAG, "not found, can not unregister." + listener);
            }*/
            if (listener != null) {
                mListeners.unregister(listener);
                Log.i(TAG, "解除了监听~~");
            }
        }
    };

    private void newBookArrived(Book book) {
        /*for (INewBookArrivedListener listener : mListeners) {
            try {
                Log.i(TAG, "newBookArrived, notify listener:" + listener);
                listener.onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }*/
        int N = mListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            INewBookArrivedListener l = mListeners.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListeners.finishBroadcast();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book(1, "Android"));
        mBooks.add(new Book(2, "IOS"));
        new Thread(new AddBookThread()).start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.np.aidldemo2.permission.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            Log.i(TAG, "客户端木有权限");
            return null;
        }
        Log.i(TAG, "客户端有权限,并连接成功");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceIsDestroy.set(true);
    }

    class AddBookThread implements Runnable {
        @Override
        public void run() {
            while (!mServiceIsDestroy.get()) {
                try {
                    Thread.sleep(5000);
                    int bookId = mBooks.size() + 1;
                    Book newBook = new Book(bookId, "C++" + bookId);
                    mBooks.add(newBook);

                    newBookArrived(newBook);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
