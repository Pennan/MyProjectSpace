package com.np.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.np.aidldemo.aidl.Book;
import com.np.aidldemo.aidl.IBookManager;
import com.np.aidldemo.service.BookService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IBookManager mBookManager;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBookManager == null)
                return;
            // 当 Binder 死亡时，移出之前绑定的 binder 代理
            mBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBookManager = null;
            // TODO: 这里重新绑定远程 Service
            Intent intent = new Intent(MainActivity.this, BookService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    };
    
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBookManager = IBookManager.Stub.asInterface(service);

            try {
                // 在 客户端绑定远程服务成功后，给 service（即 Binder）设置死亡代理
                service.linkToDeath(mDeathRecipient, 0);
                List<Book> bookList = mBookManager.getBooks();
                for (Book book : bookList) {
                    Log.e(TAG, "Book=" + book);
                }
                Book book = new Book(1, "Android");
                mBookManager.addBook(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };
    // 一般是先 开启服务，然后再 绑定服务，那么 解绑服务的时候，不会停止服务，
    // 只有 手动关闭服务 stopService，服务才会停止
    public void startServiceClick(View view) {
        Intent intent = new Intent(this, BookService.class);
        startService(intent);
    }
    // 不过先 绑定服务，也会开启服务，在 解绑时 服务停止
    public void bindServiceClick(View view) {
        Intent intent = new Intent(this, BookService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
