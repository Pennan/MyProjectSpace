package com.np.ipcbinderdemo;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BinderPoolActivity extends AppCompatActivity {

    private static final String TAG = BinderPoolActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        new Thread() {
            public void run() {
                doWork();
            }
        }.start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder computerBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTER);
        IComputer computer = ComputerImpl.asInterface(computerBinder);
        try {
            int add = computer.add(3, 5);
            Log.i(TAG, "IComputer: 3 + 5 = " + add);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder SecurityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        ISecurityCenter securityCenter = SecurityCenterImpl.asInterface(SecurityBinder);
        try {
            String msg = "Hello! 安卓大神";
            String encrypt = securityCenter.encrypt(msg);
            Log.i(TAG, "ISecurityCenter: encrypt ->" + encrypt);
            String decrypt = securityCenter.decrypt(encrypt);
            Log.i(TAG, "ISecurityCenter: decrypt ->" + decrypt);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
