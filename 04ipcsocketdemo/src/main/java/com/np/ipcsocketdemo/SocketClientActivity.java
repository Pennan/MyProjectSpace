package com.np.ipcsocketdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SocketClientActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RECEIVE_NEW_MSG = 1;
    private static final int CONNECTED_SUCCESS = 2;

    private EditText et_input;
    private Button btn_send;
    private TextView tv_content;

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RECEIVE_NEW_MSG: // 接收到服务端的消息
                    String content = tv_content.getText() + (String) msg.obj;
                    tv_content.setText(content);
                    break;
                case CONNECTED_SUCCESS: // 连接成功
                    btn_send.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);
        et_input = (EditText) findViewById(R.id.et_input);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_content = (TextView) findViewById(R.id.tv_content);

        Intent intent = new Intent();
        intent.setAction("com.np.ipcsocketdemo.SocketService.ACTION");
        intent.setPackage("com.np.ipcsocketdemo");
        startService(intent);

        btn_send.setOnClickListener(this);

        new Thread() {
            @Override
            public void run() {
                // 连接服务端服务
                connectTCPServer();
            }
        }.start();
    }

    /**
     * 连接 TCP 服务<br>
     * 为了确定能够连接成功，这里采用了超时重连的策略，每次连接失败后都会重新建立并尝试建立连接。
     * 为了降低重试机制开销，加入了休眠机制，即每次重试的时间间隔为 1000 ms.
     */
    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8888);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(CONNECTED_SUCCESS);
                System.out.println("连接服务端成功");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("连接TCP服务失败，正在重连中...");
            }
        }

        try {
            // 接收服务端的消息
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!SocketClientActivity.this.isFinishing()) {
                String msg = in.readLine();
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    String showMsg = "server" + time + ":" + msg + "\n";
                    mHandler.obtainMessage(RECEIVE_NEW_MSG, showMsg).sendToTarget();
                }
            }
            System.out.println("quit...");
            mPrintWriter.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatDateTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss)", Locale.CHINA).format(new Date(time));
    }

    @Override
    public void onClick(View v) {
        if (v == btn_send) {
            String inputStr = et_input.getText().toString();
            if (!TextUtils.isEmpty(inputStr) && mPrintWriter != null) {
                mPrintWriter.println(inputStr);
                String time = formatDateTime(System.currentTimeMillis());
                String content = tv_content.getText() + "client" + time + ":"  + inputStr + "\n";
                tv_content.setText(content);
                et_input.setText("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
