package com.np.threaddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String msg = "123";
//        sleepAndInterrupt();
        waitAndInterrupt();
    }

    private void waitAndInterrupt() {
        final Thread tA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "进入 A 线程...");
                    Log.i(TAG, "A 线程执行 wait，进入等待池，等待被 notify/notifyAll or interrupt...");
                    synchronized (MainActivity.this) {
                        wait();
                    }
                    Log.i(TAG, "A 线程执行 wait 后，获得锁后，进入就绪状态，获取 CPU 后，执行完成...");
                } catch (InterruptedException e) {
                    Log.i(TAG, "A 线程等待被打断了 interrupt，进入等锁池，等待 CPU...");
                }
            }
        });
        tA.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "进入 B 线程...");
                tA.interrupt();
                Log.i(TAG, "B 线程执行完成...");
            }
        }).start();
    }

    private void sleepAndInterrupt() {
        final Thread tA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "进入 A 线程...");
                    Thread.sleep(5000);
                    Log.i(TAG, "A 线程睡眠完成后，进入就绪状态，等待 CPU...");
                } catch (InterruptedException e) {
                    Log.i(TAG, "A 线程睡眠被打断了，进入就绪状态，等待 CPU...");
                }
            }
        });
        tA.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "进入 B 线程...");
                tA.interrupt();
                Log.i(TAG, "B 线程执行完成...");
            }
        }).start();
    }
}
