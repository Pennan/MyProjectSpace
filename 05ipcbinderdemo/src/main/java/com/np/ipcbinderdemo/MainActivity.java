package com.np.ipcbinderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 利用countDownLatch实现主线程与子线程之间的同步
        // 定义 CountDownLatch，基数为2，表示有两个线程
        CountDownLatch countDownLatch = new CountDownLatch(2);
        MyThread myThread1 = new MyThread(countDownLatch, "Thread1");
        MyThread myThread2 = new MyThread(countDownLatch, "Thread2");
        myThread1.start();
        myThread2.start();

        try {
            // 主线程等待子线程执行完毕
            countDownLatch.await();
            mainEndWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void mainEndWork() {
        Log.i(TAG, "子线程执行完毕后，主线程继续执行");
    }

    class MyThread extends Thread {

        private CountDownLatch mCountDownLatch;
        private String mThreadName;

        public MyThread(CountDownLatch countDownLatch, String threadName) {
            this.mCountDownLatch = countDownLatch;
            this.mThreadName = threadName;
        }

        @Override
        public void run() {
            super.run();
            doWorking();
            mCountDownLatch.countDown(); // 计数减一，直到计数为零
            compute();
        }

        private void compute() {
            Log.i(TAG, mThreadName + "：执行完成...");
        }

        private void doWorking() {
            Log.i(TAG, mThreadName + "：开始执行...");
        }
    }
}
