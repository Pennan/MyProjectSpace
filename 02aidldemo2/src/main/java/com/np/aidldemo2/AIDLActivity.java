package com.np.aidldemo2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class AIDLActivity extends AppCompatActivity {

    public static final String TAG = "AIDL_Activity";
    private IBookManager mRemoteBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    Book book = (Book) msg.obj;
                    Log.i(TAG, "新书到了 Book：" + book.toString());
                    break;
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                List<Book> bookList = bookManager.getBookList();
                Log.i(TAG, "接收到服务端的数据 BookList: 数据类型:" + bookList.getClass().getCanonicalName()
                        + "数据值：" + bookList.toString());
                bookManager.addBook(new Book(3, "Java"));
                List<Book> newBookList = bookManager.getBookList();
                Log.i(TAG, "接收到服务端的数据值：" + newBookList.toString());

                bookManager.registerListener(mListener);
                Log.i(TAG, "Listener1:" + mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.i(TAG, "binder died.");
        }
    };

    private INewBookArrivedListener mListener = new INewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            mHandler.obtainMessage(100, book).sendToTarget();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unregisterListener(mListener);
                Log.i(TAG, "Listener2:" + mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
    }
}
