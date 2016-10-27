package com.np.ipcsocketdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketService extends Service {

    private boolean serviceIsDestroy = false;
    private String[] mMessages = {
            "蓝瘦香菇",
            "GG",
            "Hello!",
            "you are right!",
            "听说爱笑的人都会有好运~"
    };

    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new ServerSocketRunnable()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceIsDestroy = true;
    }

    class ServerSocketRunnable implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                // 监听本地 8888 端口
                serverSocket = new ServerSocket(8888);
            } catch (IOException e) {
                System.err.println("建立 tcp 服务失败，端口：8888");
                e.printStackTrace();
                return;
            }

            while (!serviceIsDestroy) {
                try {
                    // 接收客户端请求
                    final Socket socket = serverSocket.accept();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 响应客户端请求 **/
    private void responseClient(Socket socket) throws IOException {
        // 用于接收客户端信息
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 用于向客户端发送信息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!serviceIsDestroy) {
            String str = in.readLine();
            System.out.println("接收到客户端信息：" + str);
            if (str == null) {
                // 客户端断开连接
                break;
            }
            int i = new Random().nextInt(mMessages.length);
            String msg = mMessages[i];
            out.println(msg);
            System.out.println("发送信息到客户端：" + msg);
        }
        System.out.println("客户端断开连接");
        out.close();
        in.close();
        socket.close();
    }
}
